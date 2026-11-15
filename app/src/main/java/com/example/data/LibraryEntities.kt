package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reader_profile")
data class ReaderProfileEntity(
    @PrimaryKey val id: Int = 1,
    val characterName: String = "",
    val avatarId: String = "scholar",
    val cloakColorIndex: Int = 0,
    val statusText: String = "EXPLORING THE GRAND LIBRARY",
    val totalPagesRead: Int = 0,
    val totalStudyMinutes: Int = 0,
    val booksCompleted: Long = 0L,
    val lampAmbienceId: String = "golden_study",
    val lampBrightness: Float = 0.85f,
    val hasSeenOrientation: Boolean = false,
    val createdAtMillis: Long = System.currentTimeMillis()
)

@Entity(tableName = "borrowed_books")
data class BorrowedBookEntity(
    @PrimaryKey val bookId: String,
    val bookTitle: String = "",
    val categoryId: String = "",
    val currentPage: Int = 0,
    val totalPages: Int = 1,
    val isBookmarked: Boolean = false,
    val bookmarkPage: Int = 0,
    val isCompleted: Boolean = false,
    val status: String = "borrowed",
    val borrowedAt: Long = System.currentTimeMillis(),
    val lastReadAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "margin_notes")
data class MarginNoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val bookId: String,
    val bookTitle: String = "",
    val pageNumber: Int,
    val noteText: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "reading_history")
data class ReadingHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val bookId: String,
    val bookTitle: String,
    val categoryName: String,
    val levelName: String,
    val action: String,
    val pageNumber: Int,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "downloaded_sections")
data class DownloadedSectionEntity(
    @PrimaryKey val categoryId: String,
    val sizeMb: Int,
    val isDownloaded: Boolean = false,
    val localPackPath: String = "",
    val bookCount: Int = 0,
    val downloadedAt: Long = 0L
)
