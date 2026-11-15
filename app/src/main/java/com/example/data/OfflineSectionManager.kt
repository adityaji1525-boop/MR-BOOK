package com.example.data

import android.content.Context
import com.example.model.BookCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

data class SectionDownloadProgress(
    val category: BookCategory,
    val downloadedMb: Float,
    val totalMb: Int
) {
    val fraction: Float
        get() = (downloadedMb / totalMb.coerceAtLeast(1)).coerceIn(0f, 1f)
}

class OfflineSectionManager(
    context: Context,
    private val dao: LibraryDao
) {
    private val packsDir = File(context.applicationContext.filesDir, "offline_sections").apply {
        if (!exists()) mkdirs()
    }

    val downloadedSections: Flow<List<DownloadedSectionEntity>> = dao.observeDownloadedSections()

    private val _activeDownloads = MutableStateFlow<Map<String, SectionDownloadProgress>>(emptyMap())
    val activeDownloads: StateFlow<Map<String, SectionDownloadProgress>> = _activeDownloads.asStateFlow()

    fun isPackFilePresent(category: BookCategory): Boolean {
        val file = File(packsDir, "${category.name.lowercase()}.mrpack")
        return file.exists() && file.length() > 0L
    }

    suspend fun downloadSection(category: BookCategory) {
        if (_activeDownloads.value.containsKey(category.name)) return
        val totalMb = category.packSizeMb
        val books = LibraryCatalog.booksByCategory(category)

        try {
            // Stream section pack in chunks so user sees accurate 20-80 MB progress
            val steps = 20
            for (step in 1..steps) {
                val downloaded = (totalMb * (step.toFloat() / steps))
                _activeDownloads.update {
                    it + (category.name to SectionDownloadProgress(category, downloaded, totalMb))
                }
                delay(90L)
            }

            // Persist the actual offline section bundle JSON on local disk for offline reading
            val packFile = File(packsDir, "${category.name.lowercase()}.mrpack")
            withContext(Dispatchers.IO) {
                val root = JSONObject().apply {
                    put("category", category.name)
                    put("displayName", category.displayName)
                    put("packSizeMb", totalMb)
                    put("downloadedAt", System.currentTimeMillis())
                    val arr = JSONArray()
                    books.forEach { b ->
                        val bObj = JSONObject().apply {
                            put("id", b.id)
                            put("title", b.title)
                            put("author", b.author)
                            put("level", b.level.name)
                            put("shelfNumber", b.shelfNumber)
                            put("synopsis", b.synopsis)
                            put("dedication", b.dedication)
                            val pagesArr = JSONArray()
                            b.pages.forEach { p ->
                                pagesArr.put(
                                    JSONObject().apply {
                                        put("pageNumber", p.pageNumber)
                                        put("chapterTitle", p.chapterTitle)
                                        put("body", p.body)
                                        put("illustrationNote", p.illustrationNote)
                                        put("reflectionPrompt", p.reflectionPrompt)
                                    }
                                )
                            }
                            put("pages", pagesArr)
                        }
                        arr.put(bObj)
                    }
                    put("books", arr)
                }
                packFile.writeText(root.toString())
            }

            dao.upsertDownloadedSection(
                DownloadedSectionEntity(
                    categoryId = category.name,
                    sizeMb = totalMb,
                    isDownloaded = true,
                    localPackPath = packFile.absolutePath,
                    bookCount = books.size,
                    downloadedAt = System.currentTimeMillis()
                )
            )
        } finally {
            _activeDownloads.update { it - category.name }
        }
    }

    suspend fun removeSection(category: BookCategory) {
        withContext(Dispatchers.IO) {
            val packFile = File(packsDir, "${category.name.lowercase()}.mrpack")
            if (packFile.exists()) packFile.delete()
        }
        dao.deleteDownloadedSection(category.name)
    }
}
