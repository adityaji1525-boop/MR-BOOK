package com.example.ui.components

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ai.LiveKnowledgeUiState
import com.example.model.Book
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LiveGoogleKnowledgeCard(
    state: LiveKnowledgeUiState,
    currentTopicLabel: String,
    completeTopics: List<String>,
    autoSyncEnabled: Boolean,
    showInlineTopicSearch: Boolean = true,
    onToggleAutoSync: ((Boolean) -> Unit)? = null,
    onFetchTopic: (topicTitle: String, customQuestion: String?, forceRefresh: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current
    var customTopicInput by remember { mutableStateOf("") }

    Surface(
        color = Color(0xFFF3F8F5),
        shape = RoundedCornerShape(14.dp),
        modifier = modifier
            .fillMaxWidth()
            .border(1.2.dp, EmeraldLibrarian.copy(alpha = 0.55f), RoundedCornerShape(14.dp))
            .testTag("live_google_knowledge_card")
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Header row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        color = EmeraldLibrarian,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.TravelExplore,
                            contentDescription = "Real-time Google Search",
                            tint = Color.White,
                            modifier = Modifier.padding(5.dp).size(15.dp)
                        )
                    }
                    Spacer(Modifier.width(7.dp))
                    Column {
                        Text(
                            text = "LIVE GOOGLE SEARCH KNOWLEDGE",
                            fontFamily = PlusJakartaSansFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.5.sp,
                            letterSpacing = 0.8.sp,
                            color = EmeraldLibrarian
                        )
                        Text(
                            text = "REAL-TIME WEB-GROUNDED VERIFICATION",
                            fontFamily = PlusJakartaSansFontFamily,
                            fontSize = 9.sp,
                            color = InkMuted
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    if (onToggleAutoSync != null) {
                        Surface(
                            color = if (autoSyncEnabled) EmeraldLibrarian.copy(alpha = 0.16f) else ParchmentSoft,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .border(
                                    1.dp,
                                    if (autoSyncEnabled) EmeraldLibrarian else InkMuted.copy(alpha = 0.4f),
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { onToggleAutoSync(!autoSyncEnabled) }
                                .testTag("live_google_auto_sync_toggle")
                        ) {
                            Text(
                                text = if (autoSyncEnabled) "AUTO ON" else "AUTO OFF",
                                fontFamily = PlusJakartaSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.5.sp,
                                color = if (autoSyncEnabled) EmeraldLibrarian else InkMuted,
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Surface(
                        color = ParchmentCream,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .border(1.dp, EmeraldLibrarian.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                            .clickable {
                                onFetchTopic(currentTopicLabel, null, true)
                            }
                            .testTag("live_google_refresh_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Refresh from Google Search",
                                tint = EmeraldLibrarian,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(Modifier.width(3.dp))
                            Text(
                                text = "SYNC",
                                fontFamily = PlusJakartaSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.5.sp,
                                color = EmeraldLibrarian
                            )
                        }
                    }
                }
            }

            if (showInlineTopicSearch) {
                Spacer(Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = customTopicInput,
                        onValueChange = { customTopicInput = it },
                        placeholder = {
                            Text(
                                "SEARCH ANY BOOK TOPIC LIVE ON GOOGLE...",
                                fontSize = 11.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EmeraldLibrarian,
                            unfocusedBorderColor = BrassGold.copy(alpha = 0.8f),
                            focusedTextColor = InkCharcoal,
                            unfocusedTextColor = InkCharcoal,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("live_book_topic_input")
                    )
                    Button(
                        onClick = {
                            val q = customTopicInput.trim()
                            if (q.isNotEmpty()) {
                                onFetchTopic(q, q, true)
                            }
                        },
                        enabled = customTopicInput.isNotBlank() && state !is LiveKnowledgeUiState.Loading,
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldLibrarian),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .height(48.dp)
                            .testTag("live_book_topic_search_btn")
                    ) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search topic on Google",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            if (completeTopics.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "EXPLORE ANY REAL BOOK TOPIC LIVE:",
                    fontFamily = PlusJakartaSansFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 9.5.sp,
                    letterSpacing = 0.6.sp,
                    color = InkMuted
                )
                Spacer(Modifier.height(4.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    completeTopics.take(6).forEach { topic ->
                        val activeTopic = (state as? LiveKnowledgeUiState.Success)?.result?.topicTitle
                        val isCurrent = activeTopic == topic
                        Surface(
                            color = if (isCurrent) EmeraldLibrarian else ParchmentCream,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .border(
                                    1.dp,
                                    if (isCurrent) EmeraldLibrarian else BrassGold.copy(alpha = 0.6f),
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable {
                                    customTopicInput = ""
                                    onFetchTopic(topic, null, false)
                                }
                        ) {
                            Text(
                                text = topic.uppercase(),
                                fontFamily = PlusJakartaSansFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 9.5.sp,
                                color = if (isCurrent) Color.White else MahoganyDark,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(10.dp))
            HorizontalDivider(color = EmeraldLibrarian.copy(alpha = 0.25f))
            Spacer(Modifier.height(10.dp))

            when (state) {
                LiveKnowledgeUiState.Idle -> {
                    Button(
                        onClick = { onFetchTopic(currentTopicLabel, null, false) },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldLibrarian),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("live_google_connect_button")
                    ) {
                        Icon(Icons.Default.Public, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "LOAD REAL-TIME GOOGLE SEARCH BRIEFING",
                            fontFamily = PlusJakartaSansFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = Color.White
                        )
                    }
                }

                is LiveKnowledgeUiState.Loading -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        CircularProgressIndicator(
                            color = EmeraldLibrarian,
                            strokeWidth = 2.5.dp,
                            modifier = Modifier.size(20.dp)
                        )
                        Column {
                            Text(
                                text = "QUERYING LIVE GOOGLE SEARCH...",
                                fontFamily = PlusJakartaSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = EmeraldLibrarian
                            )
                            Text(
                                text = state.topicTitle,
                                fontFamily = LoraFontFamily,
                                fontStyle = FontStyle.Italic,
                                fontSize = 12.5.sp,
                                color = InkCharcoal
                            )
                        }
                    }
                }

                is LiveKnowledgeUiState.Error -> {
                    Surface(
                        color = Color(0xFFFDF0ED),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, BurgundyLeather.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "LIVE GOOGLE SEARCH UNAVAILABLE",
                                fontFamily = PlusJakartaSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = BurgundyLeather
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = state.message,
                                fontFamily = PlusJakartaSansFontFamily,
                                fontSize = 11.5.sp,
                                color = InkCharcoal
                            )
                            Spacer(Modifier.height(8.dp))
                            Button(
                                onClick = { onFetchTopic(state.topicTitle.ifBlank { currentTopicLabel }, null, true) },
                                colors = ButtonDefaults.buttonColors(containerColor = BurgundyLeather),
                                modifier = Modifier.height(36.dp)
                            ) {
                                Text("RETRY LIVE GOOGLE SEARCH", fontSize = 10.5.sp, color = ParchmentCream)
                            }
                        }
                    }
                }

                is LiveKnowledgeUiState.Success -> {
                    val result = state.result
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = result.topicTitle,
                            fontFamily = PlayfairDisplayFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.5.sp,
                            color = MahoganyDark,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(Modifier.width(6.dp))
                        Surface(
                            color = EmeraldLibrarian.copy(alpha = 0.14f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "LIVE • ${result.fetchedAtLabel.uppercase()}",
                                fontFamily = PlusJakartaSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp,
                                color = EmeraldLibrarian,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = result.answerText,
                        fontFamily = LoraFontFamily,
                        fontSize = 13.5.sp,
                        lineHeight = 21.sp,
                        color = InkCharcoal
                    )

                    if (result.webSearchQueries.isNotEmpty()) {
                        Spacer(Modifier.height(10.dp))
                        Text(
                            text = "REAL-TIME GOOGLE SEARCH QUERIES USED:",
                            fontFamily = PlusJakartaSansFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.5.sp,
                            letterSpacing = 0.6.sp,
                            color = EmeraldLibrarian
                        )
                        Spacer(Modifier.height(4.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            result.webSearchQueries.forEach { q ->
                                Surface(
                                    color = Color.White,
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .border(1.dp, EmeraldLibrarian.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                                        .clickable {
                                            uriHandler.openUri("https://www.google.com/search?q=${Uri.encode(q)}")
                                        }
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Search,
                                            contentDescription = null,
                                            tint = EmeraldLibrarian,
                                            modifier = Modifier.size(11.dp)
                                        )
                                        Spacer(Modifier.width(3.dp))
                                        Text(
                                            text = q,
                                            fontFamily = PlusJakartaSansFontFamily,
                                            fontSize = 10.5.sp,
                                            color = InkCharcoal
                                        )
                                    }
                                }
                            }
                        }
                    }

                    if (result.sources.isNotEmpty()) {
                        Spacer(Modifier.height(10.dp))
                        Text(
                            text = "VERIFIED GOOGLE SEARCH WEB SOURCES (${result.sources.size}):",
                            fontFamily = PlusJakartaSansFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.5.sp,
                            letterSpacing = 0.6.sp,
                            color = BurgundyLeather
                        )
                        Spacer(Modifier.height(4.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                            result.sources.take(6).forEach { source ->
                                Surface(
                                    color = Color.White,
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(1.dp, BrassGold.copy(alpha = 0.55f), RoundedCornerShape(8.dp))
                                        .clickable { uriHandler.openUri(source.uri) }
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Public,
                                            contentDescription = null,
                                            tint = EmeraldLibrarian,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(Modifier.width(7.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = source.title,
                                                fontFamily = PlusJakartaSansFontFamily,
                                                fontWeight = FontWeight.SemiBold,
                                                fontSize = 11.5.sp,
                                                color = InkCharcoal,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Text(
                                                text = source.domain,
                                                fontFamily = PlusJakartaSansFontFamily,
                                                fontSize = 9.5.sp,
                                                color = InkMuted,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                        Icon(
                                            Icons.AutoMirrored.Filled.OpenInNew,
                                            contentDescription = "Open source",
                                            tint = BurgundyLeather,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    if (!result.searchEntryPointHtml.isNullOrBlank()) {
                        Spacer(Modifier.height(8.dp))
                        GoogleSearchEntryPointStrip(htmlContent = result.searchEntryPointHtml)
                    }
                }
            }
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun GoogleSearchEntryPointStrip(htmlContent: String) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .border(1.dp, ParchmentAged, RoundedCornerShape(8.dp))
            .heightIn(min = 42.dp, max = 96.dp)
    ) {
        AndroidView(
            factory = { ctx ->
                WebView(ctx).apply {
                    settings.javaScriptEnabled = false
                    setBackgroundColor(android.graphics.Color.TRANSPARENT)
                    webViewClient = object : WebViewClient() {
                        override fun shouldOverrideUrlLoading(
                            view: WebView?,
                            request: WebResourceRequest?
                        ): Boolean {
                            val targetUri = request?.url ?: return false
                            runCatching {
                                val intent = Intent(Intent.ACTION_VIEW, targetUri)
                                context.startActivity(intent)
                            }
                            return true
                        }
                    }
                }
            },
            update = { webView ->
                webView.loadDataWithBaseURL(
                    "https://www.google.com/",
                    htmlContent,
                    "text/html",
                    "UTF-8",
                    null
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun CompleteBookTopicsDialog(
    book: Book,
    onJumpToPage: (Int) -> Unit,
    onSearchTopicLive: (String) -> Unit,
    onClose: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x99160D09))
            .clickable { onClose() }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            color = ParchmentCream,
            shape = RoundedCornerShape(20.dp),
            shadowElevation = 14.dp,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 580.dp)
                .border(2.dp, BrassGold, RoundedCornerShape(20.dp))
                .clickable(enabled = false) {}
                .testTag("complete_topics_dialog")
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "COMPLETE REAL BOOK TOPICS",
                            fontFamily = PlayfairDisplayFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = BurgundyLeather
                        )
                        Text(
                            text = "${book.title.uppercase()} • ${book.level.displayName}",
                            fontFamily = PlusJakartaSansFontFamily,
                            fontSize = 10.5.sp,
                            color = InkMuted,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    IconButton(onClick = onClose) {
                        Icon(Icons.Default.Close, contentDescription = "Close topics index", tint = InkCharcoal)
                    }
                }

                if (book.realSubjectScope.isNotBlank()) {
                    Spacer(Modifier.height(6.dp))
                    Surface(
                        color = ParchmentSoft,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, ParchmentAged, RoundedCornerShape(10.dp))
                    ) {
                        Text(
                            text = book.realSubjectScope,
                            fontFamily = LoraFontFamily,
                            fontStyle = FontStyle.Italic,
                            fontSize = 12.sp,
                            color = MahoganyDark,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }

                Spacer(Modifier.height(10.dp))
                Text(
                    text = "CHAPTERS IN VOLUME (${book.pages.size}) & COMPLETE SUBJECT CURRICULUM (${book.completeTopics.size})",
                    fontFamily = PlusJakartaSansFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    color = InkMuted
                )
                Spacer(Modifier.height(6.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    itemsIndexed(book.pages) { idx, page ->
                        val pageNum = idx + 1
                        Surface(
                            color = ParchmentSoft,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, BrassGold.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                                .clickable {
                                    onJumpToPage(pageNum)
                                    onClose()
                                }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(10.dp)
                            ) {
                                Icon(
                                    Icons.Default.MenuBook,
                                    contentDescription = null,
                                    tint = BurgundyLeather,
                                    modifier = Modifier.size(17.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "CHAPTER PAGE $pageNum",
                                        fontFamily = PlusJakartaSansFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 9.5.sp,
                                        color = BurgundyLeather
                                    )
                                    Text(
                                        text = page.chapterTitle,
                                        fontFamily = PlayfairDisplayFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = InkCharcoal
                                    )
                                }
                                Surface(
                                    color = EmeraldLibrarian,
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.clickable {
                                        onJumpToPage(pageNum)
                                        onSearchTopicLive(page.chapterTitle)
                                        onClose()
                                    }
                                ) {
                                    Text(
                                        text = "LIVE INFO",
                                        fontFamily = PlusJakartaSansFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 9.5.sp,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                                    )
                                }
                            }
                        }
                    }

                    itemsIndexed(book.completeTopics) { idx, topic ->
                        Surface(
                            color = Color(0xFFF3F8F5),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, EmeraldLibrarian.copy(alpha = 0.45f), RoundedCornerShape(12.dp))
                                .clickable {
                                    if (book.pages.isNotEmpty()) {
                                        onJumpToPage((idx % book.pages.size) + 1)
                                    }
                                    onSearchTopicLive(topic)
                                    onClose()
                                }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(10.dp)
                            ) {
                                Icon(
                                    Icons.Default.TravelExplore,
                                    contentDescription = null,
                                    tint = EmeraldLibrarian,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "REAL BOOK TOPIC ${idx + 1}",
                                        fontFamily = PlusJakartaSansFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 9.sp,
                                        color = EmeraldLibrarian
                                    )
                                    Text(
                                        text = topic,
                                        fontFamily = LoraFontFamily,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 13.sp,
                                        color = InkCharcoal
                                    )
                                }
                                Text(
                                    text = "GOOGLE",
                                    fontFamily = PlusJakartaSansFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    color = EmeraldLibrarian
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
