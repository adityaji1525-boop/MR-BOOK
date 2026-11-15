package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.LampAmbience
import com.example.ui.theme.BrassBright
import com.example.ui.theme.BrassGold
import com.example.ui.theme.EmeraldLibrarian
import com.example.ui.theme.MahoganyDark
import com.example.ui.theme.ParchmentCream
import com.example.ui.theme.ParchmentSoft
import com.example.ui.theme.PlayfairDisplayFontFamily
import com.example.ui.theme.PlusJakartaSansFontFamily
import kotlin.math.roundToInt

@Composable
fun DeskLampControl(
    isLampOn: Boolean,
    lampAmbience: LampAmbience,
    lampBrightness: Float,
    onToggleLamp: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor by animateColorAsState(
        targetValue = if (isLampOn) lampAmbience.glowColor.copy(alpha = 0.85f) else BrassGold.copy(alpha = 0.35f),
        animationSpec = tween(350),
        label = "lamp_border_color"
    )
    val statusGlow by animateColorAsState(
        targetValue = if (isLampOn) lampAmbience.glowColor else Color(0xFF9C8B7A),
        animationSpec = tween(350),
        label = "lamp_status_color"
    )

    Surface(
        color = MahoganyDark.copy(alpha = 0.90f),
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 6.dp,
        modifier = modifier
            .fillMaxWidth()
            .border(1.2.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable { onToggleLamp(!isLampOn) }
            .testTag("desk_lamp_widget")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            DeskLampMiniIllustration(
                lampColor = lampAmbience.glowColor,
                isLampOn = isLampOn,
                brightness = lampBrightness,
                modifier = Modifier.size(34.dp)
            )

            Spacer(Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "DESK LAMP",
                        fontFamily = PlayfairDisplayFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = ParchmentCream,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(statusGlow)
                    )
                }
                Text(
                    text = if (isLampOn) {
                        "ON • ${lampAmbience.label} (${(lampBrightness * 100).roundToInt()}%)"
                    } else {
                        "OFF • COZY TWILIGHT MODE"
                    },
                    fontFamily = PlusJakartaSansFontFamily,
                    fontSize = 10.5.sp,
                    color = statusGlow,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Switch(
                checked = isLampOn,
                onCheckedChange = onToggleLamp,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MahoganyDark,
                    checkedTrackColor = lampAmbience.glowColor,
                    checkedBorderColor = BrassBright,
                    uncheckedThumbColor = ParchmentSoft,
                    uncheckedTrackColor = Color(0xFF3E2922),
                    uncheckedBorderColor = BrassGold.copy(alpha = 0.45f)
                ),
                modifier = Modifier.testTag("desk_lamp_toggle_switch")
            )
        }
    }
}

@Composable
fun DeskLampMiniIllustration(
    lampColor: Color,
    isLampOn: Boolean,
    brightness: Float,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        if (isLampOn) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(lampColor.copy(alpha = 0.78f * brightness), Color.Transparent),
                    center = Offset(w * 0.5f, h * 0.48f),
                    radius = w * 0.58f
                ),
                center = Offset(w * 0.5f, h * 0.48f),
                radius = w * 0.58f
            )
        }
        drawRoundRect(
            color = BrassGold,
            topLeft = Offset(w * 0.25f, h * 0.80f),
            size = Size(w * 0.50f, h * 0.14f),
            cornerRadius = CornerRadius(4f, 4f)
        )
        drawLine(
            color = BrassGold,
            start = Offset(w * 0.5f, h * 0.80f),
            end = Offset(w * 0.5f, h * 0.42f),
            strokeWidth = 4f
        )
        val shade = Path().apply {
            moveTo(w * 0.16f, h * 0.44f)
            lineTo(w * 0.84f, h * 0.44f)
            lineTo(w * 0.70f, h * 0.20f)
            lineTo(w * 0.30f, h * 0.20f)
            close()
        }
        drawPath(
            path = shade,
            color = if (isLampOn) EmeraldLibrarian else Color(0xFF283A33)
        )
        drawPath(shade, color = BrassGold, style = Stroke(width = 1.8f))
    }
}
