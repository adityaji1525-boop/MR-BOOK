package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.Custom3DPart
import com.example.model.CustomBaseChassis
import com.example.model.CustomPrimitiveShape
import com.example.model.UserScienceTemplate
import com.example.ui.theme.BrassGold
import com.example.ui.theme.BurgundyLeather
import com.example.ui.theme.EmeraldLibrarian
import com.example.ui.theme.InkCharcoal
import com.example.ui.theme.InkMuted
import com.example.ui.theme.MahoganyDark
import com.example.ui.theme.ParchmentCream
import com.example.ui.theme.ParchmentSoft
import com.example.ui.theme.PlayfairDisplayFontFamily
import com.example.ui.theme.PlusJakartaSansFontFamily
import java.util.UUID

@Composable
fun CustomTemplateBuilderDialog(
    initialTemplate: UserScienceTemplate? = null,
    onSaveTemplate: (UserScienceTemplate) -> Unit,
    onDismiss: () -> Unit
) {
    val domains = remember {
        listOf("PHYSICS", "CHEMISTRY", "BIOLOGY", "ENGINEERING", "ASTRONOMY", "OPTICS", "EARTH SCIENCE")
    }
    var title by remember { mutableStateOf(initialTemplate?.title ?: "") }
    var selectedDomain by remember { mutableStateOf(initialTemplate?.domainTag ?: domains.first()) }
    var selectedChassis by remember { mutableStateOf(initialTemplate?.baseChassis ?: CustomBaseChassis.BLANK_WORKBENCH) }
    var materialsText by remember { mutableStateOf(initialTemplate?.materials?.joinToString(", ") ?: "") }
    var stepsText by remember { mutableStateOf(initialTemplate?.buildSteps?.joinToString("\n") ?: "") }
    var principleText by remember { mutableStateOf(initialTemplate?.scientificPrinciple ?: "") }
    var effectsText by remember { mutableStateOf(initialTemplate?.expectedEffects?.joinToString("\n") ?: "") }
    var driveLabel by remember { mutableStateOf(initialTemplate?.driveLabel ?: "POWER DRIVE") }
    var angleLabel by remember { mutableStateOf(initialTemplate?.angleLabel ?: "MODEL ANGLE") }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xAA160D09))
                .clickable { onDismiss() }
                .padding(14.dp),
            contentAlignment = Alignment.Center
        ) {
        Surface(
            color = ParchmentCream,
            shape = RoundedCornerShape(20.dp),
            shadowElevation = 14.dp,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 640.dp)
                .border(2.dp, EmeraldLibrarian, RoundedCornerShape(20.dp))
                .clickable(enabled = false) {}
                .testTag("custom_template_builder_dialog")
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Science, contentDescription = null, tint = EmeraldLibrarian)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "CREATE YOUR MODEL TEMPLATE",
                            fontFamily = PlayfairDisplayFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = MahoganyDark
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close template builder", tint = InkCharcoal)
                    }
                }

                Spacer(Modifier.height(8.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .verticalScroll(rememberScrollState())
                ) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it.take(50) },
                        label = { Text("MODEL TEMPLATE NAME *") },
                        placeholder = { Text("E.G. MAGNETIC TRAIN, SMART GREENHOUSE...") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EmeraldLibrarian,
                            unfocusedBorderColor = BrassGold,
                            focusedTextColor = InkCharcoal,
                            unfocusedTextColor = InkCharcoal
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("custom_template_title_input")
                    )

                    Text(
                        text = "SCIENCE SUBJECT DOMAIN",
                        fontFamily = PlusJakartaSansFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = InkCharcoal
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(domains) { dom ->
                            val selected = dom == selectedDomain
                            Surface(
                                color = if (selected) EmeraldLibrarian else ParchmentSoft,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .border(1.dp, EmeraldLibrarian, RoundedCornerShape(8.dp))
                                    .clickable { selectedDomain = dom }
                            ) {
                                Text(
                                    text = dom,
                                    fontFamily = PlusJakartaSansFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    color = if (selected) Color.White else InkCharcoal,
                                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    Text(
                        text = "3D BASE STRUCTURE / CHASSIS",
                        fontFamily = PlusJakartaSansFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = InkCharcoal
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(CustomBaseChassis.entries) { chassis ->
                            val selected = chassis == selectedChassis
                            Surface(
                                color = if (selected) BurgundyLeather else ParchmentSoft,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .border(1.dp, BrassGold, RoundedCornerShape(8.dp))
                                    .clickable { selectedChassis = chassis }
                                    .testTag("custom_chassis_${chassis.id}")
                            ) {
                                Text(
                                    text = chassis.label,
                                    fontFamily = PlusJakartaSansFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    color = if (selected) ParchmentCream else InkCharcoal,
                                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = materialsText,
                        onValueChange = { materialsText = it.take(300) },
                        label = { Text("MATERIALS NEEDED (COMMA OR NEW LINE)") },
                        placeholder = { Text("COIL, MAGNETS, LED, WOOD BASE...") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EmeraldLibrarian,
                            unfocusedBorderColor = BrassGold,
                            focusedTextColor = InkCharcoal,
                            unfocusedTextColor = InkCharcoal
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("custom_template_materials_input")
                    )

                    OutlinedTextField(
                        value = principleText,
                        onValueChange = { principleText = it.take(240) },
                        label = { Text("SCIENTIFIC PRINCIPLE / GOAL") },
                        placeholder = { Text("EXPLAINS HOW YOUR MODEL WORKS...") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EmeraldLibrarian,
                            unfocusedBorderColor = BrassGold,
                            focusedTextColor = InkCharcoal,
                            unfocusedTextColor = InkCharcoal
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("custom_template_principle_input")
                    )

                    OutlinedTextField(
                        value = stepsText,
                        onValueChange = { stepsText = it.take(360) },
                        label = { Text("BUILD STEPS (ONE PER LINE)") },
                        placeholder = { Text("1. BUILD FRAME\n2. WIRE SENSOR\n3. TEST...") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EmeraldLibrarian,
                            unfocusedBorderColor = BrassGold,
                            focusedTextColor = InkCharcoal,
                            unfocusedTextColor = InkCharcoal
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("custom_template_steps_input")
                    )

                    OutlinedTextField(
                        value = effectsText,
                        onValueChange = { effectsText = it.take(320) },
                        label = { Text("EXPECTED EFFECTS IN TEXT FORM") },
                        placeholder = { Text("MOTION, GLOW, OR REACTION EFFECTS...") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EmeraldLibrarian,
                            unfocusedBorderColor = BrassGold,
                            focusedTextColor = InkCharcoal,
                            unfocusedTextColor = InkCharcoal
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("custom_template_effects_input")
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = driveLabel,
                            onValueChange = { driveLabel = it.take(20) },
                            label = { Text("CONTROL SLIDER NAME") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = EmeraldLibrarian,
                                unfocusedBorderColor = BrassGold,
                                focusedTextColor = InkCharcoal,
                                unfocusedTextColor = InkCharcoal
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = angleLabel,
                            onValueChange = { angleLabel = it.take(20) },
                            label = { Text("ANGLE SLIDER NAME") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = EmeraldLibrarian,
                                unfocusedBorderColor = BrassGold,
                                focusedTextColor = InkCharcoal,
                                unfocusedTextColor = InkCharcoal
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(Modifier.height(10.dp))

                Button(
                    onClick = {
                        val cleanTitle = title.trim().uppercase()
                        if (cleanTitle.isNotBlank()) {
                            val parsedMaterials = materialsText
                                .split(",", "\n")
                                .map { it.trim().uppercase() }
                                .filter { it.isNotEmpty() }
                                .ifEmpty { listOf("CUSTOM BASE FRAME", "CORE SENSOR / ACTUATOR", "CONNECTOR WIRES") }

                            val parsedSteps = stepsText
                                .split("\n")
                                .map { it.trim().uppercase() }
                                .filter { it.isNotEmpty() }
                                .ifEmpty {
                                    listOf(
                                        "1. ASSEMBLE $cleanTitle ON THE ${selectedChassis.label} BASE.",
                                        "2. CONNECT MAIN WIRING, SEAL, AND ALIGNMENT PARTS.",
                                        "3. TEST IN PART 3 TO VERIFY 0 ERRORS AND WORKING MOTION."
                                    )
                                }

                            val parsedEffects = effectsText
                                .split("\n")
                                .map { it.trim().uppercase() }
                                .filter { it.isNotEmpty() }
                                .ifEmpty {
                                    listOf(
                                        "OBSERVED EFFECT: $cleanTitle DEMONSTRATES ACTIVE $selectedDomain BEHAVIOR.",
                                        "3D VISUAL EFFECT: BUILT ON ${selectedChassis.label} STRUCTURE."
                                    )
                                }

                            val starterParts = if (selectedChassis == CustomBaseChassis.BLANK_WORKBENCH && initialTemplate == null) {
                                listOf(
                                    Custom3DPart(
                                        id = UUID.randomUUID().toString(),
                                        shape = CustomPrimitiveShape.CYLINDER,
                                        offsetX = 0f,
                                        offsetY = -0.25f,
                                        offsetZ = 0f,
                                        colorArgb = 0xFF1E6B52
                                    ),
                                    Custom3DPart(
                                        id = UUID.randomUUID().toString(),
                                        shape = CustomPrimitiveShape.SPHERE,
                                        offsetX = 0f,
                                        offsetY = 0.12f,
                                        offsetZ = 0f,
                                        colorArgb = 0xFFD49B35
                                    )
                                )
                            } else {
                                initialTemplate?.initialParts ?: emptyList()
                            }

                            onSaveTemplate(
                                UserScienceTemplate(
                                    id = initialTemplate?.id ?: UUID.randomUUID().toString(),
                                    title = cleanTitle,
                                    domainTag = selectedDomain,
                                    baseChassis = selectedChassis,
                                    materials = parsedMaterials,
                                    buildSteps = parsedSteps,
                                    scientificPrinciple = principleText.trim().uppercase()
                                        .ifBlank { "CUSTOM $selectedDomain MODEL DESIGNED BY STUDENT." },
                                    expectedEffects = parsedEffects,
                                    initialParts = starterParts,
                                    defaultAngleDeg = initialTemplate?.defaultAngleDeg ?: 45,
                                    angleLabel = angleLabel.trim().uppercase().ifBlank { "MODEL ANGLE" },
                                    driveLabel = driveLabel.trim().uppercase().ifBlank { "POWER DRIVE" }
                                )
                            )
                        }
                    },
                    enabled = title.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldLibrarian),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("save_custom_template_button")
                ) {
                    Text(
                        text = "USE & SAVE MODEL TEMPLATE",
                        fontFamily = PlusJakartaSansFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
    }
}
