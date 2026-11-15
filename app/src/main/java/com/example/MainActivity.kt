package com.example

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ai.configureAppCheck
import com.example.data.LibraryDatabase
import com.example.data.LocalLibraryRepository
import com.example.data.OfflineSectionManager
import com.example.ui.screens.LibraryRootScreen
import com.example.ui.theme.MRBookTheme
import com.example.ui.viewmodel.LibraryViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureAppCheck(this, intent)
        enableEdgeToEdge()
        setContent {
            MRBookTheme {
                val context = LocalContext.current
                val roomDb = remember(context) {
                    LibraryDatabase.getInstance(context.applicationContext)
                }
                val localRepo = remember(roomDb) {
                    LocalLibraryRepository(roomDb.libraryDao())
                }
                val offlineManager = remember(context, roomDb) {
                    OfflineSectionManager(context.applicationContext, roomDb.libraryDao())
                }
                val prefs = remember(context) {
                    context.applicationContext.getSharedPreferences(
                        "mr_book_local_prefs",
                        Context.MODE_PRIVATE
                    )
                }
                val factory = remember(localRepo, offlineManager, prefs) {
                    object : ViewModelProvider.Factory {
                        @Suppress("UNCHECKED_CAST")
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return LibraryViewModel(
                                libraryRepo = localRepo,
                                offlineManager = offlineManager,
                                prefs = prefs
                            ) as T
                        }
                    }
                }
                val libraryViewModel: LibraryViewModel = viewModel(
                    key = "library_vm_local",
                    factory = factory
                )
                LibraryRootScreen(viewModel = libraryViewModel)
            }
        }
    }
}
