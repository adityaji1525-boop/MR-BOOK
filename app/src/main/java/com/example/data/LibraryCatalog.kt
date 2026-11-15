package com.example.data

import com.example.model.Book
import com.example.model.BookCategory
import com.example.model.EducationLevel

object LibraryCatalog {
    val allBooks: List<Book> by lazy {
        LibraryCatalogPart1.books + LibraryCatalogPart2.books + LibraryCatalogPart3.books
    }

    fun findById(id: String): Book? = allBooks.firstOrNull { it.id == id }

    fun booksOnShelf(shelfNumber: Int): List<Book> =
        allBooks.filter { it.shelfNumber == shelfNumber }.sortedBy { it.level.ordinal }

    fun booksByCategory(category: BookCategory): List<Book> =
        allBooks.filter { it.category == category }.sortedBy { it.level.ordinal }

    fun searchBooks(
        query: String,
        category: BookCategory? = null,
        level: EducationLevel? = null
    ): List<Book> {
        val q = query.trim().lowercase()
        return allBooks.filter { book ->
            val matchesQuery = q.isEmpty() ||
                book.title.lowercase().contains(q) ||
                book.author.lowercase().contains(q) ||
                book.synopsis.lowercase().contains(q) ||
                book.realSubjectScope.lowercase().contains(q) ||
                book.category.displayName.lowercase().contains(q) ||
                book.level.displayName.lowercase().contains(q) ||
                "shelf ${book.shelfNumber}".contains(q) ||
                "#${book.shelfNumber}" == q ||
                book.completeTopics.any { it.lowercase().contains(q) } ||
                book.pages.any {
                    it.chapterTitle.lowercase().contains(q) ||
                        it.body.lowercase().contains(q)
                }
            val matchesCategory = category == null || book.category == category
            val matchesLevel = level == null || book.level == level
            matchesQuery && matchesCategory && matchesLevel
        }
    }
}
