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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LibraryCatalog
import com.example.data.LibraryFloorLayout
import com.example.model.BookCategory
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
fun HelpMapSheet(
    characterName: String,
    highlightedShelfNumber: Int?,
    onGoToShelf: (Int) -> Unit,
    onGoToLibrarian: () -> Unit,
    onGoToTable: (String) -> Unit,
    onClose: () -> Unit
) {
    BackHandler { onClose() }

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
                .testTag("help_map_dialog")
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Map, contentDescription = null, tint = BurgundyLeather)
                        Spacer(Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "LIBRARY SHELF HELP MAP",
                                fontFamily = PlayfairDisplayFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 19.sp,
                                color = MahoganyDark
                            )
                            Text(
                                text = "TAP ANY NUMBERED SHELF TO WALK ${characterName.uppercase()} THERE",
                                fontFamily = PlusJakartaSansFontFamily,
                                fontSize = 11.sp,
                                color = InkMuted
                            )
                        }
                    }
                    IconButton(onClick = onClose, modifier = Modifier.testTag("close_help_map")) {
                        Icon(Icons.Default.Close, contentDescription = "Close help map", tint = InkCharcoal)
                    }
                }

                Spacer(Modifier.height(10.dp))

                // Visual 2-Wing Blueprint Directory
                Surface(
                    color = MahoganyDark,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, BrassGold.copy(alpha = 0.7f), RoundedCornerShape(14.dp))
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        // Top Librarian Station
                        Surface(
                            color = EmeraldLibrarian,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .clickable { onGoToLibrarian() }
                        ) {
                            Text(
                                text = "LIBRARY CENTRE • PROJECTS, ARTWORK & BOOKS",
                                fontFamily = PlusJakartaSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.5.sp,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }

                        Spacer(Modifier.height(8.dp))

                        // Left Wing (1-5), Center Tables (A-D), Right Wing (6-10)
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Left Wing Shelves 1..5
                            Column(
                                verticalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.weight(1.1f)
                            ) {
                                LibraryFloorLayout.bookshelves.filter { it.isLeftRow }.forEach { shelf ->
                                    MapShelfMiniTile(
                                        shelfNumber = shelf.shelfNumber,
                                        category = shelf.category,
                                        isHighlighted = highlightedShelfNumber == shelf.shelfNumber,
                                        onClick = { onGoToShelf(shelf.shelfNumber) }
                                    )
                                }
                            }

                            // Center Study Tables A..D
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.weight(0.85f).padding(top = 12.dp)
                            ) {
                                Text(
                                    text = "LAMP TABLES",
                                    fontFamily = PlusJakartaSansFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 9.sp,
                                    color = BrassGold
                                )
                                LibraryFloorLayout.studyTables.forEach { table ->
                                    Surface(
                                        color = Color(0xFF6B3F28),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .border(1.dp, BrassGold.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                                            .clickable { onGoToTable(table.tableId) }
                                    ) {
                                        Text(
                                            text = "${table.label}\nLAMP",
                                            fontFamily = PlusJakartaSansFontFamily,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 10.sp,
                                            color = ParchmentCream,
                                            modifier = Modifier.padding(6.dp)
                                        )
                                    }
                                }
                            }

                            // Right Wing Shelves 6..10
                            Column(
                                verticalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.weight(1.1f)
                            ) {
                                LibraryFloorLayout.bookshelves.filter { !it.isLeftRow }.forEach { shelf ->
                                    MapShelfMiniTile(
                                        shelfNumber = shelf.shelfNumber,
                                        category = shelf.category,
                                        isHighlighted = highlightedShelfNumber == shelf.shelfNumber,
                                        onClick = { onGoToShelf(shelf.shelfNumber) }
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(10.dp))

                Text(
                    text = "CATEGORY DIRECTORY (SCHOOL → PROFESSIONAL)",
                    fontFamily = PlusJakartaSansFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.5.sp,
                    color = InkCharcoal
                )
                Spacer(Modifier.height(6.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.height(185.dp)
                ) {
                    items(LibraryFloorLayout.bookshelves) { shelf ->
                        val count = LibraryCatalog.booksOnShelf(shelf.shelfNumber).size
                        Surface(
                            color = ParchmentSoft,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, ParchmentAged, RoundedCornerShape(10.dp))
                                .clickable { onGoToShelf(shelf.shelfNumber) }
                                .testTag("help_map_shelf_row_${shelf.shelfNumber}")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clip(CircleShape)
                                        .background(shelf.category.accentColor)
                                ) {
                                    Text(
                                        text = "${shelf.shelfNumber}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = Color.White
                                    )
                                }
                                Spacer(Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "SHELF #${shelf.shelfNumber}: ${shelf.category.displayName}",
                                        fontFamily = PlusJakartaSansFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.5.sp,
                                        color = InkCharcoal
                                    )
                                    Text(
                                        text = "${shelf.subtitle} • $count BOOKS",
                                        fontFamily = PlusJakartaSansFontFamily,
                                        fontSize = 10.5.sp,
                                        color = InkMuted
                                    )
                                }
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.DirectionsWalk,
                                    contentDescription = "Walk to shelf ${shelf.shelfNumber}",
                                    tint = BurgundyLeather,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MapShelfMiniTile(
    shelfNumber: Int,
    category: BookCategory,
    isHighlighted: Boolean,
    onClick: () -> Unit
) {
    Surface(
        color = if (isHighlighted) BrassGold else Color(0xFF3E261E),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, category.accentColor, RoundedCornerShape(8.dp))
            .clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 5.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(category.accentColor)
            )
            Spacer(Modifier.width(5.dp))
            Text(
                text = "#$shelfNumber ${category.displayName.take(8)}",
                fontFamily = PlusJakartaSansFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                color = if (isHighlighted) MahoganyDark else ParchmentCream
            )
        }
    }
}
