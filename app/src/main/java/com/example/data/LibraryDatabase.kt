package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        ReaderProfileEntity::class,
        BorrowedBookEntity::class,
        MarginNoteEntity::class,
        ReadingHistoryEntity::class,
        DownloadedSectionEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class LibraryDatabase : RoomDatabase() {
    abstract fun libraryDao(): LibraryDao

    companion object {
        @Volatile
        private var INSTANCE: LibraryDatabase? = null

        fun getInstance(context: Context): LibraryDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    LibraryDatabase::class.java,
                    "mr_book_library_db"
                )
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
