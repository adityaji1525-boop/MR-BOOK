package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Chair
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.OfflinePin
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.BookProgressDoc
import com.example.data.DownloadedSectionEntity
import com.example.data.LibraryCatalog
import com.example.data.LibraryFloorLayout
import com.example.data.SectionDownloadProgress
import com.example.model.Book
import com.example.model.BookCategory
import com.example.model.EducationLevel
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
fun ShelfBrowserSheet(
    shelfNumber: Int,
    bookProgressList: List<BookProgressDoc>,
    downloadedSections: List<DownloadedSectionEntity>,
    activeDownloads: Map<String, SectionDownloadProgress>,
    onDownloadSection: (BookCategory) -> Unit,
    onTakeBookOnly: (Book) -> Unit,
    onTakeAndSitAtTable: (Book) -> Unit,
    onClose: () -> Unit
) {
    BackHandler { onClose() }

    val shelfInfo = LibraryFloorLayout.bookshelves.firstOrNull { it.shelfNumber == shelfNumber }
    val shelfCategory = shelfInfo?.category
    val isSectionOffline = shelfCategory != null && downloadedSections.any {
        it.categoryId == shelfCategory.name && it.isDownloaded
    }
    val activeDownload = shelfCategory?.let { activeDownloads[it.name] }

    val allShelfBooks = remember(shelfNumber) { LibraryCatalog.booksOnShelf(shelfNumber) }
    var selectedLevel by remember { mutableStateOf<EducationLevel?>(null) }
    var shelfSearchQuery by remember { mutableStateOf("") }

    val filteredBooks = remember(allShelfBooks, selectedLevel, shelfSearchQuery) {
        val q = shelfSearchQuery.trim().lowercase()
        allShelfBooks.filter { book ->
            val matchesLevel = selectedLevel == null || book.level == selectedLevel
            val matchesQuery = q.isEmpty() ||
                book.title.lowercase().contains(q) ||
                book.author.lowercase().contains(q) ||
                book.synopsis.lowercase().contains(q) ||
                book.realSubjectScope.lowercase().contains(q) ||
                book.completeTopics.any { it.lowercase().contains(q) }
            matchesLevel && matchesQuery
        }
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
                .border(2.dp, shelfCategory?.accentColor ?: BrassGold, RoundedCornerShape(22.dp))
                .clickable(enabled = false) {}
                .testTag("shelf_browser_sheet")
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Surface(
                            color = shelfCategory?.accentColor ?: BurgundyLeather,
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = "SHELF #$shelfNumber",
                                fontFamily = PlusJakartaSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text(
                                text = (shelfInfo?.wingTitle ?: "BOOKSHELF #$shelfNumber").uppercase(),
                                fontFamily = PlayfairDisplayFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = MahoganyDark
                            )
                            Text(
                                text = (shelfInfo?.subtitle ?: "").uppercase(),
                                fontFamily = PlusJakartaSansFontFamily,
                                fontSize = 11.5.sp,
                                color = InkMuted
                            )
                        }
                    }
                    IconButton(onClick = onClose, modifier = Modifier.testTag("close_shelf_browser")) {
                        Icon(Icons.Default.Close, contentDescription = "Close shelf browser", tint = InkCharcoal)
                    }
                }

                // Offline Section Pack Status Banner (20-80 MB per section)
                if (shelfCategory != null) {
                    Spacer(Modifier.height(8.dp))
                    Surface(
                        color = if (isSectionOffline) Color(0xFFE7F2ED) else ParchmentSoft,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = if (isSectionOffline) EmeraldLibrarian else BrassGold.copy(alpha = 0.7f),
                                shape = RoundedCornerShape(12.dp)
                            )
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = if (isSectionOffline) Icons.Default.OfflinePin else Icons.Default.CloudDownload,
                                        contentDescription = null,
                                        tint = if (isSectionOffline) EmeraldLibrarian else BurgundyLeather,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = if (isSectionOffline) {
                                                "${shelfCategory.displayName} SECTION OFFLINE READY (${shelfCategory.packSizeMb} MB)"
                                            } else {
                                                "${shelfCategory.displayName} PACK • ${shelfCategory.packSizeMb} MB"
                                            },
                                            fontFamily = PlusJakartaSansFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.5.sp,
                                            color = if (isSectionOffline) EmeraldLibrarian else MahoganyDark
                                        )
                                        Text(
                                            text = if (isSectionOffline) {
                                                "EVERY BOOK IN THIS SECTION OPENS ANYTIME OFFLINE."
                                            } else {
                                                "DOWNLOAD SECTION ONCE TO UNLOCK OFFLINE READING."
                                            },
                                            fontFamily = PlusJakartaSansFontFamily,
                                            fontSize = 10.5.sp,
                                            color = InkMuted
                                        )
                                    }
                                }
                                if (!isSectionOffline && activeDownload == null) {
                                    Spacer(Modifier.width(8.dp))
                                    Button(
                                        onClick = { onDownloadSection(shelfCategory) },
                                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldLibrarian),
                                        modifier = Modifier
                                            .height(34.dp)
                                            .testTag("shelf_download_section_${shelfCategory.name}")
                                    ) {
                                        Text(
                                            text = "GET ${shelfCategory.packSizeMb} MB",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                } else if (activeDownload != null) {
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        text = "${activeDownload.downloadedMb.toInt()}/${activeDownload.totalMb} MB",
                                        fontFamily = PlusJakartaSansFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = BurgundyLeather
                                    )
                                }
                            }
                            if (activeDownload != null) {
                                Spacer(Modifier.height(6.dp))
                                LinearProgressIndicator(
                                    progress = { activeDownload.fraction },
                                    color = shelfCategory.accentColor,
                                    trackColor = ParchmentAged,
                                    modifier = Modifier.fillMaxWidth().height(5.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = shelfSearchQuery,
                    onValueChange = { shelfSearchQuery = it },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    placeholder = { Text("SEARCH BOOKS ON THIS SHELF...") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BurgundyLeather,
                        unfocusedBorderColor = BrassGold,
                        focusedTextColor = InkCharcoal,
                        unfocusedTextColor = InkCharcoal
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("shelf_search_input")
                )

                Spacer(Modifier.height(8.dp))

                // Level Filter Chips (School -> Professional)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    item {
                        LevelFilterChip(
                            label = "ALL LEVELS",
                            selected = selectedLevel == null,
                            onClick = { selectedLevel = null }
                        )
                    }
                    items(EducationLevel.entries) { level ->
                        LevelFilterChip(
                            label = level.displayName,
                            selected = selectedLevel == level,
                            onClick = { selectedLevel = level }
                        )
                    }
                }

                Spacer(Modifier.height(10.dp))

                if (filteredBooks.isEmpty()) {
                    Surface(
                        color = ParchmentSoft,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
                    ) {
                        Text(
                            text = "NO MATCHING BOOKS ON SHELF #$shelfNumber. CLEAR SEARCH OR TRY ANOTHER LEVEL FILTER!",
                            fontFamily = PlusJakartaSansFontFamily,
                            fontSize = 12.5.sp,
                            color = InkMuted,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.height(320.dp)
                    ) {
                        items(filteredBooks, key = { it.id }) { book ->
                            val progress = bookProgressList.firstOrNull { it.bookId == book.id }
                            val isInStack = progress?.status == "borrowed" || progress?.status == "reading"
                            val bookSectionOffline = downloadedSections.any {
                                it.categoryId == book.category.name && it.isDownloaded
                            }
                            val bookDownload = activeDownloads[book.category.name]

                            Surface(
                                color = ParchmentSoft,
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, ParchmentAged, RoundedCornerShape(16.dp))
                                    .testTag("shelf_book_card_${book.id}")
                            ) {
                                Row(modifier = Modifier.padding(12.dp)) {
                                    // Vertical Book Spine Visual
                                    Box(
                                        modifier = Modifier
                                            .width(18.dp)
                                            .height(92.dp)
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(book.spineColor)
                                            .border(1.dp, book.coverTrimColor, RoundedCornerShape(4.dp))
                                    )
                                    Spacer(Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Surface(
                                                color = book.category.accentColor.copy(alpha = 0.16f),
                                                shape = RoundedCornerShape(6.dp)
                                            ) {
                                                Text(
                                                    text = book.level.displayName,
                                                    fontFamily = PlusJakartaSansFontFamily,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 10.sp,
                                                    color = book.category.accentColor,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                            Text(
                                                text = if (progress != null && progress.currentPage > 0L) {
                                                    "PAGE ${progress.currentPage}/${book.pages.size} • ${book.completeTopics.size} TOPICS"
                                                } else {
                                                    "${book.pages.size} CHAPTERS • ${book.completeTopics.size} REAL TOPICS"
                                                },
                                                fontFamily = PlusJakartaSansFontFamily,
                                                fontSize = 11.sp,
                                                color = InkMuted
                                            )
                                            if (progress?.isCompleted == true) {
                                                Icon(
                                                    Icons.Default.CheckCircle,
                                                    contentDescription = "Completed",
                                                    tint = EmeraldLibrarian,
                                                    modifier = Modifier.size(15.dp)
                                                )
                                            }
                                        }

                                        Spacer(Modifier.height(4.dp))
                                        Text(
                                            text = book.title,
                                            fontFamily = PlayfairDisplayFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            color = InkCharcoal
                                        )
                                        Text(
                                            text = "by ${book.author}",
                                            fontFamily = PlusJakartaSansFontFamily,
                                            fontSize = 12.sp,
                                            color = InkMuted
                                        )
                                        Spacer(Modifier.height(4.dp))
                                        Text(
                                            text = book.synopsis,
                                            fontFamily = PlusJakartaSansFontFamily,
                                            fontSize = 12.sp,
                                            lineHeight = 16.sp,
                                            color = InkCharcoal
                                        )

                                        Spacer(Modifier.height(10.dp))
                                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            if (bookSectionOffline) {
                                                OutlinedButton(
                                                    onClick = { onTakeBookOnly(book) },
                                                    modifier = Modifier
                                                        .height(38.dp)
                                                        .testTag("take_book_${book.id}")
                                                ) {
                                                    Icon(
                                                        Icons.AutoMirrored.Filled.MenuBook,
                                                        contentDescription = null,
                                                        tint = BurgundyLeather,
                                                        modifier = Modifier.size(15.dp)
                                                    )
                                                    Spacer(Modifier.width(4.dp))
                                                    Text(
                                                        text = if (isInStack) "IN STACK" else "TAKE",
                                                        fontSize = 11.5.sp,
                                                        color = BurgundyLeather
                                                    )
                                                }
                                                Button(
                                                    onClick = { onTakeAndSitAtTable(book) },
                                                    colors = ButtonDefaults.buttonColors(containerColor = BurgundyLeather),
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .height(38.dp)
                                                        .testTag("read_at_table_${book.id}")
                                                ) {
                                                    Icon(
                                                        Icons.Default.Chair,
                                                        contentDescription = null,
                                                        tint = ParchmentCream,
                                                        modifier = Modifier.size(15.dp)
                                                    )
                                                    Spacer(Modifier.width(5.dp))
                                                    Text(
                                                        text = "TAKE & WALK TO TABLE",
                                                        fontSize = 11.sp,
                                                        color = ParchmentCream,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }
                                            } else {
                                                Button(
                                                    onClick = { onTakeAndSitAtTable(book) },
                                                    enabled = bookDownload == null,
                                                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldLibrarian),
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(38.dp)
                                                        .testTag("read_at_table_${book.id}")
                                                ) {
                                                    Icon(
                                                        Icons.Default.CloudDownload,
                                                        contentDescription = null,
                                                        tint = Color.White,
                                                        modifier = Modifier.size(15.dp)
                                                    )
                                                    Spacer(Modifier.width(6.dp))
                                                    Text(
                                                        text = if (bookDownload != null) {
                                                            "DOWNLOADING ${bookDownload.downloadedMb.toInt()}/${bookDownload.totalMb} MB..."
                                                        } else {
                                                            "DOWNLOAD ${book.category.packSizeMb} MB & READ OFFLINE"
                                                        },
                                                        fontSize = 11.sp,
                                                        color = Color.White,
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
                }
            }
        }
    }
}

@Composable
private fun LevelFilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        color = if (selected) BurgundyLeather else ParchmentSoft,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .border(1.dp, if (selected) BurgundyLeather else BrassGold, RoundedCornerShape(10.dp))
            .clickable { onClick() }
    ) {
        Text(
            text = label,
            fontFamily = PlusJakartaSansFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            color = if (selected) ParchmentCream else InkCharcoal,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}
