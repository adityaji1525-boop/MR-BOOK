package com.example.data

import com.google.firebase.Timestamp
import java.util.Date
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalLibraryRepository(
    private val dao: LibraryDao
) {
    fun observeProfile(): Flow<UserProfileDoc?> =
        dao.observeProfile().map { entity ->
            entity?.let {
                UserProfileDoc(
                    id = "local_reader",
                    userId = "local_reader",
                    characterName = it.characterName,
                    avatarId = it.avatarId,
                    cloakColorIndex = it.cloakColorIndex.toLong(),
                    statusText = it.statusText,
                    totalPagesRead = it.totalPagesRead.toLong(),
                    totalStudyMinutes = it.totalStudyMinutes.toLong(),
                    booksCompleted = it.booksCompleted,
                    lampAmbienceId = it.lampAmbienceId,
                    lampBrightness = it.lampBrightness.toDouble(),
                    createdAt = Timestamp(Date(it.createdAtMillis)),
                    updatedAt = Timestamp(Date(it.createdAtMillis))
                )
            }
        }

    fun observeBookProgress(): Flow<List<BookProgressDoc>> =
        dao.observeBorrowedBooks().map { list ->
            list.map { item ->
                BookProgressDoc(
                    id = item.bookId,
                    userId = "local_reader",
                    bookId = item.bookId,
                    bookTitle = item.bookTitle,
                    categoryId = item.categoryId,
                    currentPage = item.currentPage.toLong(),
                    totalPages = item.totalPages.toLong(),
                    isBookmarked = item.isBookmarked,
                    bookmarkPage = item.bookmarkPage.toLong(),
                    isCompleted = item.isCompleted,
                    status = item.status,
                    createdAt = Timestamp(Date(item.borrowedAt)),
                    updatedAt = Timestamp(Date(item.lastReadAt))
                )
            }
        }

    fun observeReadingHistory(): Flow<List<ReadingHistoryDoc>> =
        dao.observeReadingHistory().map { list ->
            list.map { item ->
                ReadingHistoryDoc(
                    id = item.id.toString(),
                    userId = "local_reader",
                    bookId = item.bookId,
                    bookTitle = item.bookTitle,
                    categoryName = item.categoryName,
                    levelName = item.levelName,
                    action = item.action,
                    pageNumber = item.pageNumber.toLong(),
                    createdAt = Timestamp(Date(item.createdAt))
                )
            }
        }

    fun observeMarginNotes(): Flow<List<MarginNoteDoc>> =
        dao.observeAllNotes().map { list ->
            list.map { note ->
                MarginNoteDoc(
                    id = note.id.toString(),
                    userId = "local_reader",
                    bookId = note.bookId,
                    bookTitle = note.bookTitle,
                    pageNumber = note.pageNumber.toLong(),
                    noteText = note.noteText,
                    createdAt = Timestamp(Date(note.createdAt))
                )
            }
        }

    suspend fun saveCharacterProfile(
        characterName: String,
        avatarId: String,
        cloakColorIndex: Int,
        statusText: String = "EXPLORING THE GRAND LIBRARY"
    ) {
        val cleanName = characterName.trim().ifBlank { "READER" }.uppercase().take(30)
        val cleanStatus = statusText.trim().ifBlank { "EXPLORING THE GRAND LIBRARY" }.uppercase().take(80)
        val existing = dao.getProfileOnce()
        val updated = existing?.copy(
            characterName = cleanName,
            avatarId = avatarId,
            cloakColorIndex = cloakColorIndex.coerceIn(0, 4),
            statusText = cleanStatus
        ) ?: ReaderProfileEntity(
            id = 1,
            characterName = cleanName,
            avatarId = avatarId,
            cloakColorIndex = cloakColorIndex.coerceIn(0, 4),
            statusText = cleanStatus,
            totalPagesRead = 0,
            totalStudyMinutes = 0,
            booksCompleted = 0L,
            lampAmbienceId = "golden_study",
            lampBrightness = 0.85f,
            hasSeenOrientation = false
        )
        dao.upsertProfile(updated)
    }

    suspend fun updateStatus(statusText: String) {
        val clean = statusText.trim().ifBlank { "EXPLORING THE LIBRARY" }.uppercase().take(80)
        dao.updateStatus(clean)
    }

    suspend fun updateLampSettings(ambienceId: String, brightness: Float) {
        dao.updateLampSettings(ambienceId, brightness.coerceIn(0.25f, 1.0f))
    }

    suspend fun incrementTotals(pagesDelta: Long = 0L, minutesDelta: Long = 0L, completedDelta: Long = 0L) {
        dao.incrementTotals(
            pagesDelta = pagesDelta.toInt(),
            minutesDelta = minutesDelta.toInt(),
            completedDelta = completedDelta
        )
    }

    suspend fun upsertBookProgress(
        bookId: String,
        bookTitle: String,
        categoryId: String,
        currentPage: Int,
        totalPages: Int,
        isBookmarked: Boolean,
        bookmarkPage: Int,
        isCompleted: Boolean,
        status: String
    ) {
        val now = System.currentTimeMillis()
        val existing = dao.getBorrowedBook(bookId)
        dao.upsertBorrowedBook(
            BorrowedBookEntity(
                bookId = bookId,
                bookTitle = bookTitle.take(120),
                categoryId = categoryId.take(32),
                currentPage = currentPage.coerceIn(0, 500),
                totalPages = totalPages.coerceIn(1, 500),
                isBookmarked = isBookmarked,
                bookmarkPage = bookmarkPage.coerceIn(0, 500),
                isCompleted = isCompleted,
                status = status,
                borrowedAt = existing?.borrowedAt ?: now,
                lastReadAt = now
            )
        )
    }

    suspend fun removeBookProgress(bookId: String) {
        dao.returnBook(bookId)
    }

    suspend fun addMarginNote(bookId: String, bookTitle: String, pageNumber: Int, noteText: String) {
        val clean = noteText.trim().take(500)
        if (clean.isBlank()) return
        dao.insertNote(
            MarginNoteEntity(
                bookId = bookId,
                bookTitle = bookTitle.take(120),
                pageNumber = pageNumber.coerceIn(0, 500),
                noteText = clean,
                createdAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun deleteMarginNote(noteId: String) {
        val numericId = noteId.toLongOrNull() ?: return
        dao.deleteNote(numericId)
    }

    suspend fun logHistory(
        bookId: String,
        bookTitle: String,
        categoryName: String,
        levelName: String,
        action: String,
        pageNumber: Int
    ) {
        dao.insertHistory(
            ReadingHistoryEntity(
                bookId = bookId,
                bookTitle = bookTitle.take(120),
                categoryName = categoryName.take(40),
                levelName = levelName.take(32),
                action = action,
                pageNumber = pageNumber.coerceIn(0, 500),
                createdAt = System.currentTimeMillis()
            )
        )
    }
}
