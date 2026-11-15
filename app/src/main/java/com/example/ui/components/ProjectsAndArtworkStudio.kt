package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.WorkOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.ArtworkPoint
import com.example.model.ArtworkStroke
import com.example.model.BookCategory
import com.example.model.LibraryGalleryPlate
import com.example.model.LibraryProject
import com.example.model.SavedArtwork
import com.example.model.UserScienceTemplate
import com.example.ui.theme.BrassGold
import com.example.ui.theme.BurgundyLeather
import com.example.ui.theme.EmeraldLibrarian
import com.example.ui.theme.InkCharcoal
import com.example.ui.theme.InkMuted
import com.example.ui.theme.MahoganyDark
import com.example.ui.theme.ParchmentAged
import com.example.ui.theme.ParchmentCream
import com.example.ui.theme.ParchmentSoft
import com.example.ui.theme.PlayfairDisplayFontFamily
import com.example.ui.theme.PlusJakartaSansFontFamily

@Composable
fun ProjectsAndArtworkStudio(
    projects: List<LibraryProject>,
    savedArtworks: List<SavedArtwork>,
    customTemplates: List<UserScienceTemplate>,
    onAddProject: (String, String, String) -> Unit,
    onToggleProjectDone: (String) -> Unit,
    onDeleteProject: (String) -> Unit,
    onSaveArtwork: (String, List<ArtworkStroke>) -> Unit,
    onDeleteArtwork: (String) -> Unit,
    onSaveCustomTemplate: (UserScienceTemplate) -> Unit,
    onDeleteCustomTemplate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var studioMode by remember { mutableIntStateOf(0) } // 0 = PROJECTS, 1 = ARTWORK

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Sub-mode Toggle: PROJECTS vs ARTWORK
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf(
                Pair("PROJECTS (${projects.size})", Icons.Default.WorkOutline),
                Pair("ARTWORK (${savedArtworks.size})", Icons.Default.Palette)
            ).forEachIndexed { idx, (label, icon) ->
                val selected = studioMode == idx
                Surface(
                    color = if (selected) BurgundyLeather else ParchmentSoft,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, BrassGold, RoundedCornerShape(10.dp))
                        .clickable { studioMode = idx }
                        .testTag(if (idx == 0) "studio_tab_projects" else "studio_tab_artwork")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 9.dp)
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = if (selected) ParchmentCream else BurgundyLeather,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(5.dp))
                        Text(
                            text = label,
                            fontFamily = PlusJakartaSansFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = if (selected) ParchmentCream else MahoganyDark
                        )
                    }
                }
            }
        }

        if (studioMode == 0) {
            ProjectsPane(
                projects = projects,
                customTemplates = customTemplates,
                onAddProject = onAddProject,
                onToggleProjectDone = onToggleProjectDone,
                onDeleteProject = onDeleteProject,
                onSaveCustomTemplate = onSaveCustomTemplate,
                onDeleteCustomTemplate = onDeleteCustomTemplate
            )
        } else {
            ArtworkPane(
                savedArtworks = savedArtworks,
                onSaveArtwork = onSaveArtwork,
                onDeleteArtwork = onDeleteArtwork
            )
        }
    }
}

