package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TableRestaurant
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.model.LampAmbience
import com.example.model.TableCoverStyle
import com.example.ui.components.MrBookLogoBadge
import com.example.ui.theme.BrassBright
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
import kotlin.math.roundToInt

@Composable
fun AppSettingsSheet(
    characterName: String,
    isLampOn: Boolean,
    lampAmbience: LampAmbience,
    lampBrightness: Float,
    tableCover: TableCoverStyle,
    onToggleLamp: (Boolean) -> Unit,
    onSelectLampAmbience: (LampAmbience) -> Unit,
    onSetLampBrightness: (Float) -> Unit,
    onBrightnessChangeFinished: () -> Unit,
    onSelectTableCover: (TableCoverStyle) -> Unit,
    onOpenHelpMap: () -> Unit,
    onClose: () -> Unit
) {
    BackHandler { onClose() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x99160D09))
            .clickable { onClose() }
            .padding(14.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            color = ParchmentCream,
            shape = RoundedCornerShape(22.dp),
            shadowElevation = 14.dp,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 660.dp)
                .border(2.dp, BrassGold, RoundedCornerShape(22.dp))
                .clickable(enabled = false) {}
                .testTag("app_settings_sheet")
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        MrBookLogoBadge(size = 44.dp, tag = "settings_app_logo")
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "APP SETTINGS",
                                fontFamily = PlayfairDisplayFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 19.sp,
                                color = MahoganyDark
                            )
                            Text(
                                text = "THEME • TABLE COVER • HELP • ${characterName.uppercase()}",
                                fontFamily = PlusJakartaSansFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 10.sp,
                                color = InkMuted
                            )
                        }
                    }
                    IconButton(onClick = onClose, modifier = Modifier.testTag("close_app_settings")) {
                        Icon(Icons.Default.Close, contentDescription = "Close app settings", tint = InkCharcoal)
                    }
                }

                Spacer(Modifier.height(10.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    // 1. THEME (5 LAMP LIGHT COLORS, SWITCH & BRIGHTNESS)
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
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                    Icon(
                                        imageVector = Icons.Default.Lightbulb,
                                        contentDescription = null,
                                        tint = Color(0xFFB5691E),
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(Modifier.width(6.dp))
                                    Column {
                                        Text(
                                            text = "LIBRARY THEME & LAMP",
                                            fontFamily = PlayfairDisplayFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.5.sp,
                                            color = MahoganyDark
                                        )
                                        Text(
                                            text = if (isLampOn) {
                                                "${lampAmbience.label} • ${(lampBrightness * 100).roundToInt()}% GLOW"
                                            } else {
                                                "LAMP OFF • DIMMED EVENING THEME"
                                            },
                                            fontFamily = PlusJakartaSansFontFamily,
                                            fontSize = 10.5.sp,
                                            color = InkMuted
                                        )
                                    }
                                }
                                Switch(
                                    checked = isLampOn,
                                    onCheckedChange = onToggleLamp,
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = MahoganyDark,
                                        checkedTrackColor = lampAmbience.glowColor
                                    ),
                                    modifier = Modifier.testTag("settings_lamp_switch")
                                )
                            }

                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = "5 OPTIONAL LAMP LIGHT THEME COLORS",
                                fontFamily = PlusJakartaSansFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.5.sp,
                                color = InkCharcoal
                            )
                            Spacer(Modifier.height(6.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(LampAmbience.entries) { mode ->
                                    val selected = mode == lampAmbience && isLampOn
                                    Surface(
                                        color = if (selected) mode.glowColor else ParchmentCream,
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier
                                            .border(
                                                width = 1.5.dp,
                                                color = if (selected) MahoganyDark else BrassGold,
                                                shape = RoundedCornerShape(10.dp)
                                            )
                                            .clickable { onSelectLampAmbience(mode) }
                                            .testTag("lamp_theme_${mode.id}")
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(11.dp)
                                                    .clip(CircleShape)
                                                    .background(mode.glowColor)
                                                    .border(1.dp, MahoganyDark, CircleShape)
                                            )
                                            Spacer(Modifier.width(6.dp))
                                            Text(
                                                text = mode.label,
                                                fontFamily = PlusJakartaSansFontFamily,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.sp,
                                                color = MahoganyDark
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "BRIGHTNESS",
                                    fontFamily = PlusJakartaSansFontFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 11.5.sp,
                                    color = InkCharcoal
                                )
                                Spacer(Modifier.width(8.dp))
                                Slider(
                                    value = lampBrightness.coerceIn(0.25f, 1.0f),
                                    onValueChange = onSetLampBrightness,
                                    onValueChangeFinished = onBrightnessChangeFinished,
                                    valueRange = 0.25f..1.0f,
                                    colors = SliderDefaults.colors(
                                        thumbColor = BurgundyLeather,
                                        activeTrackColor = BrassGold
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("desk_lamp_brightness_slider")
                                )
                            }
                        }
                    }

                    // 3. WOODEN TABLE COVER / SHEET
                    Surface(
                        color = ParchmentSoft,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, BrassGold, RoundedCornerShape(14.dp))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.TableRestaurant,
                                    contentDescription = null,
                                    tint = BurgundyLeather,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    text = "TABLE COVER / SHEET",
                                    fontFamily = PlayfairDisplayFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.5.sp,
                                    color = MahoganyDark
                                )
                            }
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = "SELECTED: ${tableCover.label} — ${tableCover.description}",
                                fontFamily = PlusJakartaSansFontFamily,
                                fontSize = 10.5.sp,
                                color = InkMuted
                            )
                            Spacer(Modifier.height(8.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(TableCoverStyle.entries) { cover ->
                                    val selected = cover == tableCover
                                    Surface(
                                        color = if (selected) BrassBright else ParchmentCream,
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier
                                            .border(
                                                width = 1.5.dp,
                                                color = if (selected) MahoganyDark else cover.trimColor,
                                                shape = RoundedCornerShape(10.dp)
                                            )
                                            .clickable { onSelectTableCover(cover) }
                                            .testTag("table_cover_${cover.id}")
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(12.dp)
                                                    .clip(RoundedCornerShape(3.dp))
                                                    .background(cover.primaryColor)
                                                    .border(1.dp, cover.trimColor, RoundedCornerShape(3.dp))
                                            )
                                            Spacer(Modifier.width(6.dp))
                                            Text(
                                                text = cover.label,
                                                fontFamily = PlusJakartaSansFontFamily,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.5.sp,
                                                color = MahoganyDark
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // 4. HELP & SHELF MAP GUIDE
                    Surface(
                        color = ParchmentSoft,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, BrassGold, RoundedCornerShape(14.dp))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.HelpOutline,
                                    contentDescription = null,
                                    tint = EmeraldLibrarian,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    text = "HELP & LIBRARY GUIDE",
                                    fontFamily = PlayfairDisplayFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.5.sp,
                                    color = MahoganyDark
                                )
                            }
                            Spacer(Modifier.height(6.dp))
                            Text(
                                text = "• JOYSTICK: WALK BETWEEN SHELVES #1–#10, WOODEN TABLES, AND THE LIBRARY CENTRE.\n• SHELVES #1–#10: REACH A SHELF TO SELECT A BOOK & CARRY IT TO A TABLE TO READ.\n• LIBRARY CENTRE: ACCESS PROJECTS, ARTWORK, SEARCH BOOKS & OFFLINE PACKS.\n• APP SETTINGS: MANAGE THEME, LAMP LIGHTS, TABLE COVER & HELP.",
                                fontFamily = PlusJakartaSansFontFamily,
                                fontSize = 11.sp,
                                lineHeight = 16.sp,
                                color = InkCharcoal
                            )
                            Spacer(Modifier.height(10.dp))
                            Button(
                                onClick = onOpenHelpMap,
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldLibrarian),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(44.dp)
                                    .testTag("settings_open_help_map_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Map,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    text = "OPEN SHELF HELP MAP (#1–#10)",
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
    }
}
