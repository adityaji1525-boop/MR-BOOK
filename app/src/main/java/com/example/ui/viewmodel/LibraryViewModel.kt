package com.example.ui.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.LiveBookKnowledgeRepository
import com.example.ai.LiveKnowledgeUiState
import com.example.data.BookProgressDoc
import com.example.data.DownloadedSectionEntity
import com.example.data.LibraryCatalog
import com.example.data.LibraryFloorLayout
import com.example.data.LocalLibraryRepository
import com.example.data.MarginNoteDoc
import com.example.data.OfflineSectionManager
import com.example.data.ReadingHistoryDoc
import com.example.data.SectionDownloadProgress
import com.example.data.UserProfileDoc
import com.example.model.ArtworkStroke
import com.example.model.Book
import com.example.model.BookCategory
import com.example.model.CameraMode
import com.example.model.LampAmbience
import com.example.model.LibraryProject
import com.example.model.SavedArtwork
import com.example.model.TableCoverStyle
import com.example.model.UserScienceTemplate
import java.util.UUID
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.hypot

sealed interface ActiveHallOverlay {
    data object None : ActiveHallOverlay
    data class ShelfBrowser(val shelfNumber: Int) : ActiveHallOverlay
    data object HelpMap : ActiveHallOverlay
    data object LibrarianDesk : ActiveHallOverlay
    data object LibraryCard : ActiveHallOverlay
    data object CharacterEditor : ActiveHallOverlay
    data object CatalogSearch : ActiveHallOverlay
    data object SectionDownloads : ActiveHallOverlay
    data object AppSettings : ActiveHallOverlay
}

sealed interface ProfileLoadState {
    data object Loading : ProfileLoadState
    data class Ready(val profile: UserProfileDoc?) : ProfileLoadState
    data class Error(val message: String) : ProfileLoadState
}

data class HallGameState(
    val charX: Float = 0.50f,
    val charY: Float = 0.31f,
    val targetX: Float = 0.50f,
    val targetY: Float = 0.31f,
    val isWalking: Boolean = false,
    val facingRight: Boolean = true,
    val walkPhase: Float = 0f,
    val isSeatedAtTableId: String? = null,
    val cameraMode: CameraMode = CameraMode.LIBRARY_HALL,
    val activeBookId: String? = null,
    val activeOverlay: ActiveHallOverlay = ActiveHallOverlay.None,
    val highlightedShelfNumber: Int? = null,
    val nearbyShelfNumber: Int? = null,
    val nearbyTableId: String? = null,
    val nearbyLibrarian: Boolean = false,
    val statusToast: String? = null,
    val librarianWhisper: String = "USE THE JOYSTICK TO WALK TO SHELVES #1–#10, OR ENTER LIBRARY CENTRE FOR PROJECTS, ARTWORK & BOOKS!",
    val focusSecondsRemaining: Int = 15 * 60,
    val isFocusTimerRunning: Boolean = false,
    val isLampOn: Boolean = true,
    val lampBrightnessPreview: Float? = null,
    val tableCover: TableCoverStyle = TableCoverStyle.ROYAL_BURGUNDY,
    val projects: List<LibraryProject> = emptyList(),
    val savedArtworks: List<SavedArtwork> = emptyList(),
    val customModelTemplates: List<UserScienceTemplate> = emptyList()
)

