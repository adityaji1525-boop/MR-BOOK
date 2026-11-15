package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Chair
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.LibraryCatalog
import com.example.data.LibraryFloorLayout
import com.example.model.BookCategory
import com.example.model.CameraMode
import com.example.model.CharacterAvatar
import com.example.model.LampAmbience
import com.example.model.ScholarRank
import com.example.ui.components.DeskLampControl
import com.example.ui.components.LibraryHallCanvas
import com.example.ui.components.MrBookLogoBadge
import com.example.ui.components.StudyTableDeskPov
import com.example.ui.theme.BrassBright
import com.example.ui.theme.BrassGold
import com.example.ui.theme.BurgundyLeather
import com.example.ui.theme.EmeraldLibrarian
import com.example.ui.theme.MahoganyDark
import com.example.ui.theme.MahoganyRich
import com.example.ui.theme.ParchmentCream
import com.example.ui.theme.ParchmentSoft
import com.example.ui.theme.PlayfairDisplayFontFamily
import com.example.ui.theme.PlusJakartaSansFontFamily
import com.example.ui.viewmodel.ActiveHallOverlay
import com.example.ui.viewmodel.LibraryViewModel
import com.example.ui.viewmodel.ProfileLoadState

@Composable
fun LibraryRootScreen(
    viewModel: LibraryViewModel
) {
    val profileState by viewModel.profileState.collectAsStateWithLifecycle()
    val bookProgressList by viewModel.bookProgressList.collectAsStateWithLifecycle()
    val readingHistory by viewModel.readingHistory.collectAsStateWithLifecycle()
    val allNotes by viewModel.allNotes.collectAsStateWithLifecycle()
    val downloadedSections by viewModel.downloadedSections.collectAsStateWithLifecycle()
    val activeDownloads by viewModel.activeDownloads.collectAsStateWithLifecycle()
    val liveBookTopicState by viewModel.liveBookTopicState.collectAsStateWithLifecycle()
    val liveGlobalSearchState by viewModel.liveGlobalSearchState.collectAsStateWithLifecycle()
    val autoLiveGoogleSync by viewModel.autoLiveGoogleSync.collectAsStateWithLifecycle()
    val gameState by viewModel.gameState.collectAsStateWithLifecycle()

    when (val state = profileState) {
        ProfileLoadState.Loading -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(MahoganyDark)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    MrBookLogoBadge(size = 76.dp, isCircular = true, tag = "loading_app_logo")
                    Spacer(Modifier.height(14.dp))
                    CircularProgressIndicator(color = BrassBright)
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = "OPENING YOUR MR BOOK LEDGER...",
                        fontFamily = PlayfairDisplayFontFamily,
                        fontSize = 17.sp,
                        color = ParchmentCream
                    )
                }
            }
            return
        }

        is ProfileLoadState.Error -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(MahoganyDark)
                    .padding(24.dp)
            ) {
                Surface(
                    color = ParchmentCream,
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "COULD NOT LOAD LIBRARY PROFILE",
                            fontFamily = PlayfairDisplayFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = BurgundyLeather
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = state.message.uppercase(),
                            fontFamily = PlusJakartaSansFontFamily,
                            fontSize = 13.sp,
                            color = MahoganyDark
                        )
                    }
                }
            }
            return
        }

        is ProfileLoadState.Ready -> {
            val currentProfile = state.profile
            if (currentProfile == null || currentProfile.characterName.isBlank()) {
                CharacterSetupScreen(
                    initialName = "",
                    initialAvatarId = CharacterAvatar.SCHOLAR.id,
                    initialCloakIndex = 0,
                    isEditingExisting = false,
                    onConfirmCharacter = { name, avatarId, cloakIndex ->
                        viewModel.saveCharacter(name, avatarId, cloakIndex)
                    }
                )
                return
            }

            val avatar = CharacterAvatar.entries.firstOrNull { it.id == currentProfile.avatarId }
                ?: CharacterAvatar.SCHOLAR
            val cloakColor = LibraryFloorLayout.cloakColors
                .getOrElse(currentProfile.cloakColorIndex.toInt()) { LibraryFloorLayout.cloakColors.first() }
                .second
            val lampAmbience = LampAmbience.entries.firstOrNull { it.id == currentProfile.lampAmbienceId }
                ?: LampAmbience.GOLDEN_STUDY
            val effectiveBrightness = (gameState.lampBrightnessPreview ?: currentProfile.lampBrightness.toFloat())
                .coerceIn(0.25f, 1.0f)

            val borrowedStack = bookProgressList.filter { it.status == "borrowed" || it.status == "reading" }
            val activeBook = gameState.activeBookId?.let { LibraryCatalog.findById(it) }
                ?: borrowedStack.firstOrNull()?.let { LibraryCatalog.findById(it.bookId) }
            val offlineCount = BookCategory.entries.count { cat ->
                downloadedSections.any { it.categoryId == cat.name && it.isDownloaded }
            }

            AnimatedContent(
                targetState = gameState.cameraMode,
                transitionSpec = {
                    (fadeIn() + scaleIn(initialScale = 0.96f)) togetherWith
                        (fadeOut() + scaleOut(targetScale = 1.04f))
                },
                label = "camera_angle_transition"
            ) { mode ->
                when (mode) {
                    CameraMode.TABLE_DESK_POV if activeBook != null -> {
                        val bookNotes = allNotes.filter { it.bookId == activeBook.id }
                        StudyTableDeskPov(
                            characterName = currentProfile.characterName,
                            activeBook = activeBook,
                            deskStackBooks = borrowedStack,
                            notesForBook = bookNotes,
                            isLampOn = gameState.isLampOn,
                            lampAmbience = lampAmbience,
                            lampBrightness = effectiveBrightness,
                            tableCover = gameState.tableCover,
                            focusSecondsRemaining = gameState.focusSecondsRemaining,
                            isFocusTimerRunning = gameState.isFocusTimerRunning,
                            liveTopicState = liveBookTopicState,
                            autoLiveGoogleSync = autoLiveGoogleSync,
                            onToggleAutoLiveSync = { enabled -> viewModel.setAutoLiveGoogleSync(enabled) },
                            onFetchLiveTopic = { topic, context, customQ, force ->
                                viewModel.loadLiveBookTopic(
                                    book = activeBook,
                                    topicTitle = topic,
                                    chapterContext = context,
                                    customQuestion = customQ,
                                    forceRefresh = force
                                )
                            },
                            onSelectBook = { bookId -> viewModel.openBookAtTable(bookId) },
                            onPageChanged = { newPage -> viewModel.turnToPage(activeBook, newPage) },
                            onToggleBookmark = { page -> viewModel.toggleBookmark(activeBook, page) },
                            onToggleLamp = { enabled -> viewModel.toggleLampPower(enabled) },
                            onToggleTimer = { viewModel.toggleFocusTimer() },
                            onAddNote = { page, text -> viewModel.addMarginNote(activeBook, page, text) },
                            onDeleteNote = { noteId -> viewModel.deleteMarginNote(noteId) },
                            onStandUp = { viewModel.standUpFromTable() }
                        )
                    }

                    else -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MahoganyDark)
                        ) {
                            LibraryHallCanvas(
                                characterName = currentProfile.characterName,
                                avatar = avatar,
                                cloakColor = cloakColor,
                                charX = gameState.charX,
                                charY = gameState.charY,
                                targetX = gameState.targetX,
                                targetY = gameState.targetY,
                                isWalking = gameState.isWalking,
                                facingRight = gameState.facingRight,
                                walkPhase = gameState.walkPhase,
                                carriedBooksCount = borrowedStack.size,
                                seatedTableId = gameState.isSeatedAtTableId,
                                highlightedShelfNumber = gameState.highlightedShelfNumber,
                                nearbyShelfNumber = gameState.nearbyShelfNumber,
                                nearbyLibrarian = gameState.nearbyLibrarian,
                                isLampOn = gameState.isLampOn,
                                lampAmbience = lampAmbience,
                                lampBrightness = effectiveBrightness,
                                tableCover = gameState.tableCover,
                                onWalkToFloor = { x, y -> viewModel.walkToPoint(x, y) },
                                onTapShelf = { shelfNum -> viewModel.walkToShelf(shelfNum) },
                                onTapTable = { tableId -> viewModel.walkToTableAndSit(tableId = tableId) },
                                onTapLibrarian = { viewModel.walkToLibrarian() },
                                onJoystickMove = { dx, dy -> viewModel.onJoystickInput(dx, dy) },
                                modifier = Modifier.fillMaxSize()
                            )

                            // Top Game HUD
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .statusBarsPadding()
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    // Library Card / MR BOOK Emblem + Character & Status Pill
                                    Surface(
                                        color = MahoganyDark.copy(alpha = 0.90f),
                                        shape = RoundedCornerShape(14.dp),
                                        modifier = Modifier
                                            .weight(1f)
                                            .border(1.dp, BrassGold.copy(alpha = 0.7f), RoundedCornerShape(14.dp))
                                            .clickable { viewModel.openOverlay(ActiveHallOverlay.LibraryCard) }
                                            .testTag("hud_library_card_button")
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                                        ) {
                                            MrBookLogoBadge(size = 34.dp, isCircular = true, tag = "hud_app_logo")
                                            Spacer(Modifier.width(7.dp))
                                            Column {
                                                Text(
                                                    text = currentProfile.characterName.uppercase(),
                                                    fontFamily = PlayfairDisplayFontFamily,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 13.5.sp,
                                                    color = ParchmentCream,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                Text(
                                                    text = "${ScholarRank.fromPages(currentProfile.totalPagesRead.toInt()).title} • ${currentProfile.statusText.uppercase()}",
                                                    fontFamily = PlusJakartaSansFontFamily,
                                                    fontSize = 9.5.sp,
                                                    color = BrassBright,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }
                                        }
                                    }

                                    // Offline Section Packs Button (20-80 MB)
                                    Surface(
                                        color = MahoganyRich,
                                        shape = RoundedCornerShape(14.dp),
                                        modifier = Modifier
                                            .border(1.dp, BrassGold.copy(alpha = 0.7f), RoundedCornerShape(14.dp))
                                            .clickable { viewModel.openOverlay(ActiveHallOverlay.SectionDownloads) }
                                            .testTag("hud_offline_packs_button")
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp)
                                        ) {
                                            Icon(Icons.Default.CloudDownload, contentDescription = "Offline section packs", tint = BrassBright, modifier = Modifier.size(16.dp))
                                            Spacer(Modifier.width(4.dp))
                                            Text(
                                                text = "$offlineCount/7",
                                                fontFamily = PlusJakartaSansFontFamily,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.sp,
                                                color = ParchmentCream
                                            )
                                        }
                                    }

                                    // Search Books Button
                                    Surface(
                                        color = EmeraldLibrarian,
                                        shape = RoundedCornerShape(14.dp),
                                        modifier = Modifier
                                            .border(1.dp, BrassGold.copy(alpha = 0.7f), RoundedCornerShape(14.dp))
                                            .clickable { viewModel.openOverlay(ActiveHallOverlay.CatalogSearch) }
                                            .testTag("hud_catalog_search_button")
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Search,
                                                contentDescription = "Search books",
                                                tint = Color.White,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(Modifier.width(4.dp))
                                            Text(
                                                text = "SEARCH",
                                                fontFamily = PlusJakartaSansFontFamily,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.sp,
                                                color = Color.White
                                            )
                                        }
                                    }

                                    // App Settings Button (Login, Log Out, Theme, Table Cover, Help)
                                    Surface(
                                        color = BurgundyLeather,
                                        shape = RoundedCornerShape(14.dp),
                                        modifier = Modifier
                                            .border(1.dp, BrassGold, RoundedCornerShape(14.dp))
                                            .clickable { viewModel.openOverlay(ActiveHallOverlay.AppSettings) }
                                            .testTag("hud_app_settings_button")
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Settings,
                                                contentDescription = "Open app settings",
                                                tint = ParchmentCream,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(Modifier.width(4.dp))
                                            Text(
                                                text = "SETTINGS",
                                                fontFamily = PlusJakartaSansFontFamily,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.sp,
                                                color = ParchmentCream
                                            )
                                        }
                                    }
                                }

                                Spacer(Modifier.height(6.dp))

                                // Desk Lamp Toggle Switch (Theme & Table Cover options live exclusively in App Settings)
                                DeskLampControl(
                                    isLampOn = gameState.isLampOn,
                                    lampAmbience = lampAmbience,
                                    lampBrightness = effectiveBrightness,
                                    onToggleLamp = { enabled -> viewModel.toggleLampPower(enabled) }
                                )
                            }

                            // Bottom-Left Quick Action Bar + Reached-Shelf Prompt + Librarian Guide Strip
                            Column(
                                verticalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .navigationBarsPadding()
                                    .padding(start = 12.dp, bottom = 14.dp, end = 140.dp)
                            ) {
                                val nearbyShelf = gameState.nearbyShelfNumber?.let { num ->
                                    LibraryFloorLayout.bookshelves.firstOrNull { it.shelfNumber == num }
                                }
                                if (nearbyShelf != null && gameState.activeOverlay == ActiveHallOverlay.None) {
                                    Button(
                                        onClick = {
                                            viewModel.openOverlay(ActiveHallOverlay.ShelfBrowser(nearbyShelf.shelfNumber))
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldLibrarian),
                                        shape = RoundedCornerShape(14.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(48.dp)
                                            .border(1.5.dp, BrassBright, RoundedCornerShape(14.dp))
                                            .testTag("reached_shelf_prompt")
                                    ) {
                                        Icon(
                                            Icons.AutoMirrored.Filled.MenuBook,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(Modifier.width(6.dp))
                                        Text(
                                            text = "SHELF #${nearbyShelf.shelfNumber} (${nearbyShelf.category.displayName}) • SELECT BOOK",
                                            fontFamily = PlusJakartaSansFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.5.sp,
                                            color = Color.White,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }

                                if (gameState.nearbyLibrarian &&
                                    nearbyShelf == null &&
                                    gameState.activeOverlay == ActiveHallOverlay.None
                                ) {
                                    Button(
                                        onClick = { viewModel.openOverlay(ActiveHallOverlay.LibrarianDesk) },
                                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldLibrarian),
                                        shape = RoundedCornerShape(14.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(48.dp)
                                            .border(1.5.dp, BrassBright, RoundedCornerShape(14.dp))
                                            .testTag("reached_librarian_prompt")
                                    ) {
                                        Icon(
                                            Icons.Default.Badge,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(Modifier.width(6.dp))
                                        Text(
                                            text = "LIBRARY CENTRE • PROJECTS, ARTWORK & BOOKS",
                                            fontFamily = PlusJakartaSansFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.5.sp,
                                            color = Color.White,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }

                                val nearbyTable = gameState.nearbyTableId?.let { tid ->
                                    LibraryFloorLayout.studyTables.firstOrNull { it.tableId == tid }
                                }
                                if (nearbyTable != null && borrowedStack.isNotEmpty() && gameState.activeOverlay == ActiveHallOverlay.None) {
                                    Button(
                                        onClick = { viewModel.walkToTableAndSit(tableId = nearbyTable.tableId) },
                                        colors = ButtonDefaults.buttonColors(containerColor = BurgundyLeather),
                                        shape = RoundedCornerShape(14.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(48.dp)
                                            .border(1.5.dp, BrassGold, RoundedCornerShape(14.dp))
                                            .testTag("reached_table_prompt")
                                    ) {
                                        Icon(
                                            Icons.Default.Chair,
                                            contentDescription = null,
                                            tint = ParchmentCream,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(Modifier.width(6.dp))
                                        Text(
                                            text = "REACHED ${nearbyTable.label} • SIT & READ BOOK",
                                            fontFamily = PlusJakartaSansFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.5.sp,
                                            color = ParchmentCream,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }

                                Surface(
                                    color = ParchmentSoft.copy(alpha = 0.92f),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(1.dp, BrassGold.copy(alpha = 0.55f), RoundedCornerShape(10.dp))
                                ) {
                                    Text(
                                        text = gameState.librarianWhisper.uppercase(),
                                        fontFamily = PlusJakartaSansFontFamily,
                                        fontSize = 10.5.sp,
                                        color = MahoganyDark,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                    )
                                }

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                Surface(
                                    color = MahoganyRich.copy(alpha = 0.92f),
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier
                                        .border(1.dp, BrassGold.copy(alpha = 0.65f), RoundedCornerShape(14.dp))
                                        .clickable { viewModel.openOverlay(ActiveHallOverlay.LibrarianDesk) }
                                        .testTag("hud_carried_stack_button")
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)
                                    ) {
                                        Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = null, tint = BrassBright, modifier = Modifier.size(18.dp))
                                        Spacer(Modifier.width(5.dp))
                                        Text(
                                            text = "STACK: ${borrowedStack.size}/4",
                                            fontFamily = PlusJakartaSansFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.5.sp,
                                            color = ParchmentCream
                                        )
                                    }
                                }

                                if (borrowedStack.isNotEmpty()) {
                                    Button(
                                        onClick = { viewModel.walkToTableAndSit() },
                                        colors = ButtonDefaults.buttonColors(containerColor = BurgundyLeather),
                                        shape = RoundedCornerShape(14.dp),
                                        modifier = Modifier
                                            .height(42.dp)
                                            .border(1.dp, BrassGold, RoundedCornerShape(14.dp))
                                            .testTag("sit_and_read_button")
                                    ) {
                                        Icon(Icons.Default.Chair, contentDescription = null, tint = ParchmentCream, modifier = Modifier.size(16.dp))
                                        Spacer(Modifier.width(5.dp))
                                        Text(
                                            text = "SIT & READ",
                                            fontFamily = PlusJakartaSansFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.5.sp,
                                            color = ParchmentCream
                                        )
                                    }
                                }
                                }
                            }

                            // Transient Toast Message
                            val toast = gameState.statusToast
                            if (toast != null) {
                                Snackbar(
                                    action = {
                                        TextButton(onClick = { viewModel.clearStatusToast() }) {
                                            Text("OK", color = BrassBright)
                                        }
                                    },
                                    containerColor = MahoganyDark,
                                    contentColor = ParchmentCream,
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .navigationBarsPadding()
                                        .padding(horizontal = 16.dp, vertical = 72.dp)
                                ) {
                                    Text(toast.uppercase(), fontSize = 11.5.sp)
                                }
                            }

                            // Active Modal Overlays
                            when (val overlay = gameState.activeOverlay) {
                                ActiveHallOverlay.None -> Unit

                                is ActiveHallOverlay.ShelfBrowser -> {
                                    ShelfBrowserSheet(
                                        shelfNumber = overlay.shelfNumber,
                                        bookProgressList = bookProgressList,
                                        downloadedSections = downloadedSections,
                                        activeDownloads = activeDownloads,
                                        onDownloadSection = { category -> viewModel.downloadSection(category) },
                                        onTakeBookOnly = { book ->
                                            viewModel.borrowBook(book, sitImmediately = false)
                                        },
                                        onTakeAndSitAtTable = { book ->
                                            viewModel.borrowBook(book, sitImmediately = true)
                                        },
                                        onClose = { viewModel.closeOverlay() }
                                    )
                                }

                                ActiveHallOverlay.HelpMap -> {
                                    HelpMapSheet(
                                        characterName = currentProfile.characterName,
                                        highlightedShelfNumber = gameState.highlightedShelfNumber,
                                        onGoToShelf = { shelfNum -> viewModel.walkToShelf(shelfNum) },
                                        onGoToLibrarian = {
                                            viewModel.closeOverlay()
                                            viewModel.walkToLibrarian()
                                        },
                                        onGoToTable = { tableId -> viewModel.walkToTableAndSit(tableId = tableId) },
                                        onClose = { viewModel.closeOverlay() }
                                    )
                                }

                                ActiveHallOverlay.LibrarianDesk,
                                ActiveHallOverlay.CatalogSearch -> {
                                    LibrarianDeskSheet(
                                        characterName = currentProfile.characterName,
                                        bookProgressList = bookProgressList,
                                        downloadedSections = downloadedSections,
                                        projects = gameState.projects,
                                        savedArtworks = gameState.savedArtworks,
                                        customModelTemplates = gameState.customModelTemplates,
                                        startInSearchTab = overlay == ActiveHallOverlay.CatalogSearch,
                                        liveGlobalSearchState = liveGlobalSearchState,
                                        onSearchRealWorldLive = { query ->
                                            viewModel.searchRealWorldBookTopicLive(query)
                                        },
                                        onClearLiveGlobalSearch = {
                                            viewModel.clearLiveGlobalSearch()
                                        },
                                        onOpenOfflinePacks = { viewModel.openOverlay(ActiveHallOverlay.SectionDownloads) },
                                        onOpenLibraryCard = { viewModel.openOverlay(ActiveHallOverlay.LibraryCard) },
                                        onOpenCharacterEditor = { viewModel.openOverlay(ActiveHallOverlay.CharacterEditor) },
                                        onWalkToTable = { tableId -> viewModel.walkToTableAndSit(tableId = tableId) },
                                        onWalkToShelf = { shelfNum -> viewModel.walkToShelf(shelfNum) },
                                        onTakeAndRead = { book ->
                                            viewModel.borrowBook(book, sitImmediately = true)
                                        },
                                        onReturnBook = { bookId -> viewModel.returnBook(bookId) },
                                        onAddProject = { title, cat, goal ->
                                            viewModel.addProject(title, cat, goal)
                                        },
                                        onToggleProjectDone = { id -> viewModel.toggleProjectDone(id) },
                                        onDeleteProject = { id -> viewModel.deleteProject(id) },
                                        onSaveArtwork = { title, strokes ->
                                            viewModel.saveArtwork(title, strokes)
                                        },
                                        onDeleteArtwork = { id -> viewModel.deleteArtwork(id) },
                                        onSaveCustomTemplate = { tmpl ->
                                            viewModel.saveCustomModelTemplate(tmpl)
                                        },
                                        onDeleteCustomTemplate = { id ->
                                            viewModel.deleteCustomModelTemplate(id)
                                        },
                                        onClose = { viewModel.closeOverlay() }
                                    )
                                }

                                ActiveHallOverlay.AppSettings -> {
                                    AppSettingsSheet(
                                        characterName = currentProfile.characterName,
                                        isLampOn = gameState.isLampOn,
                                        lampAmbience = lampAmbience,
                                        lampBrightness = effectiveBrightness,
                                        tableCover = gameState.tableCover,
                                        onToggleLamp = { enabled -> viewModel.toggleLampPower(enabled) },
                                        onSelectLampAmbience = { mode -> viewModel.setLampAmbience(mode) },
                                        onSetLampBrightness = { value -> viewModel.previewLampBrightness(value) },
                                        onBrightnessChangeFinished = { viewModel.commitLampBrightness() },
                                        onSelectTableCover = { style -> viewModel.setTableCover(style) },
                                        onOpenHelpMap = { viewModel.openOverlay(ActiveHallOverlay.HelpMap) },
                                        onClose = { viewModel.closeOverlay() }
                                    )
                                }

                                ActiveHallOverlay.SectionDownloads -> {
                                    SectionDownloadsSheet(
                                        downloadedSections = downloadedSections,
                                        activeDownloads = activeDownloads,
                                        onDownloadSection = { cat -> viewModel.downloadSection(cat) },
                                        onRemoveSection = { cat -> viewModel.removeSection(cat) },
                                        onDownloadAll = { viewModel.downloadAllSections() },
                                        onClose = { viewModel.closeOverlay() }
                                    )
                                }

                                ActiveHallOverlay.LibraryCard -> {
                                    LibraryCardDialog(
                                        profile = currentProfile,
                                        avatar = avatar,
                                        cloakColor = cloakColor,
                                        bookProgressList = bookProgressList,
                                        readingHistory = readingHistory,
                                        allNotes = allNotes,
                                        onUpdateStatus = { newStatus -> viewModel.updateCustomStatus(newStatus) },
                                        onEditCharacter = { viewModel.openOverlay(ActiveHallOverlay.CharacterEditor) },
                                        onOpenBook = { bookId -> viewModel.openBookAtTable(bookId) },
                                        onClose = { viewModel.closeOverlay() }
                                    )
                                }

                                ActiveHallOverlay.CharacterEditor -> {
                                    CharacterSetupScreen(
                                        initialName = currentProfile.characterName,
                                        initialAvatarId = currentProfile.avatarId,
                                        initialCloakIndex = currentProfile.cloakColorIndex.toInt(),
                                        isEditingExisting = true,
                                        onConfirmCharacter = { name, avatarId, cloakIndex ->
                                            viewModel.saveCharacter(name, avatarId, cloakIndex)
                                        },
                                        onCancelEdit = { viewModel.closeOverlay() }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
