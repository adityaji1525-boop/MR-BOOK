package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Custom3DPart
import com.example.model.CustomPrimitiveShape
import com.example.model.ScienceModelTemplate
import com.example.model.ScienceProjectLabState
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
import java.util.UUID
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun ScienceProjectHelperPane(
    customTemplates: List<UserScienceTemplate>,
    onSaveCustomTemplate: (UserScienceTemplate) -> Unit,
    onDeleteCustomTemplate: (String) -> Unit,
    onSaveToProjects: (title: String, categoryName: String, goalNote: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var labState by remember { mutableStateOf(ScienceProjectLabState()) }
    var activePart by remember { mutableIntStateOf(0) } // 0 = TEXT & EFFECTS, 1 = 3D MODEL, 2 = WORK & ERRORS
    var isSimulationRunning by remember { mutableStateOf(false) }
    var showTemplateBuilder by remember { mutableStateOf(false) }
    var editingTemplate by remember { mutableStateOf<UserScienceTemplate?>(null) }

    val partColors = remember {
        listOf(
            0xFFD49B35, // Gold
            0xFF1E6B52, // Emerald
            0xFF28588A, // Cobalt
            0xFFA83226, // Crimson
            0xFFE2E8F0  // Silver
        )
    }
    var selectedPartColor by remember { mutableStateOf(partColors.first()) }

    val errors = remember(labState) { labState.evaluateErrors() }
    val dynamicEffects = remember(labState) { labState.computeDynamicTextEffects() }

    if (showTemplateBuilder) {
        CustomTemplateBuilderDialog(
            initialTemplate = editingTemplate,
            onSaveTemplate = { userTmpl ->
                onSaveCustomTemplate(userTmpl)
                isSimulationRunning = false
                labState = ScienceProjectLabState(
                    template = null,
                    activeCustomTemplate = userTmpl,
                    customParts = userTmpl.initialParts,
                    primaryAngleDeg = userTmpl.defaultAngleDeg
                )
                showTemplateBuilder = false
                editingTemplate = null
            },
            onDismiss = {
                showTemplateBuilder = false
                editingTemplate = null
            }
        )
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        // Science Model Picker + User Custom Templates + "+ YOUR TEMPLATE"
        Surface(
            color = ParchmentSoft,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, EmeraldLibrarian, RoundedCornerShape(14.dp))
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Icon(
                            imageVector = Icons.Default.Science,
                            contentDescription = null,
                            tint = EmeraldLibrarian,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "SCIENCE PROJECT & MODEL LAB",
                            fontFamily = PlayfairDisplayFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MahoganyDark
                        )
                    }
                    Surface(
                        color = BurgundyLeather,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .border(1.dp, BrassGold, RoundedCornerShape(8.dp))
                            .clickable {
                                editingTemplate = null
                                showTemplateBuilder = true
                            }
                            .testTag("open_custom_template_builder")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = ParchmentCream, modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(3.dp))
                            Text(
                                text = "YOUR TEMPLATE",
                                fontFamily = PlusJakartaSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.5.sp,
                                color = ParchmentCream
                            )
                        }
                    }
                }

                if (customTemplates.isNotEmpty()) {
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = "YOUR MODEL TEMPLATES (${customTemplates.size})",
                        fontFamily = PlusJakartaSansFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        color = BurgundyLeather
                    )
                    Spacer(Modifier.height(4.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(customTemplates, key = { it.id }) { userTmpl ->
                            val selected = labState.activeCustomTemplate?.id == userTmpl.id
                            Surface(
                                color = if (selected) BurgundyLeather else ParchmentCream,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .border(1.dp, BurgundyLeather, RoundedCornerShape(8.dp))
                                    .clickable {
                                        isSimulationRunning = false
                                        labState = ScienceProjectLabState(
                                            template = null,
                                            activeCustomTemplate = userTmpl,
                                            customParts = userTmpl.initialParts,
                                            primaryAngleDeg = userTmpl.defaultAngleDeg
                                        )
                                    }
                                    .testTag("user_template_${userTmpl.id}")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(start = 8.dp, end = 4.dp, top = 4.dp, bottom = 4.dp)
                                ) {
                                    Text(
                                        text = userTmpl.title,
                                        fontFamily = PlusJakartaSansFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp,
                                        color = if (selected) ParchmentCream else MahoganyDark
                                    )
                                    IconButton(
                                        onClick = {
                                            editingTemplate = userTmpl
                                            showTemplateBuilder = true
                                        },
                                        modifier = Modifier.size(22.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Edit,
                                            contentDescription = "Edit custom template",
                                            tint = if (selected) ParchmentCream else EmeraldLibrarian,
                                            modifier = Modifier.size(13.dp)
                                        )
                                    }
                                    IconButton(
                                        onClick = {
                                            if (labState.activeCustomTemplate?.id == userTmpl.id) {
                                                labState = ScienceProjectLabState()
                                            }
                                            onDeleteCustomTemplate(userTmpl.id)
                                        },
                                        modifier = Modifier.size(22.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.DeleteOutline,
                                            contentDescription = "Delete custom template",
                                            tint = if (selected) ParchmentCream else InkMuted,
                                            modifier = Modifier.size(13.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(6.dp))
                Text(
                    text = "LIBRARY SCIENCE TEMPLATES",
                    fontFamily = PlusJakartaSansFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    color = InkMuted
                )
                Spacer(Modifier.height(4.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(ScienceModelTemplate.entries) { tmpl ->
                        val selected = labState.activeCustomTemplate == null && tmpl == labState.template
                        Surface(
                            color = if (selected) tmpl.accentColor else ParchmentCream,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .border(1.dp, tmpl.accentColor, RoundedCornerShape(8.dp))
                                .clickable {
                                    isSimulationRunning = false
                                    labState = ScienceProjectLabState(
                                        template = tmpl,
                                        activeCustomTemplate = null,
                                        customProjectName = labState.customProjectName,
                                        primaryAngleDeg = tmpl.defaultAngleDeg
                                    )
                                }
                                .testTag("science_template_${tmpl.id}")
                        ) {
                            Text(
                                text = tmpl.title,
                                fontFamily = PlusJakartaSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                color = if (selected) Color.White else InkCharcoal,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
                Spacer(Modifier.height(6.dp))
                OutlinedTextField(
                    value = labState.customProjectName,
                    onValueChange = { labState = labState.copy(customProjectName = it.take(50)) },
                    placeholder = { Text("OPTIONAL PROJECT TITLE OVERRIDE...") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EmeraldLibrarian,
                        unfocusedBorderColor = BrassGold,
                        focusedTextColor = InkCharcoal,
                        unfocusedTextColor = InkCharcoal
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("science_custom_name_input")
                )
            }
        }

        // 3-Part Selector: 1. TEXT & EFFECTS | 2. 3D MODEL | 3. WORK & ERRORS
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf(
                Triple("1. EFFECTS", Icons.Default.Description, "science_part_1_effects"),
                Triple("2. 3D MODEL", Icons.Default.ViewInAr, "science_part_2_3d"),
                Triple("3. WORK & FIX", Icons.Default.Build, "science_part_3_work_errors")
            ).forEachIndexed { idx, (label, icon, tag) ->
                val selected = activePart == idx
                Surface(
                    color = if (selected) EmeraldLibrarian else ParchmentSoft,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, EmeraldLibrarian, RoundedCornerShape(10.dp))
                        .clickable { activePart = idx }
                        .testTag(tag)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 9.dp)
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = if (selected) Color.White else EmeraldLibrarian,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = label,
                            fontFamily = PlusJakartaSansFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = if (selected) Color.White else MahoganyDark,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }

        when (activePart) {
            // PART 1: SUGGESTIONS & TEXT-FORM EFFECTS
            0 -> {
                Surface(
                    color = ParchmentSoft,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, BrassGold, RoundedCornerShape(14.dp))
                        .testTag("science_part_1_panel")
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(
                            text = "${labState.activeTitle} • ${labState.activeCategoryTag}",
                            fontFamily = PlayfairDisplayFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.5.sp,
                            color = BurgundyLeather
                        )

                        // Expected Effects in Text Form
                        Surface(
                            color = Color(0xFFE7F2ED),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = "SUGGESTED EFFECTS (TEXT FORM)",
                                    fontFamily = PlusJakartaSansFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.5.sp,
                                    color = EmeraldLibrarian
                                )
                                dynamicEffects.forEach { effect ->
                                    Text(
                                        text = "• $effect",
                                        fontFamily = PlusJakartaSansFontFamily,
                                        fontSize = 11.sp,
                                        lineHeight = 15.sp,
                                        color = MahoganyDark
                                    )
                                }
                            }
                        }

                        // Scientific Principle
                        Text(
                            text = "SCIENTIFIC PRINCIPLE",
                            fontFamily = PlusJakartaSansFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.5.sp,
                            color = InkCharcoal
                        )
                        Text(
                            text = labState.activePrinciple,
                            fontFamily = PlusJakartaSansFontFamily,
                            fontSize = 11.sp,
                            lineHeight = 15.sp,
                            color = InkMuted
                        )

                        // Suggested Materials
                        Text(
                            text = "SUGGESTED MATERIALS",
                            fontFamily = PlusJakartaSansFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.5.sp,
                            color = InkCharcoal
                        )
                        labState.activeMaterials.forEach { mat ->
                            Text(
                                text = "• $mat",
                                fontFamily = PlusJakartaSansFontFamily,
                                fontSize = 11.sp,
                                color = MahoganyDark
                            )
                        }

                        // Build Steps
                        Text(
                            text = "BUILD GUIDE STEPS",
                            fontFamily = PlusJakartaSansFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.5.sp,
                            color = InkCharcoal
                        )
                        labState.activeBuildSteps.forEach { step ->
                            Text(
                                text = step,
                                fontFamily = PlusJakartaSansFontFamily,
                                fontSize = 11.sp,
                                color = InkCharcoal
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                            OutlinedButton(
                                onClick = { activePart = 1 },
                                modifier = Modifier.weight(1f).height(42.dp)
                            ) {
                                Text("OPEN 3D MODEL", fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = BurgundyLeather)
                            }
                            Button(
                                onClick = {
                                    onSaveToProjects(
                                        labState.activeTitle,
                                        "SCIENCE",
                                        "${labState.activeCategoryTag} • ${errors.size} ISSUE(S)"
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldLibrarian),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(42.dp)
                                    .testTag("save_science_to_projects_button")
                            ) {
                                Text("SAVE PROJECT", fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }
            }

            // PART 2: MAKE 3D MODEL
            1 -> {
                Surface(
                    color = ParchmentSoft,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, BrassGold, RoundedCornerShape(14.dp))
                        .testTag("science_part_2_panel")
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(
                            text = "3D MODEL BUILDER • ${labState.activeTitle}",
                            fontFamily = PlayfairDisplayFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MahoganyDark
                        )

                        ScienceModel3DViewport(
                            state = labState,
                            isWorkingAnimationActive = isSimulationRunning,
                            errorCount = if (isSimulationRunning) errors.size else 0,
                            onRotateDelta = { dYaw, dPitch ->
                                labState = labState.copy(
                                    yawDeg = (labState.yawDeg + dYaw) % 360f,
                                    pitchDeg = (labState.pitchDeg + dPitch).coerceIn(-75f, 75f)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(215.dp)
                        )

                        // View Controls: Exploded, Wireframe, Reset
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            ToggleChip(
                                text = if (labState.isExploded) "ASSEMBLED" else "EXPLODED VIEW",
                                selected = labState.isExploded,
                                onClick = { labState = labState.copy(isExploded = !labState.isExploded) },
                                modifier = Modifier.weight(1f)
                            )
                            ToggleChip(
                                text = if (labState.showWireframe) "SOLID 3D" else "WIREFRAME",
                                selected = labState.showWireframe,
                                onClick = { labState = labState.copy(showWireframe = !labState.showWireframe) },
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedButton(
                                onClick = {
                                    labState = labState.copy(yawDeg = 28f, pitchDeg = 22f, zoom = 1.0f)
                                },
                                modifier = Modifier.height(36.dp)
                            ) {
                                Icon(Icons.Default.RestartAlt, contentDescription = "Reset 3D camera", modifier = Modifier.size(15.dp))
                                Spacer(Modifier.width(3.dp))
                                Text("RESET", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        // Zoom Slider
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "ZOOM",
                                fontFamily = PlusJakartaSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = InkCharcoal
                            )
                            Spacer(Modifier.width(8.dp))
                            Slider(
                                value = labState.zoom,
                                onValueChange = { labState = labState.copy(zoom = it) },
                                valueRange = 0.65f..1.45f,
                                colors = SliderDefaults.colors(
                                    thumbColor = BurgundyLeather,
                                    activeTrackColor = BrassGold
                                ),
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "${(labState.zoom * 100).roundToInt()}%",
                                fontFamily = PlusJakartaSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = InkMuted
                            )
                        }

                        // Color & Custom 3D Parts onto Model
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "ADD 3D PARTS (${labState.customParts.size})",
                                fontFamily = PlusJakartaSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = InkCharcoal
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                                partColors.forEach { colorVal ->
                                    val chosen = colorVal == selectedPartColor
                                    Box(
                                        modifier = Modifier
                                            .size(18.dp)
                                            .clip(CircleShape)
                                            .background(Color(colorVal))
                                            .border(
                                                width = if (chosen) 2.dp else 1.dp,
                                                color = if (chosen) MahoganyDark else Color.White,
                                                shape = CircleShape
                                            )
                                            .clickable { selectedPartColor = colorVal }
                                    )
                                }
                            }
                        }
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(CustomPrimitiveShape.entries) { shape ->
                                Surface(
                                    color = BurgundyLeather,
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .clickable {
                                            val idx = labState.customParts.size
                                            val angle = idx * 1.15f
                                            val newPart = Custom3DPart(
                                                id = UUID.randomUUID().toString(),
                                                shape = shape,
                                                offsetX = cos(angle) * 0.56f,
                                                offsetY = -0.25f + (idx % 3) * 0.28f,
                                                offsetZ = sin(angle) * 0.56f,
                                                colorArgb = selectedPartColor
                                            )
                                            val nextParts = labState.customParts + newPart
                                            labState = labState.copy(customParts = nextParts)
                                            labState.activeCustomTemplate?.let { activeTmpl ->
                                                onSaveCustomTemplate(activeTmpl.copy(initialParts = nextParts))
                                            }
                                        }
                                        .testTag("add_3d_part_${shape.name}")
                                ) {
                                    Text(
                                        text = "+ ${shape.label}",
                                        fontFamily = PlusJakartaSansFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp,
                                        color = ParchmentCream,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp)
                                    )
                                }
                            }
                            if (labState.customParts.isNotEmpty()) {
                                item {
                                    Surface(
                                        color = ParchmentCream,
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier
                                            .border(1.dp, InkMuted, RoundedCornerShape(8.dp))
                                            .clickable {
                                                labState = labState.copy(customParts = emptyList())
                                                labState.activeCustomTemplate?.let { activeTmpl ->
                                                    onSaveCustomTemplate(activeTmpl.copy(initialParts = emptyList()))
                                                }
                                            }
                                    ) {
                                        Text(
                                            text = "CLEAR PARTS",
                                            fontFamily = PlusJakartaSansFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp,
                                            color = InkCharcoal,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // PART 3: MAKE IT WORKING & CHECK ERROR IN PROJECT
            else -> {
                Surface(
                    color = ParchmentSoft,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, BrassGold, RoundedCornerShape(14.dp))
                        .testTag("science_part_3_panel")
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(
                            text = "MAKE IT WORKING & ERROR CHECKER",
                            fontFamily = PlayfairDisplayFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.5.sp,
                            color = MahoganyDark
                        )

                        // Live 3D Working Preview
                        ScienceModel3DViewport(
                            state = labState,
                            isWorkingAnimationActive = isSimulationRunning,
                            errorCount = errors.size,
                            onRotateDelta = { dYaw, dPitch ->
                                labState = labState.copy(
                                    yawDeg = (labState.yawDeg + dYaw) % 360f,
                                    pitchDeg = (labState.pitchDeg + dPitch).coerceIn(-75f, 75f)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(165.dp)
                        )

                        // Working Switches
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            DiagnosticSwitchTile(
                                label = "POWER / TRIGGER",
                                checked = labState.isPowerOrTriggerOn,
                                onCheckedChange = { labState = labState.copy(isPowerOrTriggerOn = it) },
                                modifier = Modifier.weight(1f)
                            )
                            DiagnosticSwitchTile(
                                label = "SEAL / WIRING",
                                checked = labState.isSealedOrWired,
                                onCheckedChange = { labState = labState.copy(isSealedOrWired = it) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            DiagnosticSwitchTile(
                                label = "ALIGNMENT",
                                checked = labState.isComponentAligned,
                                onCheckedChange = { labState = labState.copy(isComponentAligned = it) },
                                modifier = Modifier.weight(1f)
                            )
                            DiagnosticSwitchTile(
                                label = "SAFETY GUARD",
                                checked = labState.isSafetyGuardOn,
                                onCheckedChange = { labState = labState.copy(isSafetyGuardOn = it) },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        // Drive Level & Geometry Angle Sliders
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = labState.activeDriveLabel,
                                fontFamily = PlusJakartaSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.5.sp,
                                color = InkCharcoal,
                                modifier = Modifier.width(96.dp)
                            )
                            Slider(
                                value = labState.driveLevel,
                                onValueChange = { labState = labState.copy(driveLevel = it) },
                                valueRange = 0f..1f,
                                colors = SliderDefaults.colors(
                                    thumbColor = EmeraldLibrarian,
                                    activeTrackColor = EmeraldLibrarian
                                ),
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "${(labState.driveLevel * 100).roundToInt()}%",
                                fontFamily = PlusJakartaSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.5.sp,
                                color = MahoganyDark
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = labState.activeAngleLabel,
                                fontFamily = PlusJakartaSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.5.sp,
                                color = InkCharcoal,
                                modifier = Modifier.width(96.dp)
                            )
                            Slider(
                                value = labState.primaryAngleDeg.toFloat(),
                                onValueChange = { labState = labState.copy(primaryAngleDeg = it.roundToInt()) },
                                valueRange = 0f..90f,
                                colors = SliderDefaults.colors(
                                    thumbColor = BurgundyLeather,
                                    activeTrackColor = BrassGold
                                ),
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "${labState.primaryAngleDeg}°",
                                fontFamily = PlusJakartaSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.5.sp,
                                color = MahoganyDark
                            )
                        }

                        // Run Test & Make Working Button
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                            Button(
                                onClick = {
                                    labState = labState.copy(hasRunCheck = true)
                                    isSimulationRunning = errors.isEmpty()
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (errors.isEmpty()) EmeraldLibrarian else BurgundyLeather
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                                    .testTag("run_project_check_button")
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    text = if (errors.isEmpty()) "MAKE IT WORKING" else "CHECK ERRORS (${errors.size})",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            if (errors.isNotEmpty()) {
                                OutlinedButton(
                                    onClick = {
                                        val nextParts = if (labState.customParts.isEmpty() && labState.effectiveChassis.id == "blank") {
                                            listOf(
                                                Custom3DPart(
                                                    id = UUID.randomUUID().toString(),
                                                    shape = CustomPrimitiveShape.SPHERE,
                                                    offsetX = 0f,
                                                    offsetY = 0f,
                                                    offsetZ = 0f,
                                                    colorArgb = selectedPartColor
                                                )
                                            )
                                        } else {
                                            labState.customParts
                                        }
                                        labState = labState.copy(
                                            isPowerOrTriggerOn = true,
                                            isSealedOrWired = true,
                                            isComponentAligned = true,
                                            isSafetyGuardOn = true,
                                            driveLevel = maxOf(labState.driveLevel, 0.72f),
                                            primaryAngleDeg = labState.activeDefaultAngleDeg,
                                            customParts = nextParts,
                                            hasRunCheck = true
                                        )
                                        isSimulationRunning = true
                                    },
                                    modifier = Modifier
                                        .height(44.dp)
                                        .testTag("fix_all_errors_button")
                                ) {
                                    Text("FIX ALL", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = EmeraldLibrarian)
                                }
                            }
                        }

                        // Error Diagnostic Cards or Success Card
                        if (errors.isEmpty()) {
                            Surface(
                                color = Color(0xFFE1F3EA),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.5.dp, EmeraldLibrarian, RoundedCornerShape(12.dp))
                                    .testTag("project_working_success_card")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(12.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = EmeraldLibrarian,
                                        modifier = Modifier.size(22.dp)
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "0 ERRORS • PROJECT IS WORKING!",
                                            fontFamily = PlusJakartaSansFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = EmeraldLibrarian
                                        )
                                        Text(
                                            text = "${labState.activeTitle} OPERATING AT ${(labState.driveLevel * 100).roundToInt()}% OUTPUT (${labState.primaryAngleDeg}°).",
                                            fontFamily = PlusJakartaSansFontFamily,
                                            fontSize = 10.5.sp,
                                            color = MahoganyDark
                                        )
                                    }
                                }
                            }
                            Button(
                                onClick = {
                                    onSaveToProjects(
                                        labState.activeTitle,
                                        "SCIENCE",
                                        "WORKING VERIFIED • 0 ERRORS (${(labState.driveLevel * 100).roundToInt()}%)"
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldLibrarian),
                                modifier = Modifier.fillMaxWidth().height(40.dp)
                            ) {
                                Text("SAVE WORKING MODEL TO MY PROJECTS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        } else {
                            errors.forEach { err ->
                                Surface(
                                    color = Color(0xFFFBE9E7),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(1.dp, BurgundyLeather, RoundedCornerShape(10.dp))
                                        .testTag("project_error_${err.code}")
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(10.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ErrorOutline,
                                            contentDescription = null,
                                            tint = BurgundyLeather,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(Modifier.width(8.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = "${err.code}: ${err.title}",
                                                fontFamily = PlusJakartaSansFontFamily,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.sp,
                                                color = BurgundyLeather
                                            )
                                            Text(
                                                text = err.detail,
                                                fontFamily = PlusJakartaSansFontFamily,
                                                fontSize = 10.sp,
                                                color = MahoganyDark
                                            )
                                        }
                                        Spacer(Modifier.width(6.dp))
                                        Button(
                                            onClick = {
                                                labState = when (err.fixKey) {
                                                    "power" -> labState.copy(isPowerOrTriggerOn = true)
                                                    "wire" -> labState.copy(isSealedOrWired = true)
                                                    "align" -> labState.copy(isComponentAligned = true)
                                                    "safety" -> labState.copy(isSafetyGuardOn = true)
                                                    "drive" -> labState.copy(driveLevel = 0.72f)
                                                    "angle" -> labState.copy(primaryAngleDeg = labState.activeDefaultAngleDeg)
                                                    "add_part" -> labState.copy(
                                                        customParts = labState.customParts + Custom3DPart(
                                                            id = UUID.randomUUID().toString(),
                                                            shape = CustomPrimitiveShape.SPHERE,
                                                            offsetX = 0f,
                                                            offsetY = 0f,
                                                            offsetZ = 0f,
                                                            colorArgb = selectedPartColor
                                                        )
                                                    )
                                                    else -> labState
                                                }
                                                if (labState.evaluateErrors().isEmpty()) {
                                                    isSimulationRunning = true
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = BurgundyLeather),
                                            modifier = Modifier.height(32.dp)
                                        ) {
                                            Text(err.fixLabel, fontSize = 9.5.sp, fontWeight = FontWeight.Bold, color = ParchmentCream)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ToggleChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = if (selected) BurgundyLeather else ParchmentCream,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .height(36.dp)
            .border(1.dp, if (selected) BrassGold else ParchmentAged, RoundedCornerShape(8.dp))
            .clickable { onClick() }
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(horizontal = 6.dp)) {
            Text(
                text = text,
                fontFamily = PlusJakartaSansFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 9.5.sp,
                color = if (selected) ParchmentCream else InkCharcoal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun DiagnosticSwitchTile(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = ParchmentCream,
        shape = RoundedCornerShape(10.dp),
        modifier = modifier.border(1.dp, if (checked) EmeraldLibrarian else BurgundyLeather, RoundedCornerShape(10.dp))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = label,
                fontFamily = PlusJakartaSansFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 9.5.sp,
                color = InkCharcoal,
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = EmeraldLibrarian,
                    uncheckedThumbColor = ParchmentCream,
                    uncheckedTrackColor = BurgundyLeather
                )
            )
        }
    }
}
