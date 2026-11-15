package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Chair
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.OfflinePin
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ai.LiveKnowledgeUiState
import com.example.data.BookProgressDoc
import com.example.data.DownloadedSectionEntity
import com.example.data.LibraryCatalog
import com.example.data.LibraryFloorLayout
import com.example.model.ArtworkStroke
import com.example.model.Book
import com.example.model.BookCategory
import com.example.model.EducationLevel
import com.example.model.LibraryProject
import com.example.model.SavedArtwork
import com.example.model.UserScienceTemplate
import com.example.ui.components.LiveGoogleKnowledgeCard
import com.example.ui.components.ProjectsAndArtworkStudio
import com.example.ui.theme.BrassGold
import com.example.ui.theme.BurgundyLeather
import com.example.ui.theme.EmeraldLibrarian
import com.example.ui.theme.InkCharcoal
import com.example.ui.theme.InkMuted
import com.example.ui.theme.MahoganyDark
import com.example.ui.theme.ParchmentAged
import com.example.ui.theme.ParchmentCream
import com.example.ui.theme.ParchmentSoft
import com.example.ui.theme.PlayfairDisplayFontFamily
import com.example.ui.theme.PlusJakartaSansFontFamily

@Composable
fun LibrarianDeskSheet(
    characterName: String,
    bookProgressList: List<BookProgressDoc>,
    downloadedSections: List<DownloadedSectionEntity>,
    projects: List<LibraryProject>,
    savedArtworks: List<SavedArtwork>,
    customModelTemplates: List<UserScienceTemplate>,
    startInSearchTab: Boolean,
    liveGlobalSearchState: LiveKnowledgeUiState,
    onSearchRealWorldLive: (String) -> Unit,
    onClearLiveGlobalSearch: () -> Unit,
    onOpenOfflinePacks: () -> Unit,
    onOpenLibraryCard: () -> Unit,
    onOpenCharacterEditor: () -> Unit,
    onWalkToTable: (String?) -> Unit,
    onWalkToShelf: (Int) -> Unit,
    onTakeAndRead: (Book) -> Unit,
    onReturnBook: (String) -> Unit,
    onAddProject: (String, String, String) -> Unit,
    onToggleProjectDone: (String) -> Unit,
    onDeleteProject: (String) -> Unit,
    onSaveArtwork: (String, List<ArtworkStroke>) -> Unit,
    onDeleteArtwork: (String) -> Unit,
    onSaveCustomTemplate: (UserScienceTemplate) -> Unit,
    onDeleteCustomTemplate: (String) -> Unit,
    onClose: () -> Unit
) {
    BackHandler { onClose() }

    val borrowedBooks = remember(bookProgressList) {
        bookProgressList.filter { it.status == "borrowed" || it.status == "reading" }
    }
    val offlineCategories = remember(downloadedSections) {
        downloadedSections.filter { it.isDownloaded }.map { it.categoryId }.toSet()
    }

    var activeTab by remember(startInSearchTab) { mutableIntStateOf(if (startInSearchTab) 1 else 0) }
    var selectedLevel by remember { mutableStateOf<EducationLevel?>(null) }
    var selectedCategory by remember { mutableStateOf<BookCategory?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    val results = remember(searchQuery, selectedCategory, selectedLevel) {
        LibraryCatalog.searchBooks(searchQuery, selectedCategory, selectedLevel)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x99160D09))
            .clickable { onClose() }
            .padding(14.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            color = ParchmentCream,
            shape = RoundedCornerShape(22.dp),
            shadowElevation = 14.dp,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 650.dp)
                .border(2.dp, EmeraldLibrarian, RoundedCornerShape(22.dp))
                .clickable(enabled = false) {}
                .testTag("librarian_desk_sheet")
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Librarian Eleanor Header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Image(
                            painter = painterResource(id = R.drawable.img_librarian_portrait),
                            contentDescription = "Librarian Eleanor Vance",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .border(2.dp, BrassGold, CircleShape)
                        )
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "LIBRARY CENTRE • ELEANOR",
                                fontFamily = PlayfairDisplayFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.5.sp,
                                color = EmeraldLibrarian
                            )
                            Text(
                                text = "HEAD ORGANIZER • PROJECTS, ARTWORK & BOOKS",
                                fontFamily = PlusJakartaSansFontFamily,
                                fontSize = 10.5.sp,
                                color = InkMuted
                            )
                        }
                    }
                    IconButton(onClick = onClose, modifier = Modifier.testTag("close_librarian_desk")) {
                        Icon(Icons.Default.Close, contentDescription = "Close librarian desk", tint = InkCharcoal)
                    }
                }

                Spacer(Modifier.height(10.dp))

                // 3-Tab Switcher: All Options Hub | Search Books | Projects & Artwork
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
                    listOf(
                        "ALL OPTIONS",
                        "SEARCH BOOKS",
                        "PROJECTS & ART"
                    ).forEachIndexed { idx, label ->
                        val selected = activeTab == idx
                        Surface(
                            color = if (selected) EmeraldLibrarian else ParchmentSoft,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .border(1.dp, EmeraldLibrarian, RoundedCornerShape(10.dp))
                                .clickable { activeTab = idx }
                                .testTag("librarian_tab_$idx")
                        ) {
                            Text(
                                text = label,
                                fontFamily = PlusJakartaSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = if (selected) Color.White else EmeraldLibrarian,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 9.dp)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(10.dp))

                when (activeTab) {
                    0 -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Surface(
                                color = Color(0xFFE7F2ED),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "“WELCOME TO THE LIBRARY CENTRE, ${characterName.uppercase()}! CREATE PROJECTS & ARTWORK HERE, SEARCH BOOKS, DOWNLOAD 20–80 MB OFFLINE SECTIONS, OR VIEW YOUR PROFILE. FOR THEME, TABLE COVER & HELP, OPEN APP SETTINGS.”",
                                    fontFamily = PlusJakartaSansFontFamily,
                                    fontSize = 11.5.sp,
                                    lineHeight = 17.sp,
                                    color = MahoganyDark,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }

                            // Library Centre Options Grid (Projects & Artwork only here!)
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                                LibraryOptionCard(
                                    icon = Icons.Default.Palette,
                                    title = "PROJECTS & ARTWORK",
                                    subtitle = "${projects.size} PROJECTS • ${savedArtworks.size} ART",
                                    accent = BurgundyLeather,
                                    testTag = "centre_option_projects_artwork",
                                    onClick = { activeTab = 2 },
                                    modifier = Modifier.weight(1f)
                                )
                                LibraryOptionCard(
                                    icon = Icons.Default.Search,
                                    title = "SEARCH BOOKS",
                                    subtitle = "TITLE, AUTHOR OR TOPIC",
                                    accent = EmeraldLibrarian,
                                    testTag = "centre_option_catalog",
                                    onClick = { activeTab = 1 },
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                                LibraryOptionCard(
                                    icon = Icons.Default.OfflinePin,
                                    title = "OFFLINE PACKS",
                                    subtitle = "${offlineCategories.size}/7 READY (20–80 MB)",
                                    accent = EmeraldLibrarian,
                                    testTag = "librarian_offline_packs_button",
                                    onClick = onOpenOfflinePacks,
                                    modifier = Modifier.weight(1f)
                                )
                                LibraryOptionCard(
                                    icon = Icons.Default.Badge,
                                    title = "LIBRARY CARD",
                                    subtitle = "PROFILE, HISTORY & RANK",
                                    accent = Color(0xFF2B4C7E),
                                    testTag = "centre_option_library_card",
                                    onClick = onOpenLibraryCard,
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            LibraryOptionCard(
                                icon = Icons.Default.Person,
                                title = "EDIT CHARACTER",
                                subtitle = "CUSTOMIZE NAME, LOOK & CLOAK",
                                accent = Color(0xFF5D3A6A),
                                testTag = "centre_option_character",
                                onClick = onOpenCharacterEditor,
                                modifier = Modifier.fillMaxWidth()
                            )

                            // Quick Study Table Jump Row
                            Text(
                                text = "WALK TO A STUDY TABLE",
                                fontFamily = PlusJakartaSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = InkCharcoal
                            )
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(LibraryFloorLayout.studyTables, key = { it.tableId }) { table ->
                                    Surface(
                                        color = ParchmentSoft,
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier
                                            .border(1.dp, BrassGold, RoundedCornerShape(10.dp))
                                            .clickable { onWalkToTable(table.tableId) }
                                            .testTag("centre_table_${table.tableId}")
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                                        ) {
                                            Icon(
                                                Icons.Default.Chair,
                                                contentDescription = null,
                                                tint = BurgundyLeather,
                                                modifier = Modifier.size(15.dp)
                                            )
                                            Spacer(Modifier.width(5.dp))
                                            Text(
                                                text = table.label,
                                                fontFamily = PlusJakartaSansFontFamily,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.5.sp,
                                                color = MahoganyDark
                                            )
                                        }
                                    }
                                }
                            }

                            // Checked-out stack section
                            Text(
                                text = "YOUR CHECKED-OUT DESK STACK (${borrowedBooks.size}/4)",
                                fontFamily = PlusJakartaSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = InkCharcoal
                            )

                            if (borrowedBooks.isEmpty()) {
                                Surface(
                                    color = ParchmentSoft,
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(1.dp, ParchmentAged, RoundedCornerShape(12.dp))
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            text = "NO BOOKS IN YOUR STACK YET. SEARCH BOOKS OR TAKE A STARTER BOOK TO READ AT A TABLE!",
                                            fontFamily = PlusJakartaSansFontFamily,
                                            fontSize = 11.5.sp,
                                            color = InkMuted
                                        )
                                        Spacer(Modifier.height(8.dp))
                                        Button(
                                            onClick = { onTakeAndRead(LibraryCatalog.allBooks.first()) },
                                            colors = ButtonDefaults.buttonColors(containerColor = BurgundyLeather),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = null, tint = ParchmentCream, modifier = Modifier.size(16.dp))
                                            Spacer(Modifier.width(6.dp))
                                            Text("TAKE STARTER BOOK & WALK TO TABLE", color = ParchmentCream, fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            } else {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    borrowedBooks.forEach { entry ->
                                        val book = LibraryCatalog.findById(entry.bookId) ?: return@forEach
                                        val isOffline = offlineCategories.contains(book.category.name)
                                        Surface(
                                            color = ParchmentSoft,
                                            shape = RoundedCornerShape(12.dp),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .border(1.dp, ParchmentAged, RoundedCornerShape(12.dp))
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.padding(10.dp)
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(width = 10.dp, height = 40.dp)
                                                        .clip(RoundedCornerShape(3.dp))
                                                        .background(book.spineColor)
                                                )
                                                Spacer(Modifier.width(10.dp))
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(
                                                        text = book.title,
                                                        fontFamily = PlayfairDisplayFontFamily,
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 14.sp,
                                                        color = InkCharcoal
                                                    )
                                                    Text(
                                                        text = "SHELF #${book.shelfNumber} • PAGE ${entry.currentPage}/${book.pages.size} • ${if (isOffline) "OFFLINE READY" else "${book.category.packSizeMb} MB NEEDED"}",
                                                        fontFamily = PlusJakartaSansFontFamily,
                                                        fontSize = 10.5.sp,
                                                        color = if (isOffline) EmeraldLibrarian else InkMuted
                                                    )
                                                }
                                                Button(
                                                    onClick = { onTakeAndRead(book) },
                                                    colors = ButtonDefaults.buttonColors(containerColor = BurgundyLeather),
                                                    modifier = Modifier.height(34.dp)
                                                ) {
                                                    Text(if (isOffline) "READ" else "GET", fontSize = 11.sp, color = ParchmentCream, fontWeight = FontWeight.Bold)
                                                }
                                                IconButton(onClick = { onReturnBook(book.id) }) {
                                                    Icon(
                                                        Icons.AutoMirrored.Filled.Undo,
                                                        contentDescription = "Return book to shelf",
                                                        tint = InkMuted
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    1 -> {
                        // Full Card Catalog Search + Real-Time Google Book & Topic Search
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState())
                        ) {
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                                placeholder = { Text("SEARCH BOOKS, AUTHORS, TOPICS, OR ASK GOOGLE...") },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = EmeraldLibrarian,
                                    unfocusedBorderColor = BrassGold,
                                    focusedTextColor = InkCharcoal,
                                    unfocusedTextColor = InkCharcoal
                                ),
                                modifier = Modifier.fillMaxWidth().testTag("catalog_search_input")
                            )

                            Spacer(Modifier.height(8.dp))

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Button(
                                    onClick = {
                                        val q = searchQuery.trim().ifBlank { "Greatest Science & Literature Books" }
                                        onSearchRealWorldLive(q)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldLibrarian),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(40.dp)
                                        .testTag("search_books_google_live_button")
                                ) {
                                    Text(
                                        text = if (searchQuery.isBlank()) {
                                            "LIVE GOOGLE BOOK SEARCH"
                                        } else {
                                            "GOOGLE SEARCH: \"${searchQuery.trim().take(18).uppercase()}\""
                                        },
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                if (liveGlobalSearchState !is LiveKnowledgeUiState.Idle) {
                                    Button(
                                        onClick = onClearLiveGlobalSearch,
                                        colors = ButtonDefaults.buttonColors(containerColor = ParchmentAged),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.height(40.dp)
                                    ) {
                                        Text("CLEAR", fontSize = 10.5.sp, color = InkCharcoal, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }

                            if (liveGlobalSearchState !is LiveKnowledgeUiState.Idle) {
                                Spacer(Modifier.height(8.dp))
                                LiveGoogleKnowledgeCard(
                                    state = liveGlobalSearchState,
                                    currentTopicLabel = searchQuery.ifBlank { "Book Topics" },
                                    completeTopics = emptyList(),
                                    autoSyncEnabled = false,
                                    showInlineTopicSearch = false,
                                    onFetchTopic = { topic, customQ, _ ->
                                        onSearchRealWorldLive(customQ ?: topic)
                                    }
                                )
                            }

                            Spacer(Modifier.height(8.dp))

                            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                item {
                                    CatalogChip(
                                        text = "ALL LEVELS",
                                        selected = selectedLevel == null,
                                        onClick = { selectedLevel = null }
                                    )
                                }
                                items(EducationLevel.entries) { lvl ->
                                    CatalogChip(
                                        text = lvl.displayName,
                                        selected = selectedLevel == lvl,
                                        onClick = { selectedLevel = lvl }
                                    )
                                }
                            }
                            Spacer(Modifier.height(6.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                item {
                                    CatalogChip(
                                        text = "ALL GROUPS",
                                        selected = selectedCategory == null,
                                        onClick = { selectedCategory = null }
                                    )
                                }
                                items(BookCategory.entries) { cat ->
                                    CatalogChip(
                                        text = cat.displayName,
                                        selected = selectedCategory == cat,
                                        onClick = { selectedCategory = cat }
                                    )
                                }
                            }

                            Spacer(Modifier.height(8.dp))

                            if (results.isEmpty()) {
                                Surface(
                                    color = ParchmentSoft,
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(1.dp, ParchmentAged, RoundedCornerShape(12.dp))
                                ) {
                                    Text(
                                        text = "NO SHELF BOOKS MATCHED LOCALLY — TAP 'LIVE GOOGLE BOOK SEARCH' ABOVE TO LOOK UP ANY REAL BOOK OR TOPIC ON GOOGLE!",
                                        fontFamily = PlusJakartaSansFontFamily,
                                        fontSize = 12.sp,
                                        color = InkMuted,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                            } else {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    results.forEach { book ->
                                        val isOffline = offlineCategories.contains(book.category.name)
                                        Surface(
                                            color = ParchmentSoft,
                                            shape = RoundedCornerShape(12.dp),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .border(1.dp, ParchmentAged, RoundedCornerShape(12.dp))
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.padding(10.dp)
                                            ) {
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Text(
                                                            text = "SHELF #${book.shelfNumber} • ${book.category.displayName} • ${book.level.displayName}",
                                                            fontFamily = PlusJakartaSansFontFamily,
                                                            fontWeight = FontWeight.Bold,
                                                            fontSize = 10.sp,
                                                            color = book.category.accentColor
                                                        )
                                                        Spacer(Modifier.width(6.dp))
                                                        Icon(
                                                            imageVector = if (isOffline) Icons.Default.OfflinePin else Icons.Default.CloudDownload,
                                                            contentDescription = null,
                                                            tint = if (isOffline) EmeraldLibrarian else InkMuted,
                                                            modifier = Modifier.size(13.dp)
                                                        )
                                                    }
                                                    Text(
                                                        text = book.title,
                                                        fontFamily = PlayfairDisplayFontFamily,
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 14.sp,
                                                        color = InkCharcoal
                                                    )
                                                    Text(
                                                        text = "by ${book.author} • ${book.completeTopics.size.coerceAtLeast(book.pages.size)} TOPICS • ${if (isOffline) "OFFLINE READY" else "${book.category.packSizeMb} MB"}",
                                                        fontFamily = PlusJakartaSansFontFamily,
                                                        fontSize = 11.sp,
                                                        color = InkMuted
                                                    )
                                                }
                                                IconButton(onClick = { onWalkToShelf(book.shelfNumber) }) {
                                                    Icon(
                                                        Icons.AutoMirrored.Filled.DirectionsWalk,
                                                        contentDescription = "Walk to Shelf ${book.shelfNumber}",
                                                        tint = EmeraldLibrarian
                                                    )
                                                }
                                                Button(
                                                    onClick = { onTakeAndRead(book) },
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = if (isOffline) BurgundyLeather else EmeraldLibrarian
                                                    ),
                                                    modifier = Modifier.height(34.dp)
                                                ) {
                                                    Text(
                                                        text = if (isOffline) "READ" else "${book.category.packSizeMb} MB",
                                                        fontSize = 11.sp,
                                                        color = ParchmentCream,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    else -> {
                        // Projects & Artwork Studio (Only available in the Library Centre)
                        ProjectsAndArtworkStudio(
                            projects = projects,
                            savedArtworks = savedArtworks,
                            customTemplates = customModelTemplates,
                            onAddProject = onAddProject,
                            onToggleProjectDone = onToggleProjectDone,
                            onDeleteProject = onDeleteProject,
                            onSaveArtwork = onSaveArtwork,
                            onDeleteArtwork = onDeleteArtwork,
                            onSaveCustomTemplate = onSaveCustomTemplate,
                            onDeleteCustomTemplate = onDeleteCustomTemplate
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LibraryOptionCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    accent: Color,
    testTag: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = ParchmentSoft,
        shape = RoundedCornerShape(14.dp),
        modifier = modifier
            .border(1.dp, accent.copy(alpha = 0.45f), RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .testTag(testTag)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 12.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(accent.copy(alpha = 0.14f))
            ) {
                Icon(icon, contentDescription = null, tint = accent, modifier = Modifier.size(19.dp))
            }
            Spacer(Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontFamily = PlusJakartaSansFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = MahoganyDark,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = subtitle,
                    fontFamily = PlusJakartaSansFontFamily,
                    fontSize = 10.sp,
                    color = InkMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun CatalogChip(text: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        color = if (selected) EmeraldLibrarian else ParchmentSoft,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .border(1.dp, EmeraldLibrarian.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
            .clickable { onClick() }
    ) {
        Text(
            text = text,
            fontFamily = PlusJakartaSansFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 11.sp,
            color = if (selected) Color.White else InkCharcoal,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
        )
    }
}
