package com.example.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.ComicCoral
import com.example.ui.theme.HistoryOlive
import com.example.ui.theme.LiteratureBurgundy
import com.example.ui.theme.MathSlate
import com.example.ui.theme.ScienceTeal
import com.example.ui.theme.StoryAmber
import com.example.ui.theme.TechIndigo

enum class EducationLevel(
    val displayName: String,
    val badgeShort: String,
    val description: String
) {
    SCHOOL("SCHOOL", "SCHOOL", "FOUNDATIONAL ILLUSTRATED GUIDES & CURIOUS BEGINNINGS"),
    HIGH_SCHOOL("HIGH SCHOOL", "HIGH SCHOOL", "CORE SCIENCES, CODING, CLASSIC NOVELS & DEEPER CRAFT"),
    UNIVERSITY("UNIVERSITY", "UNIVERSITY", "ACADEMIC RIGOR, THEORY, ALGORITHMS & WORLD CANON"),
    PROFESSIONAL("PROFESSIONAL", "PRO", "MASTER-LEVEL RESEARCH, ARCHITECTURE & LIFELONG MASTERY")
}

enum class BookCategory(
    val displayName: String,
    val shelfNumbersLabel: String,
    val accentColor: Color,
    val iconLabel: String,
    val packSizeMb: Int,
    val packDescription: String
) {
    SCIENCE(
        "SCIENCE",
        "SHELVES 1–2",
        ScienceTeal,
        "PHYSICS, BIOLOGY & COSMOS",
        46,
        "COSMOS PLATES, PHYSICS DIAGRAMS & BIOLOGY FOLIOS"
    ),
    TECHNOLOGY(
        "TECHNOLOGY",
        "SHELVES 3–4",
        TechIndigo,
        "COMPUTING, ENGINEERING & AI",
        58,
        "ARCHITECTURE SCHEMATICS, CODE MANUALS & SYSTEM MAPS"
    ),
    LITERATURE(
        "LITERATURE",
        "SHELVES 5–6",
        LiteratureBurgundy,
        "CLASSICS, POETRY & CRITICISM",
        42,
        "ANNOTATED LITERARY EDITIONS, SONNETS & ESSAYS"
    ),
    STORY(
        "STORY",
        "SHELF 7",
        StoryAmber,
        "FICTION, MYSTERY & FOLKTALES",
        34,
        "ILLUSTRATED KEEPSAKE NOVELLAS & WORLD STORYBOOKS"
    ),
    COMIC(
        "COMIC",
        "SHELF 8",
        ComicCoral,
        "GRAPHIC NOVELS & VISUAL STRIPS",
        78,
        "HIGH-RESOLUTION SEQUENTIAL ART PANELS & ISSUES"
    ),
    MATHEMATICS(
        "MATHEMATICS",
        "SHELF 9",
        MathSlate,
        "NUMBERS, GEOMETRY & LOGIC",
        24,
        "GEOMETRIC PROOFS, CALCULUS CURVES & PATTERN PLATES"
    ),
    HISTORY_PHILOSOPHY(
        "HISTORY & PHILOSOPHY",
        "SHELF 10",
        HistoryOlive,
        "CIVILIZATIONS, ETHICS & WISDOM",
        31,
        "HISTORICAL ATLASES, ETHICS & TIMELESS MANUSCRIPTS"
    )
}

data class BookshelfInfo(
    val shelfNumber: Int,
    val category: BookCategory,
    val wingTitle: String,
    val subtitle: String,
    val normX: Float,
    val normY: Float,
    val isLeftRow: Boolean
)

data class StudyTableInfo(
    val tableId: String,
    val label: String,
    val lampTitle: String,
    val normX: Float,
    val normY: Float
)

data class BookPage(
    val pageNumber: Int,
    val chapterTitle: String,
    val body: String,
    val illustrationNote: String? = null,
    val reflectionPrompt: String
)

data class Book(
    val id: String,
    val title: String,
    val author: String,
    val category: BookCategory,
    val level: EducationLevel,
    val shelfNumber: Int,
    val spineColor: Color,
    val coverTrimColor: Color,
    val synopsis: String,
    val dedication: String,
    val pages: List<BookPage>,
    val realSubjectScope: String = "",
    val completeTopics: List<String> = emptyList()
)

enum class CharacterAvatar(
    val id: String,
    val title: String,
    val bio: String,
    val hairColor: Color,
    val skinColor: Color,
    val hasGlasses: Boolean,
    val hasHat: Boolean
) {
    SCHOLAR(
        id = "scholar",
        title = "THE CURIOUS SCHOLAR",
        bio = "ALWAYS CARRIES ROUND SPECTACLES AND A LEATHER SATCHEL FOR BIG QUESTIONS.",
        hairColor = Color(0xFF3B2820),
        skinColor = Color(0xFFF0C6A8),
        hasGlasses = true,
        hasHat = false
    ),
    TINKERER(
        id = "tinkerer",
        title = "THE TECH TINKERER",
        bio = "LOVES BLUEPRINTS, GLOWING LAMPS, AND BUILDING IDEAS FROM SCRATCH.",
        hairColor = Color(0xFF1D2432),
        skinColor = Color(0xFFD59B78),
        hasGlasses = false,
        hasHat = true
    ),
    STORYTELLER(
        id = "storyteller",
        title = "THE DREAMY STORYTELLER",
        bio = "FINDS WHOLE UNIVERSES TUCKED BETWEEN QUIET CHAPTERS AND CANDLELIGHT.",
        hairColor = Color(0xFF8C3B2B),
        skinColor = Color(0xFFF6D5BE),
        hasGlasses = false,
        hasHat = false
    ),
    EXPLORER(
        id = "explorer",
        title = "THE COMIC EXPLORER",
        bio = "SKETCHES WORLDS IN THE MARGINS AND LEAPS FROM SHELF TO SHELF.",
        hairColor = Color(0xFF4A3728),
        skinColor = Color(0xFF9E6848),
        hasGlasses = true,
        hasHat = true
    )
}

