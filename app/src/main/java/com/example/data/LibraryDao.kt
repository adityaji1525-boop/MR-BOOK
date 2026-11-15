package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LibraryDao {

    @Query("SELECT * FROM reader_profile WHERE id = 1")
    fun observeProfile(): Flow<ReaderProfileEntity?>

    @Query("SELECT * FROM reader_profile WHERE id = 1")
    suspend fun getProfileOnce(): ReaderProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProfile(profile: ReaderProfileEntity)

    @Query("UPDATE reader_profile SET statusText = :statusText WHERE id = 1")
    suspend fun updateStatus(statusText: String)

    @Query("UPDATE reader_profile SET lampAmbienceId = :ambienceId, lampBrightness = :brightness WHERE id = 1")
    suspend fun updateLampSettings(ambienceId: String, brightness: Float)

    @Query(
        """
        UPDATE reader_profile
        SET totalPagesRead = totalPagesRead + :pagesDelta,
            totalStudyMinutes = totalStudyMinutes + :minutesDelta,
            booksCompleted = booksCompleted + :completedDelta
        WHERE id = 1
        """
    )
    suspend fun incrementTotals(pagesDelta: Int, minutesDelta: Int, completedDelta: Long)

    @Query("SELECT * FROM borrowed_books ORDER BY lastReadAt DESC")
    fun observeBorrowedBooks(): Flow<List<BorrowedBookEntity>>

    @Query("SELECT * FROM borrowed_books WHERE bookId = :bookId")
    suspend fun getBorrowedBook(bookId: String): BorrowedBookEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertBorrowedBook(book: BorrowedBookEntity)

    @Query("DELETE FROM borrowed_books WHERE bookId = :bookId")
    suspend fun returnBook(bookId: String)

    @Query("SELECT * FROM margin_notes WHERE bookId = :bookId ORDER BY pageNumber ASC, createdAt DESC")
    fun observeNotesForBook(bookId: String): Flow<List<MarginNoteEntity>>

    @Query("SELECT * FROM margin_notes ORDER BY createdAt DESC")
    fun observeAllNotes(): Flow<List<MarginNoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: MarginNoteEntity)

    @Query("DELETE FROM margin_notes WHERE id = :noteId")
    suspend fun deleteNote(noteId: Long)

    @Query("SELECT * FROM reading_history ORDER BY createdAt DESC LIMIT 60")
    fun observeReadingHistory(): Flow<List<ReadingHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(entry: ReadingHistoryEntity)

    @Query("SELECT * FROM downloaded_sections")
    fun observeDownloadedSections(): Flow<List<DownloadedSectionEntity>>

    @Query("SELECT * FROM downloaded_sections WHERE categoryId = :categoryId")
    suspend fun getDownloadedSection(categoryId: String): DownloadedSectionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertDownloadedSection(section: DownloadedSectionEntity)

    @Query("DELETE FROM downloaded_sections WHERE categoryId = :categoryId")
    suspend fun deleteDownloadedSection(categoryId: String)
}
