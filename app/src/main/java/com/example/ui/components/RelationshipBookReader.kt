package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.TravelExplore
import com.example.ai.LiveKnowledgeUiState
import com.example.model.Book
import com.example.model.BookCategory
import com.example.model.LampAmbience
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
import kotlinx.coroutines.launch

@Composable
fun RelationshipBookReader(
    book: Book,
    characterName: String,
    currentPageIndex: Int,
    isBookmarkedOnCurrentPage: Boolean,
    lampAmbience: LampAmbience,
    lampBrightness: Float,
    liveTopicState: LiveKnowledgeUiState,
    autoLiveGoogleSync: Boolean,
    onToggleAutoLiveSync: (Boolean) -> Unit,
    onFetchLiveTopic: (topicTitle: String, chapterContext: String, customQuestion: String?, forceRefresh: Boolean) -> Unit,
    onOpenCompleteTopics: () -> Unit,
    onPageChanged: (Int) -> Unit,
    onToggleBookmark: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalPages = book.pages.size
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    val flipProgress = remember { Animatable(0f) }
    var isFlippingForward by remember { mutableStateOf(true) }
    var fromPageIndex by remember(book.id) { mutableIntStateOf(currentPageIndex) }
    var dragAccumulated by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(book.id) {
        fromPageIndex = currentPageIndex
        flipProgress.snapTo(0f)
    }

    LaunchedEffect(book.id, currentPageIndex, autoLiveGoogleSync) {
        if (currentPageIndex in 1..totalPages && autoLiveGoogleSync) {
            val page = book.pages[currentPageIndex - 1]
            onFetchLiveTopic(page.chapterTitle, page.body, null, false)
        }
    }

    fun triggerPageTurn(targetPage: Int) {
        if (targetPage !in 0..totalPages || targetPage == currentPageIndex) return
        val forward = targetPage > currentPageIndex
        isFlippingForward = forward
        fromPageIndex = currentPageIndex
        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        onPageChanged(targetPage)
        scope.launch {
            flipProgress.snapTo(0f)
            flipProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 440, easing = FastOutSlowInEasing)
            )
            fromPageIndex = targetPage
            flipProgress.snapTo(0f)
        }
    }

    val paperBase = lampAmbience.pageTint
    val warmOverlayAlpha = (1f - lampBrightness) * 0.25f

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(18.dp, RoundedCornerShape(18.dp))
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(book.spineColor, BurgundyLeather, MahoganyDark)
                )
            )
            .border(2.5.dp, book.coverTrimColor, RoundedCornerShape(18.dp))
            .padding(10.dp)
            .pointerInput(currentPageIndex, book.id) {
                detectHorizontalDragGestures(
                    onDragStart = { dragAccumulated = 0f },
                    onHorizontalDrag = { _, dragAmount -> dragAccumulated += dragAmount },
                    onDragEnd = {
                        if (dragAccumulated < -48f && currentPageIndex < totalPages) {
                            triggerPageTurn(currentPageIndex + 1)
                        } else if (dragAccumulated > 48f && currentPageIndex > 0) {
                            triggerPageTurn(currentPageIndex - 1)
                        }
                        dragAccumulated = 0f
                    }
                )
            }
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            drawRoundRect(
                color = BrassGold.copy(alpha = 0.55f),
                topLeft = Offset(4f, 4f),
                size = androidx.compose.ui.geometry.Size(size.width - 8f, size.height - 8f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(14f, 14f),
                style = androidx.compose.ui.graphics.drawscope.Stroke(
                    width = 1.5f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 8f))
                )
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(12.dp))
                .background(ParchmentAged)
                .padding(horizontal = 5.dp, vertical = 3.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(10.dp))
                    .background(paperBase)
            ) {
                BookPageContent(
                    book = book,
                    characterName = characterName,
                    pageIndex = currentPageIndex,
                    totalPages = totalPages,
                    isBookmarked = isBookmarkedOnCurrentPage,
                    liveTopicState = liveTopicState,
                    autoLiveGoogleSync = autoLiveGoogleSync,
                    onToggleAutoLiveSync = onToggleAutoLiveSync,
                    onFetchLiveTopic = onFetchLiveTopic,
                    onOpenCompleteTopics = onOpenCompleteTopics,
                    onJumpToPage = { target -> triggerPageTurn(target) },
                    onToggleBookmark = onToggleBookmark,
                    onPrevious = { triggerPageTurn(currentPageIndex - 1) },
                    onNext = { triggerPageTurn(currentPageIndex + 1) }
                )

                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(18.dp)
                        .align(Alignment.CenterStart)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(Color(0x28231C18), Color.Transparent)
                            )
                        )
                )
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(12.dp)
                        .align(Alignment.CenterEnd)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(Color.Transparent, Color(0x22231C18))
                            )
                        )
                )
            }

            val progress = flipProgress.value
            if (progress > 0f && progress < 1f) {
                val angle = if (isFlippingForward) {
                    -105f * progress
                } else {
                    105f * (1f - progress)
                }
                val pivotX = if (isFlippingForward) 0f else 1f
                val curlShadow = kotlin.math.sin(progress * Math.PI).toFloat() * 0.35f

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            cameraDistance = 18f * density.density
                            transformOrigin = TransformOrigin(pivotX, 0.5f)
                            rotationY = angle
                        }
                        .clip(RoundedCornerShape(10.dp))
                        .background(paperBase)
                ) {
                    BookPageContent(
                        book = book,
                        characterName = characterName,
                        pageIndex = fromPageIndex,
                        totalPages = totalPages,
                        isBookmarked = false,
                        liveTopicState = LiveKnowledgeUiState.Idle,
                        autoLiveGoogleSync = false,
                        onToggleAutoLiveSync = {},
                        onFetchLiveTopic = { _, _, _, _ -> },
                        onOpenCompleteTopics = {},
                        onJumpToPage = {},
                        onToggleBookmark = {},
                        onPrevious = {},
                        onNext = {}
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = curlShadow * 0.6f),
                                        Color.White.copy(alpha = curlShadow * 0.25f),
                                        Color.Black.copy(alpha = curlShadow)
                                    )
                                )
                            )
                    )
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 18.dp)
                    .clickable { onToggleBookmark() }
                    .testTag("book_ribbon_bookmark")
            ) {
                Surface(
                    color = if (isBookmarkedOnCurrentPage) BurgundyLeather else Color(0xFFB39B84),
                    shape = RoundedCornerShape(bottomStart = 4.dp, bottomEnd = 4.dp),
                    shadowElevation = 4.dp,
                    modifier = Modifier.size(width = 24.dp, height = if (isBookmarkedOnCurrentPage) 44.dp else 28.dp)
                ) {
                    Icon(
                        imageVector = if (isBookmarkedOnCurrentPage) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Toggle satin bookmark",
                        tint = ParchmentCream,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }

            if (warmOverlayAlpha > 0.01f) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = warmOverlayAlpha))
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BookPageContent(
    book: Book,
    characterName: String,
    pageIndex: Int,
    totalPages: Int,
    isBookmarked: Boolean,
    liveTopicState: LiveKnowledgeUiState,
    autoLiveGoogleSync: Boolean,
    onToggleAutoLiveSync: (Boolean) -> Unit,
    onFetchLiveTopic: (topicTitle: String, chapterContext: String, customQuestion: String?, forceRefresh: Boolean) -> Unit,
    onOpenCompleteTopics: () -> Unit,
    onJumpToPage: (Int) -> Unit,
    onToggleBookmark: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp, vertical = 14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(end = 28.dp)
        ) {
            Text(
                text = "SHELF #${book.shelfNumber} • ${book.level.displayName.uppercase()}",
                fontFamily = PlusJakartaSansFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                letterSpacing = 1.sp,
                color = book.category.accentColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = if (pageIndex == 0) "EX LIBRIS" else "PAGE $pageIndex OF $totalPages",
                fontFamily = PlusJakartaSansFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 11.sp,
                color = InkMuted
            )
        }

        Spacer(Modifier.height(6.dp))
        HorizontalDivider(color = BrassGold.copy(alpha = 0.45f), thickness = 1.dp)

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(vertical = 12.dp)
        ) {
            if (pageIndex == 0) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth().padding(top = 6.dp)
                ) {
                    Surface(
                        color = book.category.accentColor.copy(alpha = 0.14f),
                        shape = CircleShape
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = BurgundyLeather,
                            modifier = Modifier.padding(10.dp).size(22.dp)
                        )
                    }
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = book.title,
                        fontFamily = PlayfairDisplayFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        lineHeight = 30.sp,
                        color = InkCharcoal,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "by ${book.author}",
                        fontFamily = LoraFontFamily,
                        fontStyle = FontStyle.Italic,
                        fontSize = 14.sp,
                        color = InkMuted
                    )
                    Spacer(Modifier.height(14.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .border(1.dp, BrassGold.copy(alpha = 0.6f), RoundedCornerShape(10.dp))
                            .background(Color(0x12D4A03E))
                            .padding(12.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "KEEPSAKE EX LIBRIS",
                                fontFamily = PlusJakartaSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                letterSpacing = 1.2.sp,
                                color = BurgundyLeather
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = "Opened at the study table by $characterName",
                                fontFamily = LoraFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                color = InkCharcoal,
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(6.dp))
                            Text(
                                text = "“${book.dedication}”",
                                fontFamily = LoraFontFamily,
                                fontStyle = FontStyle.Italic,
                                fontSize = 13.sp,
                                color = InkMuted,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = book.synopsis,
                        fontFamily = LoraFontFamily,
                        fontSize = 14.sp,
                        lineHeight = 22.sp,
                        color = InkCharcoal,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(10.dp))
                    if (book.completeTopics.isNotEmpty()) {
                        Surface(
                            color = Color(0xFFF3F8F5),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, EmeraldLibrarian.copy(alpha = 0.45f), RoundedCornerShape(12.dp))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        Icons.Default.TravelExplore,
                                        contentDescription = null,
                                        tint = EmeraldLibrarian,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "COMPLETE REAL BOOK TOPICS (${book.completeTopics.size}) • LIVE GOOGLE SEARCH",
                                        fontFamily = PlusJakartaSansFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp,
                                        color = EmeraldLibrarian
                                    )
                                }
                                if (book.realSubjectScope.isNotBlank()) {
                                    Spacer(Modifier.height(4.dp))
                                    Text(
                                        text = book.realSubjectScope,
                                        fontFamily = LoraFontFamily,
                                        fontStyle = FontStyle.Italic,
                                        fontSize = 12.sp,
                                        color = InkMuted
                                    )
                                }
                                Spacer(Modifier.height(8.dp))
                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                                    verticalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    book.completeTopics.forEachIndexed { idx, topic ->
                                        Surface(
                                            color = ParchmentCream,
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier
                                                .border(1.dp, BrassGold.copy(alpha = 0.65f), RoundedCornerShape(8.dp))
                                                .clickable {
                                                    val targetPage = if (totalPages > 0) (idx % totalPages) + 1 else 1
                                                    onJumpToPage(targetPage)
                                                    onFetchLiveTopic(topic, book.synopsis, null, false)
                                                }
                                        ) {
                                            Text(
                                                text = topic,
                                                fontFamily = PlusJakartaSansFontFamily,
                                                fontSize = 10.5.sp,
                                                color = MahoganyDark,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        Spacer(Modifier.height(10.dp))
                    }
                    Text(
                        text = "SWIPE LEFT OR TAP OPEN TO TURN THE FIRST LEAF.",
                        fontFamily = PlusJakartaSansFontFamily,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = book.category.accentColor
                    )
                }
            } else {
                val page = book.pages[(pageIndex - 1).coerceIn(0, book.pages.lastIndex)]
                Text(
                    text = page.chapterTitle,
                    fontFamily = PlayfairDisplayFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    lineHeight = 26.sp,
                    color = BurgundyLeather
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    text = page.body,
                    fontFamily = LoraFontFamily,
                    fontSize = 15.5.sp,
                    lineHeight = 25.sp,
                    color = InkCharcoal
                )

                if (page.illustrationNote != null) {
                    Spacer(Modifier.height(14.dp))
                    Surface(
                        color = book.category.accentColor.copy(alpha = 0.09f),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, book.category.accentColor.copy(alpha = 0.35f), RoundedCornerShape(10.dp))
                    ) {
                        Text(
                            text = page.illustrationNote,
                            fontFamily = PlusJakartaSansFontFamily,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = InkCharcoal,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }

                Spacer(Modifier.height(14.dp))
                LiveGoogleKnowledgeCard(
                    state = liveTopicState,
                    currentTopicLabel = page.chapterTitle,
                    completeTopics = book.completeTopics,
                    autoSyncEnabled = autoLiveGoogleSync,
                    onToggleAutoSync = onToggleAutoLiveSync,
                    onFetchTopic = { topic, customQ, force ->
                        onFetchLiveTopic(topic, page.body, customQ, force)
                    }
                )

                // CHAPTER END MOTIVATION LINE IN BIG BRACKETS WITH DUAL INVERTED COMMAS
                Spacer(Modifier.height(16.dp))
                ChapterMotivationBracket(
                    quote = chapterMotivationFor(book.category, pageIndex)
                )

                Spacer(Modifier.height(12.dp))
                Surface(
                    color = Color(0x166B1D2F),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "PERSONAL CONNECTION PROMPT",
                            fontFamily = PlusJakartaSansFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            letterSpacing = 0.8.sp,
                            color = BurgundyLeather
                        )
                        Spacer(Modifier.height(3.dp))
                        Text(
                            text = page.reflectionPrompt,
                            fontFamily = LoraFontFamily,
                            fontStyle = FontStyle.Italic,
                            fontSize = 13.sp,
                            lineHeight = 19.sp,
                            color = InkMuted
                        )
                    }
                }
            }
        }

        HorizontalDivider(color = BrassGold.copy(alpha = 0.45f), thickness = 1.dp)
        Spacer(Modifier.height(6.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Surface(
                color = if (pageIndex > 0) ParchmentAged else Color.Transparent,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .clickable(enabled = pageIndex > 0) { onPrevious() }
                    .testTag("prev_page_button")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Previous page",
                        tint = if (pageIndex > 0) InkCharcoal else InkMuted.copy(alpha = 0.35f),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "PREV",
                        fontFamily = PlusJakartaSansFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = if (pageIndex > 0) InkCharcoal else InkMuted.copy(alpha = 0.35f)
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Surface(
                    color = ParchmentSoft,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .border(1.dp, BrassGold.copy(alpha = 0.7f), RoundedCornerShape(8.dp))
                        .clickable { onOpenCompleteTopics() }
                        .testTag("open_complete_topics_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FormatListBulleted,
                            contentDescription = "All book topics",
                            tint = EmeraldLibrarian,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "TOPICS (${book.completeTopics.size.coerceAtLeast(book.pages.size)})",
                            fontFamily = PlusJakartaSansFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = MahoganyDark
                        )
                    }
                }

                IconButton(
                    onClick = onToggleBookmark,
                    modifier = Modifier.size(34.dp)
                ) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Bookmark page",
                        tint = if (isBookmarked) BurgundyLeather else InkMuted
                    )
                }
            }

            Surface(
                color = if (pageIndex < totalPages) BurgundyLeather else ParchmentAged,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .clickable(enabled = pageIndex < totalPages) { onNext() }
                    .testTag("next_page_button")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = if (pageIndex == 0) "OPEN" else if (pageIndex == totalPages) "FINISHED" else "TURN PAGE",
                        fontFamily = PlusJakartaSansFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = if (pageIndex < totalPages) ParchmentCream else InkMuted
                    )
                    Spacer(Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Next page",
                        tint = if (pageIndex < totalPages) ParchmentCream else InkMuted,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ChapterMotivationBracket(quote: String) {
    Surface(
        color = Color(0x1CD4A03E),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BrassGold.copy(alpha = 0.65f), RoundedCornerShape(12.dp))
            .testTag("chapter_motivation_bracket")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp)
        ) {
            Text(
                text = "[",
                fontFamily = PlayfairDisplayFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 34.sp,
                color = BurgundyLeather
            )
            Text(
                text = "“$quote”",
                fontFamily = LoraFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontStyle = FontStyle.Italic,
                fontSize = 13.5.sp,
                lineHeight = 20.sp,
                color = MahoganyDark,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            )
            Text(
                text = "]",
                fontFamily = PlayfairDisplayFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 34.sp,
                color = BurgundyLeather
            )
        }
    }
}

