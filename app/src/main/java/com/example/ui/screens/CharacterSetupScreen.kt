package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.LibraryFloorLayout
import com.example.model.CharacterAvatar
import com.example.ui.components.CharacterPreviewSprite
import com.example.ui.components.MrBookLogoBadge
import com.example.ui.theme.BrassBright
import com.example.ui.theme.BrassGold
import com.example.ui.theme.BurgundyLeather
import com.example.ui.theme.InkCharcoal
import com.example.ui.theme.InkMuted
import com.example.ui.theme.MahoganyDark
import com.example.ui.theme.ParchmentAged
import com.example.ui.theme.ParchmentCream
import com.example.ui.theme.ParchmentSoft
import com.example.ui.theme.PlayfairDisplayFontFamily
import com.example.ui.theme.PlusJakartaSansFontFamily

@Composable
fun CharacterSetupScreen(
    initialName: String,
    initialAvatarId: String,
    initialCloakIndex: Int,
    isEditingExisting: Boolean,
    onConfirmCharacter: (name: String, avatarId: String, cloakIndex: Int) -> Unit,
    onCancelEdit: (() -> Unit)? = null
) {
    var nameInput by remember(initialName) { mutableStateOf(initialName) }
    var selectedAvatar by remember(initialAvatarId) {
        mutableStateOf(CharacterAvatar.entries.firstOrNull { it.id == initialAvatarId } ?: CharacterAvatar.SCHOLAR)
    }
    var selectedCloakIndex by remember(initialCloakIndex) {
        mutableIntStateOf(initialCloakIndex.coerceIn(0, LibraryFloorLayout.cloakColors.lastIndex))
    }

    val selectedCloakColor = LibraryFloorLayout.cloakColors[selectedCloakIndex].second

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MahoganyDark)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .widthIn(max = 580.dp)
        ) {
            // Cozy Library Hero Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(175.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .border(2.dp, BrassGold.copy(alpha = 0.7f), RoundedCornerShape(22.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_library_hero),
                    contentDescription = "MR book Grand Library Hall",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0x331B0E0A), Color(0xCC1B0E0A))
                            )
                        )
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    MrBookLogoBadge(size = 58.dp, tag = "setup_app_logo")
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "MR BOOK",
                            fontFamily = PlayfairDisplayFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp,
                            color = BrassBright
                        )
                        Text(
                            text = "SCHOOL TO PROFESSIONAL • INTERACTIVE STUDY LIBRARY",
                            fontFamily = PlusJakartaSansFontFamily,
                            fontSize = 12.5.sp,
                            color = ParchmentCream
                        )
                    }
                }
            }

            Spacer(Modifier.height(14.dp))

            // Character Creation Card
            Surface(
                color = ParchmentCream,
                shape = RoundedCornerShape(22.dp),
                shadowElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.5.dp, BrassGold.copy(alpha = 0.6f), RoundedCornerShape(22.dp))
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(18.dp)
                ) {
                    Text(
                        text = if (isEditingExisting) "CUSTOMIZE YOUR READER" else "CHOOSE & NAME YOUR READER",
                        fontFamily = PlayfairDisplayFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 21.sp,
                        color = BurgundyLeather,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "WALK BETWEEN NUMBERED SHELVES, PICK BOOKS, AND STUDY UNDER GLOWING DESK LAMPS.",
                        fontFamily = PlusJakartaSansFontFamily,
                        fontSize = 12.5.sp,
                        color = InkMuted,
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(14.dp))

                    // Live Character Preview Stage
                    Surface(
                        color = ParchmentSoft,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, ParchmentAged, RoundedCornerShape(16.dp))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(12.dp)
                        ) {
                            CharacterPreviewSprite(
                                avatar = selectedAvatar,
                                cloakColor = selectedCloakColor,
                                size = 76.dp,
                                carriedBooksCount = 2
                            )
                            Spacer(Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = nameInput.ifBlank { "YOUR READER" }.uppercase(),
                                    fontFamily = PlayfairDisplayFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = InkCharcoal
                                )
                                Text(
                                    text = selectedAvatar.title,
                                    fontFamily = PlusJakartaSansFontFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp,
                                    color = BurgundyLeather
                                )
                                Spacer(Modifier.height(3.dp))
                                Text(
                                    text = selectedAvatar.bio,
                                    fontFamily = PlusJakartaSansFontFamily,
                                    fontSize = 11.5.sp,
                                    color = InkMuted
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(14.dp))

                    // Name Input Field
                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = { if (it.length <= 20) nameInput = it },
                        label = { Text("CHARACTER NAME") },
                        placeholder = { Text("E.G., ARIA, LEO, MAYA...") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BurgundyLeather,
                            unfocusedBorderColor = BrassGold,
                            focusedLabelColor = BurgundyLeather,
                            focusedTextColor = InkCharcoal,
                            unfocusedTextColor = InkCharcoal
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("character_name_input")
                    )

                    Spacer(Modifier.height(14.dp))

                    // 4 Character Archetype Choices
                    Text(
                        text = "SELECT CHARACTER STYLE",
                        fontFamily = PlusJakartaSansFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = InkCharcoal,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CharacterAvatar.entries.forEach { avatar ->
                            val isSelected = avatar == selectedAvatar
                            Surface(
                                color = if (isSelected) BurgundyLeather.copy(alpha = 0.12f) else ParchmentSoft,
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .border(
                                        width = if (isSelected) 2.dp else 1.dp,
                                        color = if (isSelected) BurgundyLeather else ParchmentAged,
                                        shape = RoundedCornerShape(14.dp)
                                    )
                                    .clickable { selectedAvatar = avatar }
                                    .testTag("avatar_option_${avatar.id}")
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
                                ) {
                                    CharacterPreviewSprite(
                                        avatar = avatar,
                                        cloakColor = selectedCloakColor,
                                        size = 46.dp,
                                        carriedBooksCount = 0
                                    )
                                    Spacer(Modifier.height(2.dp))
                                    Text(
                                        text = avatar.id.uppercase(),
                                        fontFamily = PlusJakartaSansFontFamily,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 11.sp,
                                        color = InkCharcoal
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(14.dp))

                    // Cloak Color Swatches
                    Text(
                        text = "CLOAK ACCENT COLOR",
                        fontFamily = PlusJakartaSansFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = InkCharcoal,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        LibraryFloorLayout.cloakColors.forEachIndexed { index, (label, color) ->
                            val selected = index == selectedCloakIndex
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .border(
                                        width = if (selected) 3.dp else 1.dp,
                                        color = if (selected) BrassBright else Color.White.copy(alpha = 0.4f),
                                        shape = CircleShape
                                    )
                                    .clickable { selectedCloakIndex = index }
                                    .testTag("cloak_color_$index")
                            ) {
                                if (selected) {
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .clip(CircleShape)
                                            .background(ParchmentCream)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(18.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (onCancelEdit != null) {
                            Button(
                                onClick = onCancelEdit,
                                colors = ButtonDefaults.buttonColors(containerColor = ParchmentAged),
                                modifier = Modifier.weight(1f).height(50.dp)
                            ) {
                                Text("CANCEL", color = InkCharcoal, fontWeight = FontWeight.SemiBold)
                            }
                        }
                        Button(
                            onClick = {
                                onConfirmCharacter(
                                    nameInput.trim().ifBlank { "READER" }.uppercase(),
                                    selectedAvatar.id,
                                    selectedCloakIndex
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = BurgundyLeather),
                            modifier = Modifier
                                .weight(1.5f)
                                .height(50.dp)
                                .testTag("enter_library_button")
                        ) {
                            Icon(Icons.Default.AutoStories, contentDescription = null, tint = ParchmentCream)
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = if (isEditingExisting) "SAVE READER" else "ENTER LIBRARY",
                                color = ParchmentCream,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
