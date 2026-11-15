package com.example.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
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
import com.example.data.LibraryCatalog
import com.example.data.MarginNoteDoc
import com.example.model.Book
import com.example.model.LampAmbience
import com.example.model.TableCoverStyle
import com.example.ui.theme.BrassBright
import com.example.ui.theme.BrassGold
import com.example.ui.theme.BurgundyLeather
import com.example.ui.theme.EmeraldLibrarian
import com.example.ui.theme.InkCharcoal
import com.example.ui.theme.InkMuted
import com.example.ui.theme.LoraFontFamily
import com.example.ui.theme.MahoganyDark
import com.example.ui.theme.MahoganyRich
import com.example.ui.theme.ParchmentCream
import com.example.ui.theme.ParchmentSoft
import com.example.ui.theme.PlayfairDisplayFontFamily
import com.example.ui.theme.PlusJakartaSansFontFamily
import kotlin.math.sin

@Composable
fun StudyTableDeskPov(
    characterName: String,
    activeBook: Book,
    deskStackBooks: List<BookProgressDoc>,
    notesForBook: List<MarginNoteDoc>,
    isLampOn: Boolean,
    lampAmbience: LampAmbience,
    lampBrightness: Float,
    tableCover: TableCoverStyle,
    focusSecondsRemaining: Int,
    isFocusTimerRunning: Boolean,
    liveTopicState: LiveKnowledgeUiState,
    autoLiveGoogleSync: Boolean,
    onToggleAutoLiveSync: (Boolean) -> Unit,
    onFetchLiveTopic: (topicTitle: String, chapterContext: String, customQuestion: String?, forceRefresh: Boolean) -> Unit,
    onSelectBook: (String) -> Unit,
    onPageChanged: (Int) -> Unit,
    onToggleBookmark: (Int) -> Unit,
    onToggleLamp: (Boolean) -> Unit,
    onToggleTimer: () -> Unit,
    onAddNote: (Int, String) -> Unit,
    onDeleteNote: (String) -> Unit,
    onStandUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onStandUp() }

    val activeProgress = deskStackBooks.firstOrNull { it.bookId == activeBook.id }
    val currentPageIndex = (activeProgress?.currentPage ?: 0L).toInt()
    val isBookmarkedHere = activeProgress?.isBookmarked == true && activeProgress.bookmarkPage == currentPageIndex.toLong()

    var showDeskNotepad by remember { mutableStateOf(false) }
    var showCompleteTopicsDialog by remember { mutableStateOf(false) }
    var noteDraft by remember { mutableStateOf("") }

    val infiniteTransition = rememberInfiniteTransition(label = "desk_imagination")
    val flicker by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2f * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(tween(4200, easing = LinearEasing), RepeatMode.Restart),
        label = "mote_phase"
    )

    val animatedDeskBrightness by animateFloatAsState(
        targetValue = if (isLampOn) lampBrightness.coerceIn(0.25f, 1.0f) else 0.12f,
        animationSpec = tween(420),
        label = "desk_lamp_brightness"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag("study_table_desk_pov")
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_wood_desk_surface),
            contentDescription = "Wooden study desk surface",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Canvas(modifier = Modifier.fillMaxSize()) {
            // Attractive Selected Table Sheet / Cloth Runner over the Wooden Desk
            if (tableCover.hasClothCover) {
                val insetX = size.width * 0.045f
                val insetY = size.height * 0.095f
                val clothW = size.width - insetX * 2f
                val clothH = size.height - insetY * 2f
                drawRoundRect(
                    color = Color(0x55000000),
                    topLeft = Offset(insetX + 4f, insetY + 6f),
                    size = Size(clothW, clothH),
                    cornerRadius = CornerRadius(24f, 24f)
                )
                drawRoundRect(
                    color = tableCover.primaryColor.copy(alpha = 0.94f),
                    topLeft = Offset(insetX, insetY),
                    size = Size(clothW, clothH),
                    cornerRadius = CornerRadius(24f, 24f)
                )
                // Woven Damask Subtle Vertical Stripes
                val stripeCount = 12
                val stripeStep = clothW / stripeCount
                for (i in 1 until stripeCount) {
                    val sx = insetX + i * stripeStep
                    drawLine(
                        color = tableCover.accentPatternColor.copy(alpha = 0.28f),
                        start = Offset(sx, insetY + 12f),
                        end = Offset(sx, insetY + clothH - 12f),
                        strokeWidth = 3f
                    )
                }
                // Stitched Gold / Embroidered Trim
                drawRoundRect(
                    color = tableCover.trimColor.copy(alpha = 0.9f),
                    topLeft = Offset(insetX + 10f, insetY + 10f),
                    size = Size(clothW - 20f, clothH - 20f),
                    cornerRadius = CornerRadius(18f, 18f),
                    style = Stroke(
                        width = 2.5f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(14f, 8f))
                    )
                )
                drawRoundRect(
                    color = tableCover.trimColor.copy(alpha = 0.55f),
                    topLeft = Offset(insetX + 2f, insetY + 2f),
                    size = Size(clothW - 4f, clothH - 4f),
                    cornerRadius = CornerRadius(22f, 22f),
                    style = Stroke(width = 2f)
                )
            }

            val lampCenter = Offset(size.width * 0.16f, size.height * 0.11f)
            val pulse = 1f + 0.04f * sin(flicker)
            val effectiveAlpha = if (isLampOn) {
                (0.32f + animatedDeskBrightness * 0.48f).coerceIn(0.2f, 0.85f)
            } else {
                0.04f
            }

            if (isLampOn) {
                drawRect(
                    color = lampAmbience.deskTint.copy(
                        alpha = lampAmbience.deskTint.alpha * (1.25f - animatedDeskBrightness * 0.35f)
                    )
                )
            } else {
                drawRect(color = Color(0x990D0809))
            }

            if (isLampOn) {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            lampAmbience.glowColor.copy(alpha = effectiveAlpha * pulse),
                            lampAmbience.glowColor.copy(alpha = effectiveAlpha * 0.35f),
                            Color.Transparent
                        ),
                        center = Offset(size.width * 0.45f, size.height * 0.42f),
                        radius = size.maxDimension * 0.72f * pulse
                    ),
                    center = Offset(size.width * 0.45f, size.height * 0.42f),
                    radius = size.maxDimension * 0.72f
                )
            }

            val moteCount = if (!isLampOn || lampAmbience.isNightImagination) 16 else 9
            for (i in 0 until moteCount) {
                val angle = flicker * 0.35f + i * 0.9f
                val mx = size.width * (0.12f + ((i * 37) % 76) / 100f) + sin(angle) * 14f
                val my = size.height * (0.08f + ((i * 53) % 72) / 100f) + kotlin.math.cos(angle) * 10f
                val r = if (lampAmbience == LampAmbience.STARLIGHT) 2.5f + (i % 3) else 2f + (i % 2)
                drawCircle(
                    color = lampAmbience.glowColor.copy(alpha = 0.45f * animatedDeskBrightness),
                    radius = r,
                    center = Offset(mx, my)
                )
            }

            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(Color.Transparent, Color(0x99120A07)),
                    center = lampCenter,
                    radius = size.maxDimension * 0.95f
                )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    color = MahoganyDark.copy(alpha = 0.88f),
                    shape = RoundedCornerShape(16.dp),
                    shadowElevation = 6.dp,
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = if (isLampOn) lampAmbience.glowColor.copy(alpha = 0.8f) else BrassGold.copy(alpha = 0.4f),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable { onToggleLamp(!isLampOn) }
                        .testTag("desk_lamp_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 8.dp, end = 6.dp, top = 4.dp, bottom = 4.dp)
                    ) {
                        DeskLampMiniIllustration(
                            lampColor = lampAmbience.glowColor,
                            isLampOn = isLampOn,
                            brightness = animatedDeskBrightness,
                            modifier = Modifier.size(30.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Column {
                            Text(
                                text = "DESK LAMP",
                                fontFamily = PlusJakartaSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.5.sp,
                                color = ParchmentCream
                            )
                            Text(
                                text = if (isLampOn) lampAmbience.label else "DIMMED",
                                fontFamily = PlusJakartaSansFontFamily,
                                fontSize = 9.5.sp,
                                color = if (isLampOn) lampAmbience.glowColor else ParchmentSoft
                            )
                        }
                        Spacer(Modifier.width(4.dp))
                        Switch(
                            checked = isLampOn,
                            onCheckedChange = onToggleLamp,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MahoganyDark,
                                checkedTrackColor = lampAmbience.glowColor,
                                uncheckedThumbColor = ParchmentSoft,
                                uncheckedTrackColor = Color(0xFF3E2922)
                            ),
                            modifier = Modifier.testTag("desk_pov_lamp_switch")
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    TeaCupProp(phase = flicker, modifier = Modifier.size(36.dp))

                    val mins = focusSecondsRemaining / 60
                    val secs = focusSecondsRemaining % 60
                    Surface(
                        color = MahoganyDark.copy(alpha = 0.86f),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .border(1.dp, BrassGold.copy(alpha = 0.55f), RoundedCornerShape(14.dp))
                            .clickable { onToggleTimer() }
                            .testTag("desk_hourglass_timer")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = if (isFocusTimerRunning) Icons.Default.Pause else Icons.Default.HourglassTop,
                                contentDescription = "Toggle study timer",
                                tint = BrassBright,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = "%02d:%02d".format(mins, secs),
                                fontFamily = PlusJakartaSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = ParchmentCream
                            )
                        }
                    }

                    Surface(
                        color = BurgundyLeather,
                        shape = RoundedCornerShape(14.dp),
                        shadowElevation = 4.dp,
                        modifier = Modifier
                            .border(1.dp, BrassGold.copy(alpha = 0.7f), RoundedCornerShape(14.dp))
                            .clickable { onStandUp() }
                            .testTag("stand_up_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = "Stand up from table",
                                tint = ParchmentCream,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = "STAND UP",
                                fontFamily = PlusJakartaSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = ParchmentCream
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            RelationshipBookReader(
                book = activeBook,
                characterName = characterName,
                currentPageIndex = currentPageIndex,
                isBookmarkedOnCurrentPage = isBookmarkedHere,
                lampAmbience = lampAmbience,
                lampBrightness = animatedDeskBrightness,
                liveTopicState = liveTopicState,
                autoLiveGoogleSync = autoLiveGoogleSync,
                onToggleAutoLiveSync = onToggleAutoLiveSync,
                onFetchLiveTopic = onFetchLiveTopic,
                onOpenCompleteTopics = { showCompleteTopicsDialog = true },
                onPageChanged = onPageChanged,
                onToggleBookmark = { onToggleBookmark(currentPageIndex) },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(deskStackBooks, key = { it.bookId }) { item ->
                        val book = LibraryCatalog.findById(item.bookId) ?: return@items
                        val isCurrent = book.id == activeBook.id
                        Surface(
                            color = book.spineColor,
                            shape = RoundedCornerShape(8.dp),
                            shadowElevation = if (isCurrent) 6.dp else 2.dp,
                            modifier = Modifier
                                .border(
                                    width = if (isCurrent) 2.dp else 1.dp,
                                    color = if (isCurrent) BrassBright else Color.White.copy(alpha = 0.25f),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable { onSelectBook(book.id) }
                                .testTag("desk_stack_book_${book.id}")
                        ) {
                            Text(
                                text = book.title,
                                fontFamily = PlusJakartaSansFontFamily,
                                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 11.sp,
                                color = ParchmentCream,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .padding(horizontal = 10.dp, vertical = 7.dp)
                                    .width(96.dp)
                            )
                        }
                    }
                }

                Spacer(Modifier.width(8.dp))

                Surface(
                    color = ParchmentSoft,
                    shape = RoundedCornerShape(12.dp),
                    shadowElevation = 4.dp,
                    modifier = Modifier
                        .border(1.dp, BrassGold, RoundedCornerShape(12.dp))
                        .clickable { showDeskNotepad = true }
                        .testTag("desk_notepad_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.EditNote,
                            contentDescription = "Open desk margin notepad",
                            tint = BurgundyLeather,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "NOTES (${notesForBook.size})",
                            fontFamily = PlusJakartaSansFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = MahoganyDark
                        )
                    }
                }
            }
        }

        if (showDeskNotepad) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x88140C09))
                    .clickable { showDeskNotepad = false }
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    color = ParchmentCream,
                    shape = RoundedCornerShape(18.dp),
                    shadowElevation = 12.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(2.dp, BrassGold, RoundedCornerShape(18.dp))
                        .clickable(enabled = false) {}
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "DESK MARGIN NOTES",
                                fontFamily = PlayfairDisplayFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = BurgundyLeather
                            )
                            IconButton(onClick = { showDeskNotepad = false }) {
                                Icon(Icons.Default.Close, contentDescription = "Close notepad", tint = InkCharcoal)
                            }
                        }
                        Text(
                            text = "${activeBook.title} • PAGE $currentPageIndex",
                            fontFamily = PlusJakartaSansFontFamily,
                            fontSize = 12.sp,
                            color = InkMuted
                        )
                        Spacer(Modifier.height(10.dp))
                        OutlinedTextField(
                            value = noteDraft,
                            onValueChange = { noteDraft = it },
                            placeholder = { Text("WRITE A THOUGHT, QUOTE, OR CONNECTION...") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BurgundyLeather,
                                unfocusedBorderColor = BrassGold,
                                focusedTextColor = InkCharcoal,
                                unfocusedTextColor = InkCharcoal
                            ),
                            modifier = Modifier.fillMaxWidth().testTag("margin_note_input")
                        )
                        Spacer(Modifier.height(8.dp))
                        Button(
                            onClick = {
                                if (noteDraft.isNotBlank()) {
                                    onAddNote(currentPageIndex, noteDraft)
                                    noteDraft = ""
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = BurgundyLeather),
                            modifier = Modifier.align(Alignment.End).testTag("save_margin_note_button")
                        ) {
                            Text("SAVE NOTE", color = ParchmentCream)
                        }
                        Spacer(Modifier.height(10.dp))
                        if (notesForBook.isEmpty()) {
                            Text(
                                text = "NO MARGIN NOTES YET FOR THIS BOOK. REFLECT ON A CHAPTER PROMPT ABOVE!",
                                fontFamily = LoraFontFamily,
                                fontSize = 13.sp,
                                color = InkMuted
                            )
                        } else {
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.height(160.dp)
                            ) {
                                items(notesForBook, key = { it.id }) { note ->
                                    Surface(
                                        color = ParchmentSoft,
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(10.dp)
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = "PAGE ${note.pageNumber}",
                                                    fontFamily = PlusJakartaSansFontFamily,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 11.sp,
                                                    color = BurgundyLeather
                                                )
                                                Text(
                                                    text = note.noteText,
                                                    fontFamily = LoraFontFamily,
                                                    fontSize = 13.sp,
                                                    color = InkCharcoal
                                                )
                                            }
                                            IconButton(onClick = { onDeleteNote(note.id) }) {
                                                Icon(
                                                    Icons.Default.DeleteOutline,
                                                    contentDescription = "Delete note",
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
            }
        }
        if (showCompleteTopicsDialog) {
            CompleteBookTopicsDialog(
                book = activeBook,
                onJumpToPage = { page -> onPageChanged(page) },
                onSearchTopicLive = { topic ->
                    onFetchLiveTopic(topic, activeBook.synopsis, null, false)
                },
                onClose = { showCompleteTopicsDialog = false }
            )
        }
    }
}


@Composable
private fun TeaCupProp(phase: Float, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val drift = sin(phase) * 3f
        drawLine(
            color = Color.White.copy(alpha = 0.45f),
            start = Offset(w * 0.38f, h * 0.26f),
            end = Offset(w * 0.38f + drift, h * 0.06f),
            strokeWidth = 2f
        )
        drawLine(
            color = Color.White.copy(alpha = 0.35f),
            start = Offset(w * 0.58f, h * 0.26f),
            end = Offset(w * 0.58f - drift, h * 0.08f),
            strokeWidth = 2f
        )
        drawOval(
            color = ParchmentSoft,
            topLeft = Offset(w * 0.12f, h * 0.64f),
            size = Size(w * 0.72f, h * 0.24f)
        )
        drawCircle(
            color = ParchmentCream,
            radius = w * 0.24f,
            center = Offset(w * 0.48f, h * 0.56f)
        )
        drawCircle(
            color = Color(0xFF7D3F18),
            radius = w * 0.17f,
            center = Offset(w * 0.48f, h * 0.54f)
        )
    }
}
