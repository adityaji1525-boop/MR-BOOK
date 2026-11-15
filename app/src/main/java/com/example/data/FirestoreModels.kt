package com.example.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@IgnoreExtraProperties
data class UserProfileDoc(
    @DocumentId val id: String = "",
    val userId: String = "",
    val characterName: String = "",
    val avatarId: String = "scholar",
    val cloakColorIndex: Long = 0L,
    val statusText: String = "Exploring the Library",
    val totalPagesRead: Long = 0L,
    val totalStudyMinutes: Long = 0L,
    val booksCompleted: Long = 0L,
    val lampAmbienceId: String = "golden_study",
    val lampBrightness: Double = 0.85,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class BookProgressDoc(
    @DocumentId val id: String = "",
    val userId: String = "",
    val bookId: String = "",
    val bookTitle: String = "",
    val categoryId: String = "",
    val currentPage: Long = 0L,
    val totalPages: Long = 1L,
    val isBookmarked: Boolean = false,
    val bookmarkPage: Long = 0L,
    val isCompleted: Boolean = false,
    val status: String = "borrowed",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class ReadingHistoryDoc(
    @DocumentId val id: String = "",
    val userId: String = "",
    val bookId: String = "",
    val bookTitle: String = "",
    val categoryName: String = "",
    val levelName: String = "",
    val action: String = "opened",
    val pageNumber: Long = 0L,
    @ServerTimestamp val createdAt: Timestamp? = null
)

@IgnoreExtraProperties
data class MarginNoteDoc(
    @DocumentId val id: String = "",
    val userId: String = "",
    val bookId: String = "",
    val bookTitle: String = "",
    val pageNumber: Long = 0L,
    val noteText: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null
)