@Composable
private fun ProjectsPane(
    projects: List<LibraryProject>,
    customTemplates: List<UserScienceTemplate>,
    onAddProject: (String, String, String) -> Unit,
    onToggleProjectDone: (String) -> Unit,
    onDeleteProject: (String) -> Unit,
    onSaveCustomTemplate: (UserScienceTemplate) -> Unit,
    onDeleteCustomTemplate: (String) -> Unit
) {
    var showScienceHelper by remember { mutableStateOf(false) }
    var titleDraft by remember { mutableStateOf("") }
    var goalDraft by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(BookCategory.SCIENCE) }

    // Option inside Projects: MY PROJECTS vs SCIENCE MODEL HELPER
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
        Surface(
            color = if (!showScienceHelper) BurgundyLeather else ParchmentSoft,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .weight(1f)
                .border(1.dp, BrassGold, RoundedCornerShape(10.dp))
                .clickable { showScienceHelper = false }
                .testTag("projects_submode_list")
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.WorkOutline,
                    contentDescription = null,
                    tint = if (!showScienceHelper) ParchmentCream else BurgundyLeather,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(Modifier.width(5.dp))
                Text(
                    text = "MY PROJECTS (${projects.size})",
                    fontFamily = PlusJakartaSansFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.5.sp,
                    color = if (!showScienceHelper) ParchmentCream else MahoganyDark
                )
            }
        }
        Surface(
            color = if (showScienceHelper) EmeraldLibrarian else ParchmentSoft,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .weight(1.15f)
                .border(1.dp, EmeraldLibrarian, RoundedCornerShape(10.dp))
                .clickable { showScienceHelper = true }
                .testTag("projects_submode_science_helper")
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Science,
                    contentDescription = null,
                    tint = if (showScienceHelper) Color.White else EmeraldLibrarian,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(Modifier.width(5.dp))
                Text(
                    text = "SCIENCE MODEL HELPER",
                    fontFamily = PlusJakartaSansFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.5.sp,
                    color = if (showScienceHelper) Color.White else EmeraldLibrarian
                )
            }
        }
    }

    if (showScienceHelper) {
        ScienceProjectHelperPane(
            customTemplates = customTemplates,
            onSaveCustomTemplate = onSaveCustomTemplate,
            onDeleteCustomTemplate = onDeleteCustomTemplate,
            onSaveToProjects = { title, cat, goal ->
                onAddProject(title, cat, goal)
                showScienceHelper = false
            }
        )
        return
    }

    Surface(
        color = ParchmentSoft,
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BrassGold, RoundedCornerShape(14.dp))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AddTask, contentDescription = null, tint = EmeraldLibrarian, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "NEW READING PROJECT",
                    fontFamily = PlayfairDisplayFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.5.sp,
                    color = MahoganyDark
                )
            }
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = titleDraft,
                onValueChange = { titleDraft = it.take(60) },
                placeholder = { Text("PROJECT TITLE (E.G. SOLAR SYSTEM REPORT)...") },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = EmeraldLibrarian,
                    unfocusedBorderColor = BrassGold,
                    focusedTextColor = InkCharcoal,
                    unfocusedTextColor = InkCharcoal
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("project_title_input")
            )
            Spacer(Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                items(BookCategory.entries) { cat ->
                    val selected = cat == selectedCategory
                    Surface(
                        color = if (selected) cat.accentColor else ParchmentCream,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .border(1.dp, cat.accentColor, RoundedCornerShape(8.dp))
                            .clickable { selectedCategory = cat }
                    ) {
                        Text(
                            text = cat.displayName,
                            fontFamily = PlusJakartaSansFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = if (selected) Color.White else InkCharcoal,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                        )
                    }
                }
            }
            Spacer(Modifier.height(6.dp))
            OutlinedTextField(
                value = goalDraft,
                onValueChange = { goalDraft = it.take(140) },
                placeholder = { Text("PROJECT GOAL OR RESEARCH NOTES...") },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = EmeraldLibrarian,
                    unfocusedBorderColor = BrassGold,
                    focusedTextColor = InkCharcoal,
                    unfocusedTextColor = InkCharcoal
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("project_goal_input")
            )
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = {
                    if (titleDraft.isNotBlank()) {
                        onAddProject(titleDraft, selectedCategory.displayName, goalDraft)
                        titleDraft = ""
                        goalDraft = ""
                    }
                },
                enabled = titleDraft.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldLibrarian),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .testTag("add_project_button")
            ) {
                Text(
                    text = "SAVE PROJECT",
                    fontFamily = PlusJakartaSansFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = Color.White
                )
            }
        }
    }

    if (projects.isEmpty()) {
        Surface(
            color = ParchmentSoft,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, ParchmentAged, RoundedCornerShape(12.dp))
        ) {
            Text(
                text = "NO PROJECTS YET. CREATE YOUR FIRST LIBRARY RESEARCH OR BOOK PROJECT ABOVE!",
                fontFamily = PlusJakartaSansFontFamily,
                fontSize = 11.5.sp,
                color = InkMuted,
                modifier = Modifier.padding(14.dp)
            )
        }
    } else {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            projects.forEach { project ->
                Surface(
                    color = ParchmentSoft,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            if (project.isCompleted) EmeraldLibrarian else ParchmentAged,
                            RoundedCornerShape(12.dp)
                        )
                        .testTag("project_card_${project.id}")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(10.dp)
                    ) {
                        IconButton(onClick = { onToggleProjectDone(project.id) }) {
                            Icon(
                                imageVector = if (project.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                contentDescription = "Toggle project completion",
                                tint = if (project.isCompleted) EmeraldLibrarian else BurgundyLeather
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = project.categoryName,
                                fontFamily = PlusJakartaSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.5.sp,
                                color = EmeraldLibrarian
                            )
                            Text(
                                text = project.title,
                                fontFamily = PlayfairDisplayFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                textDecoration = if (project.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                                color = MahoganyDark
                            )
                            if (project.goalNote.isNotBlank()) {
                                Text(
                                    text = project.goalNote,
                                    fontFamily = PlusJakartaSansFontFamily,
                                    fontSize = 11.sp,
                                    color = InkMuted
                                )
                            }
                        }
                        IconButton(onClick = { onDeleteProject(project.id) }) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = "Delete project",
                                tint = InkMuted
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ArtworkPane(
    savedArtworks: List<SavedArtwork>,
    onSaveArtwork: (String, List<ArtworkStroke>) -> Unit,
    onDeleteArtwork: (String) -> Unit
) {
    val palette = remember {
        listOf(
            0xFF6B1D2F, // Burgundy
            0xFF1E6B52, // Emerald
            0xFFC89B3C, // Gold
            0xFF284B7E, // Indigo
            0xFF261E1A  // Charcoal
        )
    }
    var selectedColorArgb by remember { mutableStateOf(palette.first()) }
    var artTitleDraft by remember { mutableStateOf("") }
    val activeStrokes = remember { mutableStateListOf<ArtworkStroke>() }
    var currentStrokePoints by remember { mutableStateOf<List<ArtworkPoint>>(emptyList()) }
    var canvasSize by remember { mutableStateOf(IntSize(1, 1)) }

    val galleryPlates = remember {
        listOf(
            LibraryGalleryPlate("hero", "GRAND LIBRARY HALL", "ARCHED TIMBER & GOLDEN READING GALLERY", R.drawable.img_library_hero, BurgundyLeather),
            LibraryGalleryPlate("eleanor", "LIBRARIAN ELEANOR", "HEAD ORGANIZER PORTRAIT", R.drawable.img_librarian_portrait, EmeraldLibrarian),
            LibraryGalleryPlate("tiles", "WHITE DESIGN TILES", "HIGH-RES MARBLE FLOOR FINISH", R.drawable.img_white_tile_floor, Color(0xFF4A6B82)),
            LibraryGalleryPlate("desk", "CARVED OAK DESK", "HIGH-RES STUDY TABLE GRAIN", R.drawable.img_wood_desk_surface, Color(0xFF8B5636)),
            LibraryGalleryPlate("shelf", "HERITAGE BOOKSHELF", "HIGH-RES WOODEN SHELF TEXTURE", R.drawable.img_wood_shelf_texture, Color(0xFF6A3A20)),
            LibraryGalleryPlate("emblem", "MR BOOK OFFICIAL LOGO", "SCHOLAR BOOK, LAMP & COMPASS CREST", R.drawable.img_mr_book_logo, BrassGold)
        )
    }
    var inspectedPlate by remember { mutableStateOf<LibraryGalleryPlate?>(null) }

    // Sketch Pad Card
    Surface(
        color = ParchmentSoft,
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BrassGold, RoundedCornerShape(14.dp))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Brush, contentDescription = null, tint = BurgundyLeather, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "LIBRARY ARTWORK SKETCH PAD",
                        fontFamily = PlayfairDisplayFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.5.sp,
                        color = MahoganyDark
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    palette.forEach { colorVal ->
                        val isChosen = colorVal == selectedColorArgb
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(Color(colorVal))
                                .border(
                                    width = if (isChosen) 2.5.dp else 1.dp,
                                    color = if (isChosen) BrassGold else Color.White,
                                    shape = CircleShape
                                )
                                .clickable { selectedColorArgb = colorVal }
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(148.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(ParchmentCream)
                    .border(1.5.dp, BrassGold, RoundedCornerShape(12.dp))
                    .clipToBounds()
                    .onSizeChanged { canvasSize = it }
                    .pointerInput(selectedColorArgb) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                val w = canvasSize.width.coerceAtLeast(1).toFloat()
                                val h = canvasSize.height.coerceAtLeast(1).toFloat()
                                currentStrokePoints = listOf(ArtworkPoint((offset.x / w).coerceIn(0f, 1f), (offset.y / h).coerceIn(0f, 1f)))
                            },
                            onDrag = { change, _ ->
                                change.consume()
                                val w = canvasSize.width.coerceAtLeast(1).toFloat()
                                val h = canvasSize.height.coerceAtLeast(1).toFloat()
                                currentStrokePoints = currentStrokePoints + ArtworkPoint(
                                    (change.position.x / w).coerceIn(0f, 1f),
                                    (change.position.y / h).coerceIn(0f, 1f)
                                )
                            },
                            onDragEnd = {
                                if (currentStrokePoints.isNotEmpty()) {
                                    activeStrokes.add(ArtworkStroke(selectedColorArgb, currentStrokePoints))
                                    currentStrokePoints = emptyList()
                                }
                            },
                            onDragCancel = {
                                currentStrokePoints = emptyList()
                            }
                        )
                    }
                    .testTag("artwork_sketch_canvas")
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val renderStroke: (ArtworkStroke) -> Unit = { stroke ->
                        if (stroke.points.size == 1) {
                            val pt = stroke.points.first()
                            drawCircle(
                                color = Color(stroke.colorArgb),
                                radius = 5f,
                                center = Offset(pt.x * size.width, pt.y * size.height)
                            )
                        } else if (stroke.points.size > 1) {
                            val path = Path().apply {
                                val first = stroke.points.first()
                                moveTo(first.x * size.width, first.y * size.height)
                                stroke.points.drop(1).forEach { p ->
                                    lineTo(p.x * size.width, p.y * size.height)
                                }
                            }
                            drawPath(
                                path = path,
                                color = Color(stroke.colorArgb),
                                style = Stroke(width = 6f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                            )
                        }
                    }
                    activeStrokes.forEach(renderStroke)
                    if (currentStrokePoints.isNotEmpty()) {
                        renderStroke(ArtworkStroke(selectedColorArgb, currentStrokePoints))
                    }
                }
                if (activeStrokes.isEmpty() && currentStrokePoints.isEmpty()) {
                    Text(
                        text = "DRAW YOUR ARTWORK HERE WITH YOUR FINGER",
                        fontFamily = PlusJakartaSansFontFamily,
                        fontSize = 11.sp,
                        color = InkMuted,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = artTitleDraft,
                    onValueChange = { artTitleDraft = it.take(40) },
                    placeholder = { Text("ARTWORK TITLE...") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BurgundyLeather,
                        unfocusedBorderColor = BrassGold,
                        focusedTextColor = InkCharcoal,
                        unfocusedTextColor = InkCharcoal
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("artwork_title_input")
                )
                OutlinedButton(
                    onClick = {
                        activeStrokes.clear()
                        currentStrokePoints = emptyList()
                    },
                    modifier = Modifier.height(50.dp)
                ) {
                    Text("CLEAR", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = InkCharcoal)
                }
                Button(
                    onClick = {
                        if (activeStrokes.isNotEmpty()) {
                            onSaveArtwork(artTitleDraft, activeStrokes.toList())
                            activeStrokes.clear()
                            artTitleDraft = ""
                        }
                    },
                    enabled = activeStrokes.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(containerColor = BurgundyLeather),
                    modifier = Modifier
                        .height(50.dp)
                        .testTag("save_artwork_button")
                ) {
                    Text("SAVE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ParchmentCream)
                }
            }
        }
    }

    if (savedArtworks.isNotEmpty()) {
        Text(
            text = "SAVED SKETCHES (${savedArtworks.size})",
            fontFamily = PlusJakartaSansFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = InkCharcoal
        )
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(savedArtworks, key = { it.id }) { art ->
                Surface(
                    color = ParchmentSoft,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .width(140.dp)
                        .border(1.dp, BrassGold, RoundedCornerShape(12.dp))
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(76.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(ParchmentCream)
                                .border(1.dp, ParchmentAged, RoundedCornerShape(8.dp))
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                art.strokes.forEach { stroke ->
                                    if (stroke.points.size > 1) {
                                        val path = Path().apply {
                                            val first = stroke.points.first()
                                            moveTo(first.x * size.width, first.y * size.height)
                                            stroke.points.drop(1).forEach { p ->
                                                lineTo(p.x * size.width, p.y * size.height)
                                            }
                                        }
                                        drawPath(
                                            path = path,
                                            color = Color(stroke.colorArgb),
                                            style = Stroke(width = 3.5f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(Modifier.height(4.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = art.title,
                                fontFamily = PlusJakartaSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                color = MahoganyDark,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = { onDeleteArtwork(art.id) },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DeleteOutline,
                                    contentDescription = "Delete artwork",
                                    tint = InkMuted,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // High-Resolution Library Artwork Plates Gallery
    Text(
        text = "LIBRARY HIGH-RES ARTWORK GALLERY",
        fontFamily = PlusJakartaSansFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        color = InkCharcoal
    )
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(galleryPlates, key = { it.id }) { plate ->
            val selected = inspectedPlate?.id == plate.id
            Surface(
                color = ParchmentSoft,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .width(136.dp)
                    .border(
                        width = if (selected) 2.dp else 1.dp,
                        color = if (selected) plate.accentColor else BrassGold,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable { inspectedPlate = if (selected) null else plate }
            ) {
                Column(modifier = Modifier.padding(6.dp)) {
                    Image(
                        painter = painterResource(id = plate.drawableRes),
                        contentDescription = plate.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(74.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = plate.title,
                        fontFamily = PlusJakartaSansFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        color = MahoganyDark,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = plate.subtitle,
                        fontFamily = PlusJakartaSansFontFamily,
                        fontSize = 8.5.sp,
                        color = InkMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }

    inspectedPlate?.let { plate ->
        Surface(
            color = ParchmentSoft,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.5.dp, plate.accentColor, RoundedCornerShape(14.dp))
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Image(
                    painter = painterResource(id = plate.drawableRes),
                    contentDescription = plate.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(145.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = plate.title,
                    fontFamily = PlayfairDisplayFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MahoganyDark
                )
                Text(
                    text = plate.subtitle,
                    fontFamily = PlusJakartaSansFontFamily,
                    fontSize = 11.sp,
                    color = InkMuted
                )
            }
        }
    }
}
