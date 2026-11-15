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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.OfflinePin
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DownloadedSectionEntity
import com.example.data.LibraryCatalog
import com.example.data.SectionDownloadProgress
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
fun SectionDownloadsSheet(
    downloadedSections: List<DownloadedSectionEntity>,
    activeDownloads: Map<String, SectionDownloadProgress>,
    onDownloadSection: (BookCategory) -> Unit,
    onRemoveSection: (BookCategory) -> Unit,
    onDownloadAll: () -> Unit,
    onClose: () -> Unit
) {
    BackHandler { onClose() }

    val downloadedMap = downloadedSections.associateBy { it.categoryId }
    val totalAvailableMb = BookCategory.entries.sumOf { it.packSizeMb }
    val downloadedMb = BookCategory.entries.sumOf { cat ->
        if (downloadedMap[cat.name]?.isDownloaded == true) cat.packSizeMb else 0
    }
    val downloadedCount = BookCategory.entries.count { downloadedMap[it.name]?.isDownloaded == true }

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
                .testTag("section_downloads_sheet")
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.OfflinePin, contentDescription = null, tint = EmeraldLibrarian)
                        Spacer(Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "OFFLINE BOOK SECTIONS",
                                fontFamily = PlayfairDisplayFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = MahoganyDark
                            )
                            Text(
                                text = "$downloadedCount/${BookCategory.entries.size} SECTIONS OFFLINE • $downloadedMb / $totalAvailableMb MB",
                                fontFamily = PlusJakartaSansFontFamily,
                                fontSize = 12.sp,
                                color = InkMuted
                            )
                        }
                    }
                    IconButton(onClick = onClose, modifier = Modifier.testTag("close_downloads_sheet")) {
                        Icon(Icons.Default.Close, contentDescription = "Close downloads", tint = InkCharcoal)
                    }
                }

                Spacer(Modifier.height(8.dp))

                Surface(
                    color = Color(0xFFE7F2ED),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.padding(10.dp)
                    ) {
                        Text(
                            text = "EACH SECTION IS 24–78 MB. ONCE DOWNLOADED, ALL BOOKS IN THAT SECTION OPEN OFFLINE!",
                            fontFamily = PlusJakartaSansFontFamily,
                            fontSize = 11.5.sp,
                            color = MahoganyDark,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(Modifier.width(8.dp))
                        if (downloadedCount < BookCategory.entries.size) {
                            Button(
                                onClick = onDownloadAll,
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldLibrarian),
                                modifier = Modifier.height(34.dp).testTag("download_all_sections_button")
                            ) {
                                Text("GET ALL", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Spacer(Modifier.height(10.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.height(360.dp)
                ) {
                    items(BookCategory.entries, key = { it.name }) { category ->
                        val isDownloaded = downloadedMap[category.name]?.isDownloaded == true
                        val progress = activeDownloads[category.name]
                        val bookCount = LibraryCatalog.booksByCategory(category).size

                        Surface(
                            color = ParchmentSoft,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = 1.dp,
                                    color = if (isDownloaded) EmeraldLibrarian else ParchmentAged,
                                    shape = RoundedCornerShape(14.dp)
                                )
                                .testTag("section_pack_card_${category.name}")
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = "${category.displayName} (${category.shelfNumbersLabel})",
                                                fontFamily = PlusJakartaSansFontFamily,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.5.sp,
                                                color = InkCharcoal
                                            )
                                            Spacer(Modifier.width(6.dp))
                                            Surface(
                                                color = category.accentColor.copy(alpha = 0.16f),
                                                shape = RoundedCornerShape(6.dp)
                                            ) {
                                                Text(
                                                    text = "${category.packSizeMb} MB",
                                                    fontFamily = PlusJakartaSansFontFamily,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 10.sp,
                                                    color = category.accentColor,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                        Text(
                                            text = "$bookCount BOOKS • ${category.packDescription}",
                                            fontFamily = PlusJakartaSansFontFamily,
                                            fontSize = 11.sp,
                                            color = InkMuted
                                        )
                                    }

                                    Spacer(Modifier.width(8.dp))

                                    when {
                                        progress != null -> {
                                            Text(
                                                text = "${progress.downloadedMb.toInt()}/${progress.totalMb} MB",
                                                fontFamily = PlusJakartaSansFontFamily,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.sp,
                                                color = BurgundyLeather
                                            )
                                        }
                                        isDownloaded -> {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    Icons.Default.CheckCircle,
                                                    contentDescription = "Offline ready",
                                                    tint = EmeraldLibrarian,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                                IconButton(
                                                    onClick = { onRemoveSection(category) },
                                                    modifier = Modifier.size(32.dp)
                                                ) {
                                                    Icon(
                                                        Icons.Default.DeleteOutline,
                                                        contentDescription = "Remove offline pack",
                                                        tint = InkMuted,
                                                        modifier = Modifier.size(18.dp)
                                                    )
                                                }
                                            }
                                        }
                                        else -> {
                                            Button(
                                                onClick = { onDownloadSection(category) },
                                                colors = ButtonDefaults.buttonColors(containerColor = BurgundyLeather),
                                                modifier = Modifier
                                                    .height(36.dp)
                                                    .testTag("download_section_${category.name}")
                                            ) {
                                                Icon(
                                                    Icons.Default.CloudDownload,
                                                    contentDescription = null,
                                                    tint = ParchmentCream,
                                                    modifier = Modifier.size(15.dp)
                                                )
                                                Spacer(Modifier.width(4.dp))
                                                Text(
                                                    text = "DOWNLOAD",
                                                    fontSize = 11.sp,
                                                    color = ParchmentCream,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }
                                }

                                if (progress != null) {
                                    Spacer(Modifier.height(8.dp))
                                    LinearProgressIndicator(
                                        progress = { progress.fraction },
                                        color = category.accentColor,
                                        trackColor = ParchmentAged,
                                        modifier = Modifier.fillMaxWidth().height(6.dp)
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