private fun chapterMotivationFor(category: BookCategory, pageIndex: Int): String {
    val quotes = when (category) {
        BookCategory.SCIENCE -> listOf(
            "Every small question asked under lamplight opens a window onto the cosmos.",
            "Patience turns scattered clues into clear laws of nature.",
            "Stay curious—today's wonder becomes tomorrow's discovery.",
            "Truth rewards the observer who looks twice with humility."
        )
        BookCategory.TECHNOLOGY -> listOf(
            "Every great system began as one clear line written with patience.",
            "Bugs are just stepping stones guiding you toward mastery.",
            "Build tools with care, and your craft will uplift countless minds.",
            "Clarity of thought always outlasts complexity."
        )
        BookCategory.LITERATURE -> listOf(
            "Words kept alive by thoughtful readers turn pages into lifelong companions.",
            "Read slowly enough to hear the heartbeat between every line.",
            "A well-loved book teaches us how to understand one another more deeply.",
            "Every chapter you finish leaves a quiet lantern inside your memory."
        )
        BookCategory.STORY -> listOf(
            "Courage grows whenever you step past the edge of the familiar page.",
            "Small kindnesses rewrite entire worlds one chapter at a time.",
            "Keep your imagination lit—no storm can dim a hopeful story.",
            "Even quiet beginnings lead to unforgettable horizons."
        )
        BookCategory.COMIC -> listOf(
            "Imagination turns blank paper into motion, color, and unstoppable spirit!",
            "Between every panel lies space for your own bold leap forward.",
            "Sketch your dreams bravely—every hero starts in rough pencil.",
            "Energy and heart turn ordinary moments into legend."
        )
        BookCategory.MATHEMATICS -> listOf(
            "Behind every complex problem hides a calm, elegant pattern waiting for you.",
            "Step-by-step reasoning turns the impossible into plain daylight.",
            "Precision is not cold—it is beauty made trustworthy.",
            "Never fear a tough equation; every proof yields to steady persistence."
        )
        BookCategory.HISTORY_PHILOSOPHY -> listOf(
            "Wisdom belongs to those who listen across centuries before acting today.",
            "Character is sculpted quietly by the daily habits you choose.",
            "Honor the past by planting something generous for the future.",
            "A calm, examined mind remains free in every season."
        )
    }
    return quotes[(pageIndex - 1).coerceAtLeast(0) % quotes.size]
}
