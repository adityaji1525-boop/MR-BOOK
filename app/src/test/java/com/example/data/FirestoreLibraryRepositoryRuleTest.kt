package com.example.data

import com.example.base.FirestoreEmulatorTestBase
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import java.util.UUID

class FirestoreLibraryRepositoryRuleTest : FirestoreEmulatorTestBase() {

    @Test
    fun saveCharacterProfile_andObserve_succeedsForOwner() = runBlocking {
        val uid = signInTestUser(ALICE_EMAIL)
        val repo = FirestoreLibraryRepository(firestore, auth)

        withTimeout(DEFAULT_TIMEOUT_MS) {
            repo.saveCharacterProfile("Aria", "scholar", 1, "Studying at Table A")
        }

        val profile = withTimeout(FLOW_TIMEOUT_MS) {
            repo.observeProfile(uid).first { it != null }
        }
        assertNotNull(profile)
        assertEquals("Aria", profile?.characterName)
        assertEquals("Studying at Table A", profile?.statusText)
    }

    @Test
    fun upsertBookProgress_andLogHistory_succeedsForOwner() = runBlocking {
        val uid = signInTestUser(ALICE_EMAIL)
        val repo = FirestoreLibraryRepository(firestore, auth)
        val uniqueBookId = "sci_school_${UUID.randomUUID().toString().take(6)}"

        withTimeout(DEFAULT_TIMEOUT_MS) {
            repo.upsertBookProgress(
                bookId = uniqueBookId,
                bookTitle = "Why the Sky Glows Blue",
                categoryId = "SCIENCE",
                currentPage = 2,
                totalPages = 4,
                isBookmarked = true,
                bookmarkPage = 2,
                isCompleted = false,
                status = "reading"
            )
            repo.logHistory(
                bookId = uniqueBookId,
                bookTitle = "Why the Sky Glows Blue",
                categoryName = "Science",
                levelName = "School",
                action = "turned_page",
                pageNumber = 2
            )
        }

        val progressList = withTimeout(FLOW_TIMEOUT_MS) {
            repo.observeBookProgress(uid).first { list -> list.any { it.bookId == uniqueBookId } }
        }
        assertTrue(progressList.any { it.bookId == uniqueBookId && it.currentPage == 2L })

        val historyList = withTimeout(FLOW_TIMEOUT_MS) {
            repo.observeReadingHistory(uid).first { list -> list.any { it.bookId == uniqueBookId } }
        }
        assertTrue(historyList.any { it.bookId == uniqueBookId && it.action == "turned_page" })
    }

    @Test
    fun observeProfile_crossUserAccess_failsWithPermissionDenied() = runBlocking {
        val aliceUid = signInTestUser(ALICE_EMAIL)
        val aliceRepo = FirestoreLibraryRepository(firestore, auth)
        withTimeout(DEFAULT_TIMEOUT_MS) {
            aliceRepo.saveCharacterProfile("Alice", "scholar", 0)
        }

        signInTestUser(BOB_EMAIL)
        val bobRepo = FirestoreLibraryRepository(firestore, auth)
        try {
            withTimeout(FLOW_TIMEOUT_MS) {
                bobRepo.observeProfile(aliceUid).first()
            }
            fail("Expected PERMISSION_DENIED when Bob reads Alice profile")
        } catch (e: Exception) {
            val firestoreException = e.unwrapFirestoreException()
            assertNotNull("Expected FirestoreException, got $e", firestoreException)
            assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, firestoreException?.code)
        }
    }

    @Test
    fun observeBookProgress_unauthenticated_failsWithPermissionDenied() = runBlocking {
        auth.signOut()
        val repo = FirestoreLibraryRepository(firestore, auth)
        try {
            withTimeout(FLOW_TIMEOUT_MS) {
                repo.observeBookProgress("alice_uid").first()
            }
            fail("Expected PERMISSION_DENIED when unauthenticated")
        } catch (e: Exception) {
            val firestoreException = e.unwrapFirestoreException()
            assertNotNull("Expected FirestoreException, got $e", firestoreException)
            assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, firestoreException?.code)
        }
    }

    private companion object {
        const val ALICE_EMAIL = "alice_library@test.com"
        const val BOB_EMAIL = "bob_library@test.com"
        const val DEFAULT_TIMEOUT_MS = 5000L
        const val FLOW_TIMEOUT_MS = 4000L
    }
}
