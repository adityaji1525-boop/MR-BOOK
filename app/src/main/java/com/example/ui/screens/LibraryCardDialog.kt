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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.BookProgressDoc
import com.example.data.LibraryCatalog
import com.example.data.MarginNoteDoc
import com.example.data.ReadingHistoryDoc
import com.example.data.UserProfileDoc
import com.example.model.CharacterAvatar
import com.example.model.ScholarRank
import com.example.ui.components.CharacterPreviewSprite
import com.example.ui.components.MrBookLogoBadge
import com.example.ui.theme.BrassGold
import com.example.ui.theme.BurgundyLeather
import com.example.ui.theme.EmeraldLibrarian
import com.example.ui.theme.InkCharcoal
import com.example.ui.theme.InkMuted
import com.example.ui.theme.LoraFontFamily
import com.example.ui.theme.MahoganyDark
import com.example.ui.theme.ParchmentAged
import com.example.ui.theme.ParchmentCream
import com.example.ui.theme.ParchmentSoft
import com.example.ui.theme.PlayfairDisplayFontFamily
import com.example.ui.theme.PlusJakartaSansFontFamily
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun LibraryCardDialog(
    profile: UserProfileDoc,
    avatar: CharacterAvatar,
    cloakColor: Color,
    bookProgressList: List<BookProgressDoc>,
    readingHistory: List<ReadingHistoryDoc>,
    allNotes: List<MarginNoteDoc>,
    onUpdateStatus: (String) -> Unit,
    onEditCharacter: () -> Unit,
    onOpenBook: (String) -> Unit,
    onClose: () -> Unit
) {
    BackHandler { onClose() }

    val rank = ScholarRank.fromPages(profile.totalPagesRead.toInt())
    val activeStackCount = bookProgressList.count { it.status == "borrowed" || it.status == "reading" }
    val completedCount = maxOf(profile.booksCompleted.toInt(), bookProgressList.count { it.isCompleted })

    var activeTab by remember { mutableIntStateOf(0) }
    var statusDraft by remember(profile.statusText) { mutableStateOf(profile.statusText) }

    val quickStatuses = remember {
        listOf(
            "EXPLORING THE LIBRARY",
            "DEEP IN STUDY",
            "UNDER THE STARLIGHT LAMP",
            "TAKING MARGIN NOTES"
        )
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
                .border(2.dp, BrassGold, RoundedCornerShape(22.dp))
                .clickable(enabled = false) {}
                .testTag("library_card_dialog")
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        MrBookLogoBadge(size = 36.dp, isCircular = true, tag = "library_card_app_logo")
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "MR BOOK • PROFILE & LEDGER",
                            fontFamily = PlayfairDisplayFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = BurgundyLeather,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    IconButton(onClick = onClose, modifier = Modifier.testTag("close_library_card")) {
                        Icon(Icons.Default.Close, contentDescription = "Close library card", tint = InkCharcoal)
                    }
                }

                Spacer(Modifier.height(6.dp))

                // 3 Tabs: Profile & Status, Progress, History & Notes
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
                    listOf(
                        "PROFILE & STATUS",
                        "PROGRESS (${bookProgressList.size})",
                        "HISTORY (${readingHistory.size})"
                    ).forEachIndexed { idx, label ->
                        val selected = activeTab == idx
                        Surface(
                            color = if (selected) BurgundyLeather else ParchmentSoft,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .border(1.dp, BrassGold.copy(alpha = 0.7f), RoundedCornerShape(10.dp))
                                .clickable { activeTab = idx }
                        ) {
                            Text(
                                text = label,
                                fontFamily = PlusJakartaSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = if (selected) ParchmentCream else InkCharcoal,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(10.dp))

                when (activeTab) {
                    0 -> {
                        Surface(
                            color = ParchmentSoft,
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.5.dp, BrassGold.copy(alpha = 0.7f), RoundedCornerShape(16.dp))
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(12.dp)
                            ) {
                                CharacterPreviewSprite(
                                    avatar = avatar,
                                    cloakColor = cloakColor,
                                    size = 64.dp,
                                    carriedBooksCount = activeStackCount
                                )
                                Spacer(Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = profile.characterName.uppercase(),
                                        fontFamily = PlayfairDisplayFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp,
                                        color = MahoganyDark
                                    )
                                    Text(
                                        text = "RANK: ${rank.title}",
                                        fontFamily = PlusJakartaSansFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = EmeraldLibrarian
                                    )
                                    Text(
                                        text = "${avatar.title} • LOCAL SCHOLAR",
                                        fontFamily = PlusJakartaSansFontFamily,
                                        fontSize = 11.sp,
                                        color = InkMuted,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                IconButton(
                                    onClick = onEditCharacter,
                                    modifier = Modifier.testTag("edit_character_button")
                                ) {
                                    Icon(Icons.Default.Edit, contentDescription = "Edit character", tint = BurgundyLeather)
                                }
                            }
                        }

                        Spacer(Modifier.height(10.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            StatTile(label = "PAGES TURNED", value = "${profile.totalPagesRead}", modifier = Modifier.weight(1f))
                            StatTile(label = "COMPLETED", value = "$completedCount", modifier = Modifier.weight(1f))
                            StatTile(label = "STUDY MINS", value = "${profile.totalStudyMinutes}", modifier = Modifier.weight(1f))
                        }

                        Spacer(Modifier.height(12.dp))

                        Text(
                            text = "LIVE READER STATUS",
                            fontFamily = PlusJakartaSansFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = InkCharcoal
                        )
                        Spacer(Modifier.height(6.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = statusDraft,
                                onValueChange = { statusDraft = it.take(80) },
                                singleLine = true,
                                placeholder = { Text("WHAT ARE YOU READING RIGHT NOW?") },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = BurgundyLeather,
                                    unfocusedBorderColor = BrassGold,
                                    focusedTextColor = InkCharcoal,
                                    unfocusedTextColor = InkCharcoal
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("status_input_field")
                            )
                            Button(
                                onClick = { onUpdateStatus(statusDraft.uppercase()) },
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldLibrarian),
                                modifier = Modifier
                                    .height(50.dp)
                                    .testTag("save_status_button")
                            ) {
                                Text("SAVE", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }

                        Spacer(Modifier.height(6.dp))

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(quickStatuses) { preset ->
                                Surface(
                                    color = if (statusDraft == preset) EmeraldLibrarian else ParchmentSoft,
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .border(1.dp, EmeraldLibrarian.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                        .clickable {
                                            statusDraft = preset
                                            onUpdateStatus(preset)
                                        }
                                ) {
                                    Text(
                                        text = preset,
                                        fontFamily = PlusJakartaSansFontFamily,
                                        fontSize = 11.sp,
                                        color = if (statusDraft == preset) Color.White else InkCharcoal,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(14.dp))

                        Button(
                            onClick = onClose,
                            colors = ButtonDefaults.buttonColors(containerColor = BurgundyLeather),
                            modifier = Modifier.fillMaxWidth().height(44.dp)
                        ) {
                            Text("BACK TO HALL", color = ParchmentCream, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }

                    1 -> {
                        if (bookProgressList.isEmpty()) {
                            Surface(
                                color = ParchmentSoft,
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "NO READING PROGRESS RECORDED YET. PICK A BOOK FROM SHELVES 1–10 AND SIT AT A STUDY TABLE!",
                                    fontFamily = LoraFontFamily,
                                    fontSize = 13.sp,
                                    color = InkMuted,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        } else {
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.height(310.dp)
                            ) {
                                items(bookProgressList, key = { it.bookId }) { progress ->
                                    val fraction = (progress.currentPage.toFloat() / progress.totalPages.coerceAtLeast(1L).toFloat()).coerceIn(0f, 1f)
                                    Surface(
                                        color = ParchmentSoft,
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .border(1.dp, ParchmentAged, RoundedCornerShape(12.dp))
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(
                                                        text = progress.bookTitle.ifBlank { LibraryCatalog.findById(progress.bookId)?.title ?: progress.bookId },
                                                        fontFamily = PlayfairDisplayFontFamily,
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 15.sp,
                                                        color = InkCharcoal
                                                    )
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Text(
                                                            text = "PAGE ${progress.currentPage} OF ${progress.totalPages} • ${progress.status.uppercase()}",
                                                            fontFamily = PlusJakartaSansFontFamily,
                                                            fontSize = 11.sp,
                                                            color = InkMuted
                                                        )
                                                        if (progress.isBookmarked) {
                                                            Spacer(Modifier.width(6.dp))
                                                            Icon(
                                                                Icons.Default.Bookmark,
                                                                contentDescription = "Bookmarked",
                                                                tint = BurgundyLeather,
                                                                modifier = Modifier.size(13.dp)
                                                            )
                                                        }
                                                        if (progress.isCompleted) {
                                                            Spacer(Modifier.width(4.dp))
                                                            Icon(
                                                                Icons.Default.CheckCircle,
                                                                contentDescription = "Completed",
                                                                tint = EmeraldLibrarian,
                                                                modifier = Modifier.size(13.dp)
                                                            )
                                                        }
                                                    }
                                                }
                                                Button(
                                                    onClick = { onOpenBook(progress.bookId) },
                                                    colors = ButtonDefaults.buttonColors(containerColor = BurgundyLeather),
                                                    modifier = Modifier.height(34.dp)
                                                ) {
                                                    Text("RESUME", fontSize = 11.sp, color = ParchmentCream)
                                                }
                                            }
                                            Spacer(Modifier.height(6.dp))
                                            LinearProgressIndicator(
                                                progress = { fraction },
                                                color = if (progress.isCompleted) EmeraldLibrarian else BurgundyLeather,
                                                trackColor = ParchmentAged,
                                                modifier = Modifier.fillMaxWidth().height(5.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    else -> {
                        val dateFormatter = remember { SimpleDateFormat("MMM d, h:mm a", Locale.getDefault()) }
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.height(310.dp)
                        ) {
                            if (readingHistory.isNotEmpty()) {
                                item {
                                    Text(
                                        text = "RECENT READING HISTORY",
                                        fontFamily = PlusJakartaSansFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = InkCharcoal
                                    )
                                }
                                items(readingHistory.take(25), key = { "hist_${it.id}" }) { h ->
                                    val timeStr = h.createdAt?.toDate()?.let { dateFormatter.format(it).uppercase() } ?: "JUST NOW"
                                    val actionLabel = when (h.action) {
                                        "downloaded_section" -> "DOWNLOADED OFFLINE PACK"
                                        "turned_page" -> "READ PAGE ${h.pageNumber}"
                                        "completed" -> "COMPLETED BOOK"
                                        "borrowed" -> "TOOK FROM SHELF"
                                        "returned" -> "RETURNED TO SHELF"
                                        else -> "OPENED BOOK"
                                    }
                                    Surface(
                                        color = ParchmentSoft,
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .border(1.dp, ParchmentAged, RoundedCornerShape(10.dp))
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(10.dp)
                                        ) {
                                            Icon(
                                                Icons.Default.History,
                                                contentDescription = null,
                                                tint = EmeraldLibrarian,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Spacer(Modifier.width(8.dp))
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = h.bookTitle,
                                                    fontFamily = PlusJakartaSansFontFamily,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 12.5.sp,
                                                    color = InkCharcoal
                                                )
                                                Text(
                                                    text = "$actionLabel • ${h.categoryName.uppercase()} (${h.levelName.uppercase()})",
                                                    fontFamily = PlusJakartaSansFontFamily,
                                                    fontSize = 11.sp,
                                                    color = InkMuted
                                                )
                                            }
                                            Text(
                                                text = timeStr,
                                                fontFamily = PlusJakartaSansFontFamily,
                                                fontSize = 10.sp,
                                                color = InkMuted
                                            )
                                        }
                                    }
                                }
                            } else {
                                item {
                                    Text(
                                        text = "YOUR READING TIMELINE WILL APPEAR HERE AS YOU DOWNLOAD SECTIONS, BORROW BOOKS, AND TURN CHAPTERS.",
                                        fontFamily = LoraFontFamily,
                                        fontSize = 13.sp,
                                        color = InkMuted
                                    )
                                }
                            }

                            if (allNotes.isNotEmpty()) {
                                item {
                                    Spacer(Modifier.height(4.dp))
                                    Text(
                                        text = "SAVED MARGIN NOTES (${allNotes.size})",
                                        fontFamily = PlusJakartaSansFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = InkCharcoal
                                    )
                                }
                                items(allNotes.take(15), key = { "note_${it.id}" }) { note ->
                                    val book = LibraryCatalog.findById(note.bookId)
                                    Surface(
                                        color = ParchmentSoft,
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .border(1.dp, ParchmentAged, RoundedCornerShape(10.dp))
                                            .clickable { if (book != null) onOpenBook(book.id) }
                                    ) {
                                        Column(modifier = Modifier.padding(10.dp)) {
                                            Text(
                                                text = "${note.bookTitle.ifBlank { book?.title ?: "Book" }} • PAGE ${note.pageNumber}",
                                                fontFamily = PlusJakartaSansFontFamily,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.sp,
                                                color = BurgundyLeather
                                            )
                                            Text(
                                                text = note.noteText,
                                                fontFamily = LoraFontFamily,
                                                fontSize = 12.5.sp,
                                                color = InkCharcoal
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

@Composable
private fun StatTile(label: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        color = ParchmentSoft,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.border(1.dp, ParchmentAged, RoundedCornerShape(12.dp))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 6.dp)
        ) {
            Text(
                text = value,
                fontFamily = PlayfairDisplayFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = BurgundyLeather
            )
            Text(
                text = label,
                fontFamily = PlusJakartaSansFontFamily,
                fontSize = 11.sp,
                color = InkMuted
            )
        }
    }
}
