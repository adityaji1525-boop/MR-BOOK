package com.example.data

import androidx.compose.ui.graphics.Color
import com.example.model.BookCategory
import com.example.model.BookshelfInfo
import com.example.model.StudyTableInfo

object LibraryFloorLayout {

    val cloakColors: List<Pair<String, Color>> = listOf(
        "BURGUNDY" to Color(0xFF7D2438),
        "FOREST" to Color(0xFF235C48),
        "COBALT" to Color(0xFF2B4C7E),
        "AMBER" to Color(0xFFB5691E),
        "PLUM" to Color(0xFF5D3A6A)
    )

    val librarianSpot = Pair(0.50f, 0.21f)

    val bookshelves: List<BookshelfInfo> = listOf(
        BookshelfInfo(
            shelfNumber = 1,
            category = BookCategory.SCIENCE,
            wingTitle = "SCIENCE WING I",
            subtitle = "PHYSICS, LIGHT & COSMOS",
            normX = 0.13f,
            normY = 0.27f,
            isLeftRow = true
        ),
        BookshelfInfo(
            shelfNumber = 2,
            category = BookCategory.SCIENCE,
            wingTitle = "SCIENCE WING II",
            subtitle = "LIFE, CELLS & ECOLOGY",
            normX = 0.13f,
            normY = 0.41f,
            isLeftRow = true
        ),
        BookshelfInfo(
            shelfNumber = 3,
            category = BookCategory.TECHNOLOGY,
            wingTitle = "TECHNOLOGY WING I",
            subtitle = "LOGIC, CODE & NETWORKS",
            normX = 0.13f,
            normY = 0.55f,
            isLeftRow = true
        ),
        BookshelfInfo(
            shelfNumber = 4,
            category = BookCategory.TECHNOLOGY,
            wingTitle = "TECHNOLOGY WING II",
            subtitle = "SYSTEMS, ROBOTICS & AI",
            normX = 0.13f,
            normY = 0.69f,
            isLeftRow = true
        ),
        BookshelfInfo(
            shelfNumber = 5,
            category = BookCategory.LITERATURE,
            wingTitle = "LITERATURE WING I",
            subtitle = "CLASSIC NOVELS & PROSE",
            normX = 0.13f,
            normY = 0.83f,
            isLeftRow = true
        ),
        BookshelfInfo(
            shelfNumber = 6,
            category = BookCategory.LITERATURE,
            wingTitle = "LITERATURE WING II",
            subtitle = "POETRY, ESSAYS & THEORY",
            normX = 0.87f,
            normY = 0.27f,
            isLeftRow = false
        ),
        BookshelfInfo(
            shelfNumber = 7,
            category = BookCategory.STORY,
            wingTitle = "STORYTELLER ALCOVE",
            subtitle = "FABLES, MYSTERY & EPICS",
            normX = 0.87f,
            normY = 0.41f,
            isLeftRow = false
        ),
        BookshelfInfo(
            shelfNumber = 8,
            category = BookCategory.COMIC,
            wingTitle = "COMIC GALLERY",
            subtitle = "SEQUENTIAL ART & PANELS",
            normX = 0.87f,
            normY = 0.55f,
            isLeftRow = false
        ),
        BookshelfInfo(
            shelfNumber = 9,
            category = BookCategory.MATHEMATICS,
            wingTitle = "MATHEMATICS VAULT",
            subtitle = "PATTERNS, CALCULUS & PROOF",
            normX = 0.87f,
            normY = 0.69f,
            isLeftRow = false
        ),
        BookshelfInfo(
            shelfNumber = 10,
            category = BookCategory.HISTORY_PHILOSOPHY,
            wingTitle = "WISDOM ARCHIVE",
            subtitle = "HISTORY, ETHICS & THOUGHT",
            normX = 0.87f,
            normY = 0.83f,
            isLeftRow = false
        )
    )

    val studyTables: List<StudyTableInfo> = listOf(
        StudyTableInfo(
            tableId = "table_a",
            label = "TABLE A",
            lampTitle = "EMERALD BANKER'S LAMP",
            normX = 0.39f,
            normY = 0.40f
        ),
        StudyTableInfo(
            tableId = "table_b",
            label = "TABLE B",
            lampTitle = "AMBER BRASS LAMP",
            normX = 0.61f,
            normY = 0.40f
        ),
        StudyTableInfo(
            tableId = "table_c",
            label = "TABLE C",
            lampTitle = "STARLIGHT STUDY LAMP",
            normX = 0.39f,
            normY = 0.68f
        ),
        StudyTableInfo(
            tableId = "table_d",
            label = "TABLE D",
            lampTitle = "CANDLELIGHT DESK LAMP",
            normX = 0.61f,
            normY = 0.68f
        )
    )
}