enum class LampAmbience(
    val id: String,
    val label: String,
    val subtitle: String,
    val glowColor: Color,
    val deskTint: Color,
    val pageTint: Color,
    val isNightImagination: Boolean
) {
    GOLDEN_STUDY(
        id = "golden_study",
        label = "GOLDEN STUDY",
        subtitle = "WARM BRASS BANKER'S GLOW",
        glowColor = Color(0xFFFFCA68),
        deskTint = Color(0x33FFA726),
        pageTint = Color(0xFFFAF1DC),
        isNightImagination = false
    ),
    CANDLELIGHT(
        id = "candlelight",
        label = "CANDLE REVERIE",
        subtitle = "DEEP HEARTH EMBER & INTIMACY",
        glowColor = Color(0xFFFF8A3D),
        deskTint = Color(0x4D7C3508),
        pageTint = Color(0xFFF5E3C3),
        isNightImagination = true
    ),
    STARLIGHT(
        id = "starlight",
        label = "STARLIGHT IMAGINATION",
        subtitle = "MIDNIGHT WONDER & COSMOS GLOW",
        glowColor = Color(0xFF7EB4FF),
        deskTint = Color(0x660F1733),
        pageTint = Color(0xFFEBEFF8),
        isNightImagination = true
    ),
    MORNING_WINDOW(
        id = "morning_window",
        label = "MORNING DAYLIGHT",
        subtitle = "CRISP SUNLIT READING DESK",
        glowColor = Color(0xFFFFF3CC),
        deskTint = Color(0x14FFFFFF),
        pageTint = Color(0xFFFFFCF5),
        isNightImagination = false
    ),
    EMERALD_SANCTUARY(
        id = "emerald_sanctuary",
        label = "EMERALD AURA",
        subtitle = "SERENE JADE SANCTUARY GLOW",
        glowColor = Color(0xFF5FE3A1),
        deskTint = Color(0x36144A34),
        pageTint = Color(0xFFF0F9F4),
        isNightImagination = true
    )
}

enum class TableCoverStyle(
    val id: String,
    val label: String,
    val description: String,
    val primaryColor: Color,
    val trimColor: Color,
    val accentPatternColor: Color,
    val hasClothCover: Boolean
) {
    NATURAL_WOOD(
        id = "natural_wood",
        label = "BARE OAK WOOD",
        description = "PURE NATURAL POLISHED TIMBER GRAIN",
        primaryColor = Color(0xFF8B5636),
        trimColor = Color(0xFFC99B5E),
        accentPatternColor = Color(0xFF6B3F24),
        hasClothCover = false
    ),
    ROYAL_BURGUNDY(
        id = "royal_burgundy",
        label = "ROYAL VELVET",
        description = "CRIMSON VELVET RUNNER WITH STITCHED GOLD TRIM",
        primaryColor = Color(0xFF7B1D32),
        trimColor = Color(0xFFD4A03E),
        accentPatternColor = Color(0xFF591223),
        hasClothCover = true
    ),
    EMERALD_DAMASK(
        id = "emerald_damask",
        label = "EMERALD DAMASK",
        description = "BOTANICAL GREEN BROCADE SHEET WITH BRASS BORDER",
        primaryColor = Color(0xFF1F5D46),
        trimColor = Color(0xFFE0B85C),
        accentPatternColor = Color(0xFF154231),
        hasClothCover = true
    ),
    IVORY_LACE(
        id = "ivory_lace",
        label = "IVORY LACE",
        description = "CRISP CREAM LINEN COVER WITH EMBROIDERED TRIM",
        primaryColor = Color(0xFFF6EFE0),
        trimColor = Color(0xFFC59B52),
        accentPatternColor = Color(0xFFE0D2B4),
        hasClothCover = true
    ),
    CELESTIAL_INDIGO(
        id = "celestial_indigo",
        label = "STARLIGHT SILK",
        description = "MIDNIGHT INDIGO TAPESTRY WITH GOLDEN BORDER",
        primaryColor = Color(0xFF23335A),
        trimColor = Color(0xFFF0C66B),
        accentPatternColor = Color(0xFF394F85),
        hasClothCover = true
    )
}

enum class ScholarRank(val title: String, val minPages: Int) {
    NOVICE("PAGE SEEKER", 0),
    LAMP_SCHOLAR("LAMP SCHOLAR", 10),
    ARCHIVIST("SHELF ARCHIVIST", 25),
    GRAND_LIBRARIAN("GRAND LIBRARIAN", 50);

    companion object {
        fun fromPages(pagesRead: Int): ScholarRank =
            entries.lastOrNull { pagesRead >= it.minPages } ?: NOVICE
    }
}

enum class CameraMode {
    LIBRARY_HALL,
    TABLE_DESK_POV
}