class LibraryViewModel(
    private val libraryRepo: LocalLibraryRepository,
    private val offlineManager: OfflineSectionManager,
    private val prefs: SharedPreferences? = null,
    private val liveKnowledgeRepo: LiveBookKnowledgeRepository = LiveBookKnowledgeRepository(prefs)
) : ViewModel() {

    val profileState: StateFlow<ProfileLoadState> = libraryRepo.observeProfile()
        .map<UserProfileDoc?, ProfileLoadState> { ProfileLoadState.Ready(it) }
        .catch { error ->
            emit(ProfileLoadState.Error(error.message ?: "Failed to load reader profile"))
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProfileLoadState.Loading)

    val bookProgressList: StateFlow<List<BookProgressDoc>> = libraryRepo.observeBookProgress()
        .catch { emit(emptyList()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val readingHistory: StateFlow<List<ReadingHistoryDoc>> = libraryRepo.observeReadingHistory()
        .catch { emit(emptyList()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allNotes: StateFlow<List<MarginNoteDoc>> = libraryRepo.observeMarginNotes()
        .catch { emit(emptyList()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val downloadedSections: StateFlow<List<DownloadedSectionEntity>> = offlineManager.downloadedSections
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeDownloads: StateFlow<Map<String, SectionDownloadProgress>> = offlineManager.activeDownloads

    private val _liveBookTopicState = MutableStateFlow<LiveKnowledgeUiState>(LiveKnowledgeUiState.Idle)
    val liveBookTopicState: StateFlow<LiveKnowledgeUiState> = _liveBookTopicState.asStateFlow()

    private val _liveGlobalSearchState = MutableStateFlow<LiveKnowledgeUiState>(LiveKnowledgeUiState.Idle)
    val liveGlobalSearchState: StateFlow<LiveKnowledgeUiState> = _liveGlobalSearchState.asStateFlow()

    private val _autoLiveGoogleSync = MutableStateFlow(
        prefs?.getBoolean("auto_live_google_sync", true) ?: true
    )
    val autoLiveGoogleSync: StateFlow<Boolean> = _autoLiveGoogleSync.asStateFlow()

    private val _gameState = MutableStateFlow(
        HallGameState(
            tableCover = TableCoverStyle.entries.firstOrNull {
                it.id == prefs?.getString("table_cover_id", TableCoverStyle.ROYAL_BURGUNDY.id)
            } ?: TableCoverStyle.ROYAL_BURGUNDY,
            projects = LibraryProject.listFromJsonString(prefs?.getString("projects_json", null)),
            savedArtworks = SavedArtwork.listFromJsonString(prefs?.getString("artworks_json", null)),
            customModelTemplates = UserScienceTemplate.listFromJsonString(prefs?.getString("custom_templates_json", null))
        )
    )
    val gameState: StateFlow<HallGameState> = _gameState.asStateFlow()

    private var walkJob: Job? = null
    private var joystickJob: Job? = null
    private var joystickDirX: Float = 0f
    private var joystickDirY: Float = 0f
    private var lastDismissedShelfNumber: Int? = null
    private var dismissedLibrarianThisVisit: Boolean = false
    private var timerJob: Job? = null
    private var elapsedStudySeconds = 0
    private var liveTopicJob: Job? = null
    private var liveGlobalSearchJob: Job? = null

    fun setAutoLiveGoogleSync(enabled: Boolean) {
        prefs?.edit()?.putBoolean("auto_live_google_sync", enabled)?.apply()
        _autoLiveGoogleSync.value = enabled
    }

    fun loadLiveBookTopic(
        book: Book,
        topicTitle: String,
        chapterContext: String = "",
        customQuestion: String? = null,
        forceRefresh: Boolean = false
    ) {
        val effectiveTopic = customQuestion?.trim()?.takeIf { it.isNotBlank() } ?: topicTitle
        if (customQuestion.isNullOrBlank() && !forceRefresh) {
            liveKnowledgeRepo.getCachedTopic(book.id, effectiveTopic)?.let { cached ->
                _liveBookTopicState.value = LiveKnowledgeUiState.Success(cached)
                return
            }
        }
        liveTopicJob?.cancel()
        liveTopicJob = viewModelScope.launch {
            _liveBookTopicState.value = LiveKnowledgeUiState.Loading(effectiveTopic)
            try {
                val result = liveKnowledgeRepo.fetchTopicKnowledge(
                    book = book,
                    topicTitle = topicTitle,
                    chapterContext = chapterContext,
                    customQuestion = customQuestion,
                    forceRefresh = forceRefresh
                )
                _liveBookTopicState.value = result
            } catch (e: CancellationException) {
                throw e
            }
        }
    }

    fun clearLiveBookTopic() {
        liveTopicJob?.cancel()
        _liveBookTopicState.value = LiveKnowledgeUiState.Idle
    }

    fun searchRealWorldBookTopicLive(query: String) {
        val clean = query.trim()
        if (clean.isBlank()) return
        liveGlobalSearchJob?.cancel()
        liveGlobalSearchJob = viewModelScope.launch {
            _liveGlobalSearchState.value = LiveKnowledgeUiState.Loading(clean)
            try {
                val result = liveKnowledgeRepo.searchRealWorldBookOrTopic(clean)
                _liveGlobalSearchState.value = result
            } catch (e: CancellationException) {
                throw e
            }
        }
    }

    fun clearLiveGlobalSearch() {
        liveGlobalSearchJob?.cancel()
        _liveGlobalSearchState.value = LiveKnowledgeUiState.Idle
    }

    fun isSectionReadyOffline(category: BookCategory): Boolean {
        val dbReady = downloadedSections.value.any { it.categoryId == category.name && it.isDownloaded }
        return dbReady && offlineManager.isPackFilePresent(category)
    }

    fun downloadSection(category: BookCategory, thenOpenBook: Book? = null) {
        viewModelScope.launch {
            _gameState.update {
                it.copy(
                    librarianWhisper = "DOWNLOADING ${category.displayName} (${category.packSizeMb} MB) FOR OFFLINE ACCESS..."
                )
            }
            offlineManager.downloadSection(category)
            libraryRepo.logHistory(
                bookId = "section_${category.name.lowercase()}",
                bookTitle = "${category.displayName} OFFLINE PACK (${category.packSizeMb} MB)",
                categoryName = category.displayName,
                levelName = "ALL LEVELS",
                action = "downloaded_section",
                pageNumber = 0
            )
            libraryRepo.updateStatus("${category.displayName} (${category.packSizeMb} MB) READY OFFLINE")
            _gameState.update {
                it.copy(
                    statusToast = "${category.displayName} (${category.packSizeMb} MB) DOWNLOADED! ALL BOOKS IN THIS SECTION ARE NOW AVAILABLE OFFLINE.",
                    librarianWhisper = "${category.displayName} SECTION IS NOW AVAILABLE OFFLINE!"
                )
            }
            if (thenOpenBook != null) {
                borrowBook(thenOpenBook, sitImmediately = true)
            }
        }
    }

    fun downloadAllSections() {
        viewModelScope.launch {
            BookCategory.entries.forEach { category ->
                if (!isSectionReadyOffline(category)) {
                    offlineManager.downloadSection(category)
                    libraryRepo.logHistory(
                        bookId = "section_${category.name.lowercase()}",
                        bookTitle = "${category.displayName} OFFLINE PACK (${category.packSizeMb} MB)",
                        categoryName = category.displayName,
                        levelName = "ALL LEVELS",
                        action = "downloaded_section",
                        pageNumber = 0
                    )
                }
            }
            _gameState.update {
                it.copy(statusToast = "ALL 7 BOOK SECTIONS DOWNLOADED FOR FULL OFFLINE STUDY!")
            }
        }
    }

    fun removeSection(category: BookCategory) {
        viewModelScope.launch {
            offlineManager.removeSection(category)
            _gameState.update {
                it.copy(statusToast = "REMOVED ${category.displayName} OFFLINE PACK (${category.packSizeMb} MB FREED).")
            }
        }
    }

    fun saveCharacter(name: String, avatarId: String, cloakColorIndex: Int) {
        viewModelScope.launch {
            val cleanName = name.trim().ifBlank { "READER" }.uppercase()
            try {
                libraryRepo.saveCharacterProfile(
                    characterName = cleanName,
                    avatarId = avatarId,
                    cloakColorIndex = cloakColorIndex,
                    statusText = "EXPLORING THE GRAND LIBRARY"
                )
                _gameState.update {
                    it.copy(
                        activeOverlay = ActiveHallOverlay.None,
                        librarianWhisper = "WELCOME, $cleanName! DOWNLOAD A BOOK SECTION (20–80 MB) AND WALK TO SHELVES 1–10 TO BEGIN."
                    )
                }
            } catch (e: Exception) {
                _gameState.update {
                    it.copy(statusToast = "COULD NOT SAVE PROFILE: ${e.localizedMessage?.uppercase() ?: ""}")
                }
            }
        }
    }

    fun updateCustomStatus(statusText: String) {
        viewModelScope.launch {
            libraryRepo.updateStatus(statusText.trim().uppercase())
            _gameState.update { it.copy(statusToast = "READER STATUS UPDATED.") }
        }
    }

    fun openOverlay(overlay: ActiveHallOverlay) {
        joystickJob?.cancel()
        joystickDirX = 0f
        joystickDirY = 0f
        _gameState.update { it.copy(activeOverlay = overlay, isWalking = false, walkPhase = 0f) }
    }

    fun closeOverlay() {
        _gameState.update { state ->
            if (state.activeOverlay is ActiveHallOverlay.ShelfBrowser) {
                lastDismissedShelfNumber = state.activeOverlay.shelfNumber
            }
            if (state.activeOverlay == ActiveHallOverlay.LibrarianDesk ||
                state.activeOverlay == ActiveHallOverlay.CatalogSearch
            ) {
                if (state.nearbyLibrarian) {
                    dismissedLibrarianThisVisit = true
                }
            }
            state.copy(activeOverlay = ActiveHallOverlay.None)
        }
    }

    fun clearStatusToast() {
        _gameState.update { it.copy(statusToast = null) }
    }

    fun walkToPoint(normX: Float, normY: Float, onArrived: (() -> Unit)? = null) {
        joystickJob?.cancel()
        joystickDirX = 0f
        joystickDirY = 0f
        val clampedX = normX.coerceIn(0.20f, 0.80f)
        val clampedY = normY.coerceIn(0.18f, 0.88f)
        startWalk(clampedX, clampedY, clearSeated = true, onArrived = onArrived)
    }

    fun walkToShelf(shelfNumber: Int) {
        val shelf = LibraryFloorLayout.bookshelves.firstOrNull { it.shelfNumber == shelfNumber } ?: return
        lastDismissedShelfNumber = null
        joystickJob?.cancel()
        joystickDirX = 0f
        joystickDirY = 0f
        val standX = if (shelf.isLeftRow) 0.24f else 0.76f
        val standY = shelf.normY
        _gameState.update {
            it.copy(
                activeOverlay = ActiveHallOverlay.None,
                highlightedShelfNumber = shelfNumber,
                librarianWhisper = "WALKING TO SHELF #$shelfNumber (${shelf.category.displayName})."
            )
        }
        startWalk(standX, standY, clearSeated = true) {
            viewModelScope.launch {
                libraryRepo.updateStatus("BROWSING SHELF #$shelfNumber • ${shelf.category.displayName}")
            }
            _gameState.update {
                it.copy(
                    nearbyShelfNumber = shelfNumber,
                    activeOverlay = ActiveHallOverlay.ShelfBrowser(shelfNumber)
                )
            }
        }
    }

    fun walkToLibrarian() {
        dismissedLibrarianThisVisit = false
        joystickJob?.cancel()
        joystickDirX = 0f
        joystickDirY = 0f
        val (lx, _) = LibraryFloorLayout.librarianSpot
        startWalk(lx, 0.28f, clearSeated = true) {
            viewModelScope.launch {
                libraryRepo.updateStatus("AT LIBRARY CENTRE WITH LIBRARIAN ELEANOR")
            }
            _gameState.update {
                it.copy(
                    nearbyLibrarian = true,
                    activeOverlay = ActiveHallOverlay.LibrarianDesk,
                    librarianWhisper = "WELCOME TO THE LIBRARY CENTRE! PROJECTS, ARTWORK & BOOKS ARE READY HERE."
                )
            }
        }
    }

    fun walkToTableAndSit(tableId: String? = null, preferredBookId: String? = null) {
        joystickJob?.cancel()
        joystickDirX = 0f
        joystickDirY = 0f
        val table = LibraryFloorLayout.studyTables.firstOrNull { it.tableId == tableId }
            ?: nearestTable(_gameState.value.charX, _gameState.value.charY)

        val preferredBook = preferredBookId?.let { LibraryCatalog.findById(it) }
        _gameState.update {
            it.copy(
                activeOverlay = ActiveHallOverlay.None,
                nearbyShelfNumber = null,
                librarianWhisper = if (preferredBook != null) {
                    "CARRYING \"${preferredBook.title}\" TO ${table.label} TO READ..."
                } else {
                    "WALKING TO ${table.label}..."
                }
            )
        }
        startWalk(table.normX, table.normY + 0.08f, clearSeated = false) {
            val activeBorrowed = bookProgressList.value.filter { it.status == "borrowed" || it.status == "reading" }
            val chosenId = preferredBookId
                ?: _gameState.value.activeBookId?.takeIf { id -> activeBorrowed.any { it.bookId == id } }
                ?: activeBorrowed.firstOrNull()?.bookId

            val chosenBook = chosenId?.let { LibraryCatalog.findById(it) }
            if (chosenBook == null) {
                _gameState.update {
                    it.copy(
                        isSeatedAtTableId = table.tableId,
                        statusToast = "YOUR ARMS ARE EMPTY! WALK TO SHELVES 1–10 AND PICK A BOOK FIRST.",
                        librarianWhisper = "SEATED AT ${table.label}. PICK A BOOK FROM SHELVES 1–10 TO OPEN IT HERE!"
                    )
                }
            } else if (!isSectionReadyOffline(chosenBook.category)) {
                _gameState.update {
                    it.copy(
                        isSeatedAtTableId = table.tableId,
                        statusToast = "DOWNLOAD ${chosenBook.category.displayName} (${chosenBook.category.packSizeMb} MB) FIRST TO OPEN IT AT THE TABLE.",
                        activeOverlay = ActiveHallOverlay.SectionDownloads
                    )
                }
            } else {
                viewModelScope.launch {
                    libraryRepo.updateStatus("STUDYING AT ${table.label}")
                    libraryRepo.logHistory(
                        bookId = chosenBook.id,
                        bookTitle = chosenBook.title,
                        categoryName = chosenBook.category.displayName,
                        levelName = chosenBook.level.displayName,
                        action = "opened",
                        pageNumber = (bookProgressList.value.firstOrNull { it.bookId == chosenBook.id }?.currentPage ?: 0L).toInt()
                    )
                }
                _gameState.update {
                    it.copy(
                        isSeatedAtTableId = table.tableId,
                        activeBookId = chosenBook.id,
                        cameraMode = CameraMode.TABLE_DESK_POV,
                        librarianWhisper = "ENJOY READING AT ${table.label} UNDER THE ${table.lampTitle}."
                    )
                }
            }
        }
    }

    fun onJoystickInput(dx: Float, dy: Float) {
        val current = _gameState.value
        if (current.cameraMode != CameraMode.LIBRARY_HALL) return
        if (current.activeOverlay != ActiveHallOverlay.None) {
            joystickDirX = 0f
            joystickDirY = 0f
            joystickJob?.cancel()
            return
        }

        val magnitude = hypot(dx, dy)
        if (magnitude < 0.10f) {
            joystickDirX = 0f
            joystickDirY = 0f
            joystickJob?.cancel()
            val s = _gameState.value
            val shelfNearby = findNearbyShelf(s.charX, s.charY)
            val tableNearby = findNearbyTable(s.charX, s.charY)
            val atLibrarianCentre = isNearLibrarianCentre(s.charX, s.charY)
            val shouldOpenShelf = shelfNearby != null &&
                shelfNearby != lastDismissedShelfNumber &&
                s.activeOverlay == ActiveHallOverlay.None
            val shouldOpenLibrarian = !shouldOpenShelf &&
                atLibrarianCentre &&
                !dismissedLibrarianThisVisit &&
                s.activeOverlay == ActiveHallOverlay.None
            val shelfInfo = shelfNearby?.let { num ->
                LibraryFloorLayout.bookshelves.firstOrNull { it.shelfNumber == num }
            }
            _gameState.update {
                it.copy(
                    isWalking = false,
                    walkPhase = 0f,
                    nearbyShelfNumber = shelfNearby,
                    nearbyTableId = tableNearby,
                    nearbyLibrarian = atLibrarianCentre,
                    highlightedShelfNumber = shelfNearby ?: it.highlightedShelfNumber,
                    activeOverlay = when {
                        shouldOpenShelf -> ActiveHallOverlay.ShelfBrowser(shelfNearby!!)
                        shouldOpenLibrarian -> ActiveHallOverlay.LibrarianDesk
                        else -> it.activeOverlay
                    },
                    librarianWhisper = when {
                        shelfInfo != null ->
                            "REACHED SHELF #${shelfInfo.shelfNumber} (${shelfInfo.category.displayName}) — CHOOSE A BOOK!"
                        atLibrarianCentre ->
                            "ENTERED LIBRARY CENTRE — PROJECTS, ARTWORK & BOOKS ARE READY!"
                        else -> it.librarianWhisper
                    }
                )
            }
            return
        }

        walkJob?.cancel()
        joystickDirX = dx.coerceIn(-1f, 1f)
        joystickDirY = dy.coerceIn(-1f, 1f)

        if (joystickJob?.isActive == true) return
        joystickJob = viewModelScope.launch {
            var stalledFrames = 0
            val speedPerFrame = 0.0085f
            while (hypot(joystickDirX, joystickDirY) >= 0.10f) {
                val s = _gameState.value
                if (s.cameraMode != CameraMode.LIBRARY_HALL || s.activeOverlay != ActiveHallOverlay.None) {
                    break
                }
                val nx = (s.charX + joystickDirX * speedPerFrame).coerceIn(0.20f, 0.80f)
                val ny = (s.charY + joystickDirY * speedPerFrame).coerceIn(0.18f, 0.88f)
                val shelfNearby = findNearbyShelf(nx, ny)
                val tableNearby = findNearbyTable(nx, ny)
                val atLibrarianCentre = isNearLibrarianCentre(nx, ny)
                if (shelfNearby == null) {
                    lastDismissedShelfNumber = null
                }
                if (!atLibrarianCentre) {
                    dismissedLibrarianThisVisit = false
                }

                val movedDist = hypot(nx - s.charX, ny - s.charY)
                val canAutoOpenShelf = shelfNearby != null && shelfNearby != lastDismissedShelfNumber
                val canAutoOpenCentre = atLibrarianCentre && !dismissedLibrarianThisVisit
                if ((canAutoOpenShelf || canAutoOpenCentre) && movedDist < 0.002f) {
                    stalledFrames++
                } else {
                    stalledFrames = 0
                }

                val shelfInfo = shelfNearby?.let { num ->
                    LibraryFloorLayout.bookshelves.firstOrNull { it.shelfNumber == num }
                }
                val autoOpenShelfFromEdge = stalledFrames >= 20 && canAutoOpenShelf
                val autoOpenCentreFromEdge = !autoOpenShelfFromEdge && stalledFrames >= 20 && canAutoOpenCentre
                val autoOpenedAny = autoOpenShelfFromEdge || autoOpenCentreFromEdge

                _gameState.update {
                    it.copy(
                        charX = nx,
                        charY = ny,
                        targetX = nx,
                        targetY = ny,
                        isWalking = !autoOpenedAny,
                        isSeatedAtTableId = null,
                        facingRight = joystickDirX >= 0f,
                        walkPhase = if (autoOpenedAny) 0f else (it.walkPhase + 0.35f) % (2f * Math.PI.toFloat()),
                        nearbyShelfNumber = shelfNearby,
                        nearbyTableId = tableNearby,
                        nearbyLibrarian = atLibrarianCentre,
                        highlightedShelfNumber = shelfNearby ?: it.highlightedShelfNumber,
                        activeOverlay = when {
                            autoOpenShelfFromEdge -> ActiveHallOverlay.ShelfBrowser(shelfNearby!!)
                            autoOpenCentreFromEdge -> ActiveHallOverlay.LibrarianDesk
                            else -> it.activeOverlay
                        },
                        librarianWhisper = when {
                            shelfInfo != null ->
                                "REACHED SHELF #${shelfInfo.shelfNumber} (${shelfInfo.category.displayName}) — CHOOSE A BOOK!"
                            atLibrarianCentre ->
                                "ENTERED LIBRARY CENTRE — PROJECTS, ARTWORK & BOOKS ARE READY!"
                            else -> it.librarianWhisper
                        }
                    )
                }
                if (autoOpenedAny) {
                    joystickDirX = 0f
                    joystickDirY = 0f
                    break
                }
                delay(16L)
            }
            _gameState.update { it.copy(isWalking = false, walkPhase = 0f) }
        }
    }

    private fun startWalk(
        destX: Float,
        destY: Float,
        clearSeated: Boolean,
        onArrived: (() -> Unit)? = null
    ) {
        walkJob?.cancel()
        walkJob = viewModelScope.launch {
            _gameState.update {
                it.copy(
                    targetX = destX,
                    targetY = destY,
                    isWalking = true,
                    isSeatedAtTableId = if (clearSeated) null else it.isSeatedAtTableId,
                    facingRight = destX >= it.charX
                )
            }
            val speedPerFrame = 0.014f
            while (true) {
                val s = _gameState.value
                val dx = destX - s.charX
                val dy = destY - s.charY
                val dist = hypot(dx, dy)
                if (dist <= speedPerFrame) {
                    val shelfNearby = findNearbyShelf(destX, destY)
                    val tableNearby = findNearbyTable(destX, destY)
                    val atLibrarianCentre = isNearLibrarianCentre(destX, destY)
                    _gameState.update {
                        it.copy(
                            charX = destX,
                            charY = destY,
                            isWalking = false,
                            walkPhase = 0f,
                            nearbyShelfNumber = shelfNearby,
                            nearbyTableId = tableNearby,
                            nearbyLibrarian = atLibrarianCentre
                        )
                    }
                    break
                } else {
                    val nx = s.charX + (dx / dist) * speedPerFrame
                    val ny = s.charY + (dy / dist) * speedPerFrame
                    _gameState.update {
                        it.copy(
                            charX = nx,
                            charY = ny,
                            walkPhase = (it.walkPhase + 0.35f) % (2f * Math.PI.toFloat()),
                            facingRight = dx >= 0f,
                            nearbyShelfNumber = findNearbyShelf(nx, ny),
                            nearbyTableId = findNearbyTable(nx, ny),
                            nearbyLibrarian = isNearLibrarianCentre(nx, ny)
                        )
                    }
                }
                delay(16L)
            }
            onArrived?.invoke()
        }
    }

    fun borrowBook(book: Book, sitImmediately: Boolean) {
        if (!isSectionReadyOffline(book.category)) {
            downloadSection(book.category, thenOpenBook = if (sitImmediately) book else null)
            return
        }
        viewModelScope.launch {
            val activeStack = bookProgressList.value.filter { it.status == "borrowed" || it.status == "reading" }
            val existing = bookProgressList.value.firstOrNull { it.bookId == book.id }
            if (existing == null && activeStack.size >= 4) {
                _gameState.update {
                    it.copy(statusToast = "DESK STACK FULL (4 BOOKS MAX). RETURN ONE AT THE LIBRARIAN DESK FIRST.")
                }
                return@launch
            }
            libraryRepo.upsertBookProgress(
                bookId = book.id,
                bookTitle = book.title,
                categoryId = book.category.name,
                currentPage = (existing?.currentPage ?: 0L).toInt(),
                totalPages = book.pages.size,
                isBookmarked = existing?.isBookmarked ?: false,
                bookmarkPage = (existing?.bookmarkPage ?: 0L).toInt(),
                isCompleted = existing?.isCompleted ?: false,
                status = if (sitImmediately) "reading" else "borrowed"
            )
            libraryRepo.logHistory(
                bookId = book.id,
                bookTitle = book.title,
                categoryName = book.category.displayName,
                levelName = book.level.displayName,
                action = "borrowed",
                pageNumber = (existing?.currentPage ?: 0L).toInt()
            )
            _gameState.update {
                it.copy(
                    activeBookId = book.id,
                    statusToast = if (sitImmediately) null else "TOOK \"${book.title}\" FROM SHELF #${book.shelfNumber}."
                )
            }
            if (sitImmediately) {
                walkToTableAndSit(preferredBookId = book.id)
            }
        }
    }

    fun returnBook(bookId: String) {
        val book = LibraryCatalog.findById(bookId)
        viewModelScope.launch {
            libraryRepo.removeBookProgress(bookId)
            if (book != null) {
                libraryRepo.logHistory(
                    bookId = book.id,
                    bookTitle = book.title,
                    categoryName = book.category.displayName,
                    levelName = book.level.displayName,
                    action = "returned",
                    pageNumber = 0
                )
            }
            val remaining = bookProgressList.value.filterNot { it.bookId == bookId }
            _gameState.update { state ->
                val nextActive = if (state.activeBookId == bookId) remaining.firstOrNull()?.bookId else state.activeBookId
                val nextCamera = if (nextActive == null && state.cameraMode == CameraMode.TABLE_DESK_POV) {
                    CameraMode.LIBRARY_HALL
                } else {
                    state.cameraMode
                }
                state.copy(
                    activeBookId = nextActive,
                    cameraMode = nextCamera,
                    statusToast = "RETURNED BOOK TO ITS SHELF."
                )
            }
        }
    }

    fun openBookAtTable(bookId: String) {
        val book = LibraryCatalog.findById(bookId) ?: return
        if (!isSectionReadyOffline(book.category)) {
            downloadSection(book.category, thenOpenBook = book)
            return
        }
        viewModelScope.launch {
            val existing = bookProgressList.value.firstOrNull { it.bookId == book.id }
            libraryRepo.upsertBookProgress(
                bookId = book.id,
                bookTitle = book.title,
                categoryId = book.category.name,
                currentPage = (existing?.currentPage ?: 0L).toInt(),
                totalPages = book.pages.size,
                isBookmarked = existing?.isBookmarked ?: false,
                bookmarkPage = (existing?.bookmarkPage ?: 0L).toInt(),
                isCompleted = existing?.isCompleted ?: false,
                status = "reading"
            )
            libraryRepo.updateStatus("READING AT STUDY TABLE")
            libraryRepo.logHistory(
                bookId = book.id,
                bookTitle = book.title,
                categoryName = book.category.displayName,
                levelName = book.level.displayName,
                action = "opened",
                pageNumber = (existing?.currentPage ?: 0L).toInt()
            )
            _gameState.update {
                it.copy(
                    activeBookId = bookId,
                    cameraMode = CameraMode.TABLE_DESK_POV,
                    activeOverlay = ActiveHallOverlay.None
                )
            }
        }
    }

    fun standUpFromTable() {
        pauseFocusTimer()
        viewModelScope.launch {
            libraryRepo.updateStatus("WALKING IN THE LIBRARY HALL")
        }
        _gameState.update {
            it.copy(
                cameraMode = CameraMode.LIBRARY_HALL,
                isSeatedAtTableId = null,
                librarianWhisper = "STOOD UP FROM THE STUDY TABLE. READY FOR YOUR NEXT SHELF?"
            )
        }
    }

    fun turnToPage(book: Book, newPageIndex: Int) {
        val maxPage = book.pages.size
        val clamped = newPageIndex.coerceIn(0, maxPage)
        val existing = bookProgressList.value.firstOrNull { it.bookId == book.id }
        val prevPage = (existing?.currentPage ?: 0L).toInt()
        val wasCompleted = existing?.isCompleted == true
        val nowCompleted = wasCompleted || clamped >= maxPage

        viewModelScope.launch {
            libraryRepo.upsertBookProgress(
                bookId = book.id,
                bookTitle = book.title,
                categoryId = book.category.name,
                currentPage = clamped,
                totalPages = maxPage,
                isBookmarked = existing?.isBookmarked ?: false,
                bookmarkPage = (existing?.bookmarkPage ?: 0L).toInt(),
                isCompleted = nowCompleted,
                status = if (nowCompleted) "completed" else "reading"
            )
            if (clamped > prevPage) {
                val newlyCompletedDelta = if (!wasCompleted && nowCompleted) 1L else 0L
                libraryRepo.incrementTotals(pagesDelta = 1L, completedDelta = newlyCompletedDelta)
                libraryRepo.logHistory(
                    bookId = book.id,
                    bookTitle = book.title,
                    categoryName = book.category.displayName,
                    levelName = book.level.displayName,
                    action = if (!wasCompleted && nowCompleted) "completed" else "turned_page",
                    pageNumber = clamped
                )
            }
        }
    }

    fun toggleBookmark(book: Book, pageIndex: Int) {
        val existing = bookProgressList.value.firstOrNull { it.bookId == book.id }
        val currentlyBookmarkedHere = existing?.isBookmarked == true && existing.bookmarkPage == pageIndex.toLong()
        val shouldBookmark = !currentlyBookmarkedHere
        viewModelScope.launch {
            libraryRepo.upsertBookProgress(
                bookId = book.id,
                bookTitle = book.title,
                categoryId = book.category.name,
                currentPage = (existing?.currentPage ?: pageIndex.toLong()).toInt(),
                totalPages = book.pages.size,
                isBookmarked = shouldBookmark,
                bookmarkPage = if (shouldBookmark) pageIndex else (existing?.bookmarkPage ?: 0L).toInt(),
                isCompleted = existing?.isCompleted ?: false,
                status = existing?.status ?: "reading"
            )
        }
    }

    fun addMarginNote(book: Book, pageNumber: Int, text: String) {
        viewModelScope.launch {
            libraryRepo.addMarginNote(book.id, book.title, pageNumber, text)
        }
    }

    fun deleteMarginNote(noteId: String) {
        viewModelScope.launch {
            libraryRepo.deleteMarginNote(noteId)
        }
    }

    fun toggleLampPower(enabled: Boolean? = null) {
        val nextOn = enabled ?: !_gameState.value.isLampOn
        val readyProfile = (profileState.value as? ProfileLoadState.Ready)?.profile
        val ambience = LampAmbience.entries.firstOrNull { it.id == readyProfile?.lampAmbienceId }
            ?: LampAmbience.GOLDEN_STUDY
        _gameState.update {
            it.copy(
                isLampOn = nextOn,
                librarianWhisper = if (nextOn) {
                    "DESK LAMP SWITCHED ON (${ambience.label}) — ENJOY THE COZY LIBRARY GLOW."
                } else {
                    "DESK LAMP DIMMED TO QUIET TWILIGHT MODE."
                }
            )
        }
    }

    fun cycleLampAmbience() {
        val readyProfile = (profileState.value as? ProfileLoadState.Ready)?.profile
        val currentId = readyProfile?.lampAmbienceId ?: LampAmbience.GOLDEN_STUDY.id
        val modes = LampAmbience.entries
        val nextIndex = (modes.indexOfFirst { it.id == currentId } + 1) % modes.size
        val next = modes[nextIndex]
        val brightness = _gameState.value.lampBrightnessPreview
            ?: readyProfile?.lampBrightness?.toFloat()
            ?: 0.85f
        _gameState.update { it.copy(isLampOn = true) }
        viewModelScope.launch {
            libraryRepo.updateLampSettings(next.id, brightness)
        }
    }

    fun setLampAmbience(ambience: LampAmbience) {
        val readyProfile = (profileState.value as? ProfileLoadState.Ready)?.profile
        val brightness = _gameState.value.lampBrightnessPreview
            ?: readyProfile?.lampBrightness?.toFloat()
            ?: 0.85f
        _gameState.update {
            it.copy(
                isLampOn = true,
                librarianWhisper = "LIBRARY ATMOSPHERE SET TO ${ambience.label}."
            )
        }
        viewModelScope.launch {
            libraryRepo.updateLampSettings(ambience.id, brightness)
        }
    }

    fun previewLampBrightness(brightness: Float) {
        _gameState.update {
            it.copy(
                isLampOn = true,
                lampBrightnessPreview = brightness.coerceIn(0.25f, 1.0f)
            )
        }
    }

    fun commitLampBrightness(brightness: Float? = null) {
        val readyProfile = (profileState.value as? ProfileLoadState.Ready)?.profile
        val currentId = readyProfile?.lampAmbienceId ?: LampAmbience.GOLDEN_STUDY.id
        val target = (brightness ?: _gameState.value.lampBrightnessPreview ?: readyProfile?.lampBrightness?.toFloat() ?: 0.85f)
            .coerceIn(0.25f, 1.0f)
        _gameState.update { it.copy(isLampOn = true, lampBrightnessPreview = null) }
        viewModelScope.launch {
            libraryRepo.updateLampSettings(currentId, target)
        }
    }

    fun setTableCover(style: TableCoverStyle) {
        prefs?.edit()?.putString("table_cover_id", style.id)?.apply()
        _gameState.update {
            it.copy(
                tableCover = style,
                librarianWhisper = "STUDY TABLES DRESSED IN ${style.label} COVER."
            )
        }
    }

    fun addProject(title: String, categoryName: String, goalNote: String) {
        val cleanTitle = title.trim().uppercase().take(60)
        if (cleanTitle.isBlank()) return
        val newItem = LibraryProject(
            id = UUID.randomUUID().toString(),
            title = cleanTitle,
            categoryName = categoryName.trim().uppercase().ifBlank { "SCIENCE" },
            goalNote = goalNote.trim().uppercase().take(160),
            isCompleted = false
        )
        _gameState.update { state ->
            val updated = listOf(newItem) + state.projects
            prefs?.edit()?.putString("projects_json", LibraryProject.listToJsonString(updated))?.apply()
            state.copy(
                projects = updated,
                statusToast = "PROJECT SAVED IN LIBRARY CENTRE."
            )
        }
    }

    fun toggleProjectDone(projectId: String) {
        _gameState.update { state ->
            val updated = state.projects.map { p ->
                if (p.id == projectId) p.copy(isCompleted = !p.isCompleted) else p
            }
            prefs?.edit()?.putString("projects_json", LibraryProject.listToJsonString(updated))?.apply()
            state.copy(projects = updated)
        }
    }

    fun deleteProject(projectId: String) {
        _gameState.update { state ->
            val updated = state.projects.filterNot { it.id == projectId }
            prefs?.edit()?.putString("projects_json", LibraryProject.listToJsonString(updated))?.apply()
            state.copy(projects = updated)
        }
    }

    fun saveArtwork(title: String, strokes: List<ArtworkStroke>) {
        if (strokes.isEmpty()) return
        val cleanTitle = title.trim().uppercase().ifBlank { "LIBRARY SKETCH" }.take(50)
        val newArt = SavedArtwork(
            id = UUID.randomUUID().toString(),
            title = cleanTitle,
            strokes = strokes
        )
        _gameState.update { state ->
            val updated = listOf(newArt) + state.savedArtworks
            prefs?.edit()?.putString("artworks_json", SavedArtwork.listToJsonString(updated))?.apply()
            state.copy(
                savedArtworks = updated,
                statusToast = "ARTWORK SAVED IN LIBRARY CENTRE."
            )
        }
    }

    fun deleteArtwork(artworkId: String) {
        _gameState.update { state ->
            val updated = state.savedArtworks.filterNot { it.id == artworkId }
            prefs?.edit()?.putString("artworks_json", SavedArtwork.listToJsonString(updated))?.apply()
            state.copy(savedArtworks = updated)
        }
    }

    fun saveCustomModelTemplate(template: UserScienceTemplate) {
        _gameState.update { state ->
            val filtered = state.customModelTemplates.filterNot { it.id == template.id }
            val updated = listOf(template) + filtered
            prefs?.edit()?.putString(
                "custom_templates_json",
                UserScienceTemplate.listToJsonString(updated)
            )?.apply()
            state.copy(
                customModelTemplates = updated,
                statusToast = "CUSTOM MODEL TEMPLATE SAVED IN LIBRARY CENTRE."
            )
        }
    }

    fun deleteCustomModelTemplate(templateId: String) {
        _gameState.update { state ->
            val updated = state.customModelTemplates.filterNot { it.id == templateId }
            prefs?.edit()?.putString(
                "custom_templates_json",
                UserScienceTemplate.listToJsonString(updated)
            )?.apply()
            state.copy(customModelTemplates = updated)
        }
    }

    fun toggleFocusTimer() {
        if (_gameState.value.isFocusTimerRunning) {
            pauseFocusTimer()
        } else {
            startFocusTimer()
        }
    }

    private fun startFocusTimer() {
        timerJob?.cancel()
        _gameState.update { it.copy(isFocusTimerRunning = true) }
        timerJob = viewModelScope.launch {
            while (_gameState.value.isFocusTimerRunning && _gameState.value.focusSecondsRemaining > 0) {
                delay(1000L)
                elapsedStudySeconds++
                if (elapsedStudySeconds >= 60) {
                    elapsedStudySeconds = 0
                    libraryRepo.incrementTotals(minutesDelta = 1L)
                }
                _gameState.update {
                    val next = (it.focusSecondsRemaining - 1).coerceAtLeast(0)
                    it.copy(
                        focusSecondsRemaining = next,
                        isFocusTimerRunning = next > 0
                    )
                }
            }
        }
    }

    private fun pauseFocusTimer() {
        timerJob?.cancel()
        _gameState.update { it.copy(isFocusTimerRunning = false) }
    }

    private fun nearestTable(x: Float, y: Float) =
        LibraryFloorLayout.studyTables.minBy { hypot(it.normX - x, it.normY - y) }

    private fun findNearbyShelf(x: Float, y: Float): Int? {
        return LibraryFloorLayout.bookshelves
            .filter { shelf ->
                val nearX = if (shelf.isLeftRow) x <= 0.295f else x >= 0.705f
                nearX && abs(y - shelf.normY) <= 0.064f
            }
            .minByOrNull { abs(y - it.normY) }
            ?.shelfNumber
    }

    private fun findNearbyTable(x: Float, y: Float): String? {
        return LibraryFloorLayout.studyTables
            .filter { table -> hypot(x - table.normX, y - (table.normY + 0.04f)) <= 0.105f }
            .minByOrNull { hypot(x - it.normX, y - (it.normY + 0.04f)) }
            ?.tableId
    }

    private fun isNearLibrarianCentre(x: Float, y: Float): Boolean {
        val (lx, _) = LibraryFloorLayout.librarianSpot
        return abs(x - lx) <= 0.17f && y <= 0.295f
    }
}
