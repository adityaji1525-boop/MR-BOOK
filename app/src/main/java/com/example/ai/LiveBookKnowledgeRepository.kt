package com.example.ai

import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import com.example.model.Book
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.Tool
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.ConcurrentHashMap
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout
import org.json.JSONArray
import org.json.JSONObject

private const val TAG = "LiveBookKnowledgeRepo"
private const val PREF_CACHE_PREFIX = "live_topic_cache_v1_"

data class GroundedSource(
    val title: String,
    val uri: String,
    val domain: String
)

data class LiveBookKnowledgeResult(
    val queryKey: String,
    val topicTitle: String,
    val answerText: String,
    val webSearchQueries: List<String>,
    val sources: List<GroundedSource>,
    val searchEntryPointHtml: String?,
    val fetchedAtLabel: String
)

sealed interface LiveKnowledgeUiState {
    data object Idle : LiveKnowledgeUiState
    data class Loading(val topicTitle: String) : LiveKnowledgeUiState
    data class Success(val result: LiveBookKnowledgeResult) : LiveKnowledgeUiState
    data class Error(val topicTitle: String, val message: String) : LiveKnowledgeUiState
}

class LiveBookKnowledgeRepository(
    private val prefs: SharedPreferences? = null
) {
    private val groundedModel by lazy {
        Firebase.ai(backend = GenerativeBackend.googleAI()).generativeModel(
            modelName = "gemini-3.8-flash",
            tools = listOf(Tool.googleSearch())
        )
    }

    private val memoryCache = ConcurrentHashMap<String, LiveBookKnowledgeResult>()

    fun getCachedTopic(bookId: String, topicTitle: String): LiveBookKnowledgeResult? {
        val key = buildCacheKey(bookId, topicTitle)
        memoryCache[key]?.let { return it }
        val raw = prefs?.getString("$PREF_CACHE_PREFIX$key", null) ?: return null
        return decodeFromJson(raw)?.also { memoryCache[key] = it }
    }

    suspend fun fetchTopicKnowledge(
        book: Book,
        topicTitle: String,
        chapterContext: String = "",
        customQuestion: String? = null,
        forceRefresh: Boolean = false
    ): LiveKnowledgeUiState {
        val isCustom = !customQuestion.isNullOrBlank()
        val effectiveTopic = customQuestion?.trim()?.takeIf { it.isNotBlank() } ?: topicTitle
        val cacheKey = buildCacheKey(book.id, effectiveTopic)

        if (!isCustom && !forceRefresh) {
            getCachedTopic(book.id, effectiveTopic)?.let { cached ->
                return LiveKnowledgeUiState.Success(cached)
            }
        }

        val allTopicsOutline = if (book.completeTopics.isNotEmpty()) {
            book.completeTopics.joinToString(", ")
        } else {
            book.pages.joinToString(", ") { it.chapterTitle }
        }

        val prompt = buildString {
            appendLine("Use Google Search to retrieve accurate, verified, up-to-date real-world textbook and reference knowledge.")
            appendLine("Book Title: ${book.title}")
            appendLine("Author / Reference Domain: ${book.author}")
            appendLine("Subject Category: ${book.category.displayName} | Education Level: ${book.level.displayName}")
            if (book.realSubjectScope.isNotBlank()) {
                appendLine("Real Book Scope: ${book.realSubjectScope}")
            }
            appendLine("Complete Topics Covered in Book: $allTopicsOutline")
            if (isCustom) {
                appendLine("Reader Question / Specific Topic Request: $effectiveTopic")
            } else {
                appendLine("Requested Topic / Chapter: $effectiveTopic")
            }
            if (chapterContext.isNotBlank()) {
                appendLine("Current Page Summary Context: ${chapterContext.take(450)}")
            }
            appendLine()
            appendLine("Provide a thorough, complete, factual explanation suitable for ${book.level.displayName} readers:")
            appendLine("1. Core Definition & Foundational Principles (real facts, terminology, laws, or historical context)")
            appendLine("2. Complete Sub-Topics & Mechanics (formulas, equations, mechanisms, literary/structural elements, or code concepts where applicable)")
            appendLine("3. Real-World Examples & Modern / Current Research Updates (grounded in live Google Search)")
            appendLine("4. Common Misconceptions & Key Takeaways")
            appendLine("Write clearly in well-structured plain text paragraphs without markdown code fences or excessive asterisks.")
        }

        return executeGroundedQuery(
            cacheKey = cacheKey,
            topicTitle = effectiveTopic,
            prompt = prompt,
            persistToCache = !isCustom
        )
    }

    suspend fun searchRealWorldBookOrTopic(query: String): LiveKnowledgeUiState {
        val cleanQuery = query.trim()
        if (cleanQuery.isBlank()) {
            return LiveKnowledgeUiState.Error("", "Enter a book title, author, or topic to search with Google.")
        }
        val cacheKey = buildCacheKey("global_search", cleanQuery)
        val prompt = buildString {
            appendLine("Use Google Search to provide complete, real-world information about the book, author, or academic topic below:")
            appendLine("Query: $cleanQuery")
            appendLine()
            appendLine("Include:")
            appendLine("1. What the real book or topic is about (author/origin, field, verified facts)")
            appendLine("2. Complete list of all major chapters, units, or core topics covered in this subject or real book")
            appendLine("3. Detailed explanation of the key concepts and real-world applications")
            appendLine("4. Up-to-date context or latest references from live Google Search")
            appendLine("Write clearly in clean structured plain-text paragraphs.")
        }
        return executeGroundedQuery(
            cacheKey = cacheKey,
            topicTitle = cleanQuery,
            prompt = prompt,
            persistToCache = false
        )
    }

    private suspend fun executeGroundedQuery(
        cacheKey: String,
        topicTitle: String,
        prompt: String,
        persistToCache: Boolean
    ): LiveKnowledgeUiState {
        return try {
            val response = withTimeout(60_000L) {
                groundedModel.generateContent(prompt)
            }
            val text = response.text?.trim().orEmpty()
            if (text.isBlank()) {
                return LiveKnowledgeUiState.Error(
                    topicTitle = topicTitle,
                    message = "Google Search returned an empty response. Please try again."
                )
            }

            val grounding = response.candidates.firstOrNull()?.groundingMetadata
            val searchQueries = grounding?.webSearchQueries.orEmpty()
                .map { it.trim() }
                .filter { it.isNotBlank() }
                .distinct()

            val sources = grounding?.groundingChunks.orEmpty()
                .mapNotNull { chunk ->
                    val web = chunk.web
                    val uri = web?.uri?.trim().orEmpty()
                    val title = web?.title?.trim().orEmpty()
                    if (uri.isNotBlank()) {
                        val domain = runCatching { Uri.parse(uri).host.orEmpty() }
                            .getOrDefault("")
                            .removePrefix("www.")
                        GroundedSource(
                            title = title.ifBlank { domain.ifBlank { "Google Search Source" } },
                            uri = uri,
                            domain = domain.ifBlank { "web" }
                        )
                    } else {
                        null
                    }
                }
                .distinctBy { it.uri }

            val entryPointHtml = grounding?.searchEntryPoint?.renderedContent?.takeIf { it.isNotBlank() }
            val timestamp = SimpleDateFormat("HH:mm, MMM d", Locale.getDefault()).format(Date())

            val result = LiveBookKnowledgeResult(
                queryKey = cacheKey,
                topicTitle = topicTitle,
                answerText = text,
                webSearchQueries = searchQueries,
                sources = sources,
                searchEntryPointHtml = entryPointHtml,
                fetchedAtLabel = timestamp
            )

            if (persistToCache) {
                memoryCache[cacheKey] = result
                prefs?.edit()?.putString("$PREF_CACHE_PREFIX$cacheKey", encodeToJson(result))?.apply()
            }

            LiveKnowledgeUiState.Success(result)
        } catch (e: TimeoutCancellationException) {
            Log.e(TAG, "Live Google Search timed out for $topicTitle", e)
            LiveKnowledgeUiState.Error(
                topicTitle = topicTitle,
                message = "Live Google Search timed out after 60 seconds. Check your connection and retry."
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Live Google Search failed for $topicTitle", e)
            LiveKnowledgeUiState.Error(
                topicTitle = topicTitle,
                message = e.localizedMessage ?: "Live Google Search failed. Please retry."
            )
        }
    }

    private fun buildCacheKey(bookId: String, topicTitle: String): String {
        val sanitized = topicTitle.trim().lowercase().replace(Regex("[^a-z0-9]+"), "_")
        return "${bookId}__${sanitized}".take(90)
    }

    private fun encodeToJson(result: LiveBookKnowledgeResult): String {
        val root = JSONObject().apply {
            put("queryKey", result.queryKey)
            put("topicTitle", result.topicTitle)
            put("answerText", result.answerText)
            put("fetchedAtLabel", result.fetchedAtLabel)
            put("searchEntryPointHtml", result.searchEntryPointHtml ?: "")
            put("webSearchQueries", JSONArray(result.webSearchQueries))
            val sourcesArr = JSONArray()
            result.sources.forEach { src ->
                sourcesArr.put(
                    JSONObject().apply {
                        put("title", src.title)
                        put("uri", src.uri)
                        put("domain", src.domain)
                    }
                )
            }
            put("sources", sourcesArr)
        }
        return root.toString()
    }

    private fun decodeFromJson(raw: String): LiveBookKnowledgeResult? {
        return runCatching {
            val root = JSONObject(raw)
            val queriesArr = root.optJSONArray("webSearchQueries") ?: JSONArray()
            val queries = (0 until queriesArr.length()).map { queriesArr.getString(it) }
            val sourcesArr = root.optJSONArray("sources") ?: JSONArray()
            val sources = (0 until sourcesArr.length()).mapNotNull { idx ->
                val obj = sourcesArr.optJSONObject(idx) ?: return@mapNotNull null
                GroundedSource(
                    title = obj.optString("title"),
                    uri = obj.optString("uri"),
                    domain = obj.optString("domain")
                )
            }
            val html = root.optString("searchEntryPointHtml").takeIf { it.isNotBlank() }
            LiveBookKnowledgeResult(
                queryKey = root.getString("queryKey"),
                topicTitle = root.getString("topicTitle"),
                answerText = root.getString("answerText"),
                webSearchQueries = queries,
                sources = sources,
                searchEntryPointHtml = html,
                fetchedAtLabel = root.optString("fetchedAtLabel", "Cached")
            )
        }.getOrNull()
    }
}
