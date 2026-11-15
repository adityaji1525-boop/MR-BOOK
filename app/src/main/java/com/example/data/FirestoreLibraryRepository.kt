package com.example.data

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class FirestoreLibraryRepository(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth = Firebase.auth
) {
    private fun requireUserId(): String {
        return auth.currentUser?.uid
            ?: throw IllegalStateException("User must be signed in with Google before accessing Firestore.")
    }

    fun observeProfile(userId: String): Flow<UserProfileDoc?> = flow {
        val docRef = db.collection("users").document(userId)
        emitAll(
            docRef.snapshots()
                .map { snap ->
                    if (snap.exists()) {
                        snap.toObject(UserProfileDoc::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
                    } else {
                        null
                    }
                }
                .catch { error ->
                    handleFlowError(error, OperationType.GET, docRef.path, userId)
                    throw error
                }
        )
    }

    fun observeBookProgress(userId: String): Flow<List<BookProgressDoc>> = flow {
        val colRef = db.collection("users").document(userId).collection("book_progress")
        emitAll(
            colRef.orderBy("updatedAt", Query.Direction.DESCENDING)
                .snapshots()
                .map { snap ->
                    snap.toObjects(BookProgressDoc::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
                }
                .catch { error ->
                    handleFlowError(error, OperationType.LIST, colRef.path, userId)
                    throw error
                }
        )
    }

    fun observeReadingHistory(userId: String): Flow<List<ReadingHistoryDoc>> = flow {
        val colRef = db.collection("users").document(userId).collection("reading_history")
        emitAll(
            colRef.orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(60)
                .snapshots()
                .map { snap ->
                    snap.toObjects(ReadingHistoryDoc::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
                }
                .catch { error ->
                    handleFlowError(error, OperationType.LIST, colRef.path, userId)
                    throw error
                }
        )
    }

    fun observeMarginNotes(userId: String): Flow<List<MarginNoteDoc>> = flow {
        val colRef = db.collection("users").document(userId).collection("margin_notes")
        emitAll(
            colRef.orderBy("createdAt", Query.Direction.DESCENDING)
                .snapshots()
                .map { snap ->
                    snap.toObjects(MarginNoteDoc::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
                }
                .catch { error ->
                    handleFlowError(error, OperationType.LIST, colRef.path, userId)
                    throw error
                }
        )
    }

    suspend fun saveCharacterProfile(
        characterName: String,
        avatarId: String,
        cloakColorIndex: Int,
        statusText: String = "ENTERED THE LIBRARY"
    ) {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid)
        val cleanName = characterName.trim().ifBlank { "READER" }.uppercase().take(30)
        val cleanStatus = statusText.trim().ifBlank { "EXPLORING THE LIBRARY" }.uppercase().take(80)
        try {
            val existing = docRef.get().await()
            if (existing.exists()) {
                docRef.update(
                    mapOf(
                        "characterName" to cleanName,
                        "avatarId" to avatarId,
                        "cloakColorIndex" to cloakColorIndex.coerceIn(0, 4),
                        "statusText" to cleanStatus,
                        "updatedAt" to FieldValue.serverTimestamp()
                    )
                ).await()
            } else {
                val payload = mapOf(
                    "userId" to uid,
                    "characterName" to cleanName,
                    "avatarId" to avatarId,
                    "cloakColorIndex" to cloakColorIndex.coerceIn(0, 4),
                    "statusText" to cleanStatus,
                    "totalPagesRead" to 0L,
                    "totalStudyMinutes" to 0L,
                    "booksCompleted" to 0L,
                    "lampAmbienceId" to "golden_study",
                    "lampBrightness" to 0.85,
                    "createdAt" to FieldValue.serverTimestamp(),
                    "updatedAt" to FieldValue.serverTimestamp()
                )
                docRef.set(payload).await()
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, docRef.path, auth)
            throw e
        }
    }

    suspend fun updateStatus(statusText: String) {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid)
        try {
            docRef.update(
                mapOf(
                    "statusText" to statusText.trim().ifBlank { "Exploring the Library" }.take(80),
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            ).await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, docRef.path, auth)
        }
    }

    suspend fun updateLampSettings(ambienceId: String, brightness: Float) {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid)
        try {
            docRef.update(
                mapOf(
                    "lampAmbienceId" to ambienceId,
                    "lampBrightness" to brightness.toDouble().coerceIn(0.25, 1.0),
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            ).await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, docRef.path, auth)
        }
    }

    suspend fun incrementTotals(pagesDelta: Long = 0L, minutesDelta: Long = 0L, completedDelta: Long = 0L) {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid)
        val updates = mutableMapOf<String, Any>("updatedAt" to FieldValue.serverTimestamp())
        if (pagesDelta != 0L) updates["totalPagesRead"] = FieldValue.increment(pagesDelta)
        if (minutesDelta != 0L) updates["totalStudyMinutes"] = FieldValue.increment(minutesDelta)
        if (completedDelta != 0L) updates["booksCompleted"] = FieldValue.increment(completedDelta)
        try {
            docRef.update(updates).await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, docRef.path, auth)
        }
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
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid).collection("book_progress").document(bookId)
        try {
            val existing = docRef.get().await()
            if (existing.exists()) {
                docRef.update(
                    mapOf(
                        "bookTitle" to bookTitle.take(120),
                        "categoryId" to categoryId.take(32),
                        "currentPage" to currentPage.coerceIn(0, 500),
                        "totalPages" to totalPages.coerceIn(1, 500),
                        "isBookmarked" to isBookmarked,
                        "bookmarkPage" to bookmarkPage.coerceIn(0, 500),
                        "isCompleted" to isCompleted,
                        "status" to status,
                        "updatedAt" to FieldValue.serverTimestamp()
                    )
                ).await()
            } else {
                docRef.set(
                    mapOf(
                        "userId" to uid,
                        "bookId" to bookId,
                        "bookTitle" to bookTitle.take(120),
                        "categoryId" to categoryId.take(32),
                        "currentPage" to currentPage.coerceIn(0, 500),
                        "totalPages" to totalPages.coerceIn(1, 500),
                        "isBookmarked" to isBookmarked,
                        "bookmarkPage" to bookmarkPage.coerceIn(0, 500),
                        "isCompleted" to isCompleted,
                        "status" to status,
                        "createdAt" to FieldValue.serverTimestamp(),
                        "updatedAt" to FieldValue.serverTimestamp()
                    )
                ).await()
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, docRef.path, auth)
            throw e
        }
    }

    suspend fun removeBookProgress(bookId: String) {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid).collection("book_progress").document(bookId)
        try {
            docRef.delete().await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.DELETE, docRef.path, auth)
        }
    }

    suspend fun logHistory(
        bookId: String,
        bookTitle: String,
        categoryName: String,
        levelName: String,
        action: String,
        pageNumber: Int
    ) {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid).collection("reading_history").document()
        val payload = mapOf(
            "userId" to uid,
            "bookId" to bookId.take(64),
            "bookTitle" to bookTitle.take(120),
            "categoryName" to categoryName.take(48),
            "levelName" to levelName.take(32),
            "action" to action,
            "pageNumber" to pageNumber.coerceIn(0, 500),
            "createdAt" to FieldValue.serverTimestamp()
        )
        try {
            docRef.set(payload).await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, docRef.path, auth)
        }
    }

    suspend fun addMarginNote(bookId: String, bookTitle: String, pageNumber: Int, noteText: String) {
        val uid = requireUserId()
        val trimmed = noteText.trim().take(500)
        if (trimmed.isEmpty()) return
        val docRef = db.collection("users").document(uid).collection("margin_notes").document()
        val payload = mapOf(
            "userId" to uid,
            "bookId" to bookId.take(64),
            "bookTitle" to bookTitle.take(120),
            "pageNumber" to pageNumber.coerceIn(0, 500),
            "noteText" to trimmed,
            "createdAt" to FieldValue.serverTimestamp()
        )
        try {
            docRef.set(payload).await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, docRef.path, auth)
            throw e
        }
    }

    suspend fun deleteMarginNote(noteId: String) {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid).collection("margin_notes").document(noteId)
        try {
            docRef.delete().await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.DELETE, docRef.path, auth)
        }
    }

    private fun handleFlowError(error: Throwable, op: OperationType, path: String, expectedUid: String) {
        val firestoreError = generateSequence(error) { it.cause }
            .filterIsInstance<FirebaseFirestoreException>()
            .firstOrNull()
        if (firestoreError == null && error is CancellationException) return
        val exceptionToLog = firestoreError ?: (error as? Exception)
        if (exceptionToLog != null && auth.currentUser?.uid == expectedUid) {
            handleFirestoreError(exceptionToLog, op, path, auth)
        }
    }
}
