package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.model.CharacterAvatar
import com.example.ui.theme.BrassGold
import com.example.ui.theme.BurgundyLeather
import com.example.ui.theme.MahoganyDark
import com.example.ui.theme.ParchmentCream
import kotlin.math.sin

fun DrawScope.drawLibraryCharacter(
    center: Offset,
    scale: Float,
    avatar: CharacterAvatar,
    cloakColor: Color,
    walkPhase: Float,
    isWalking: Boolean,
    facingRight: Boolean,
    carriedBooksCount: Int,
    isSeated: Boolean
) {
    val s = scale
    val bobY = if (isWalking) -kotlin.math.abs(sin(walkPhase)) * 4f * s else 0f
    val legSwing = if (isWalking) sin(walkPhase) * 6f * s else 0f
    val dir = if (facingRight) 1f else -1f

    // Soft ground shadow
    drawOval(
        color = Color(0x441A0F0A),
        topLeft = Offset(center.x - 16f * s, center.y + 18f * s),
        size = Size(32f * s, 9f * s)
    )

    if (!isSeated) {
        // Left & Right animated legs
        drawRoundRect(
            color = MahoganyDark,
            topLeft = Offset(center.x - 8f * s + legSwing * 0.5f, center.y + 10f * s),
            size = Size(5.5f * s, 11f * s),
            cornerRadius = CornerRadius(3f * s, 3f * s)
        )
        drawRoundRect(
            color = MahoganyDark,
            topLeft = Offset(center.x + 2.5f * s - legSwing * 0.5f, center.y + 10f * s),
            size = Size(5.5f * s, 11f * s),
            cornerRadius = CornerRadius(3f * s, 3f * s)
        )
    }

    // Cloak / Coat body
    val cloakPath = Path().apply {
        moveTo(center.x - 10f * s, center.y - 4f * s + bobY)
        lineTo(center.x + 10f * s, center.y - 4f * s + bobY)
        lineTo(center.x + 13f * s, center.y + 13f * s + bobY)
        lineTo(center.x - 13f * s, center.y + 13f * s + bobY)
        close()
    }
    drawPath(cloakPath, color = cloakColor)

    // Brass collar trim
    drawRoundRect(
        color = BrassGold,
        topLeft = Offset(center.x - 7f * s, center.y - 5f * s + bobY),
        size = Size(14f * s, 3f * s),
        cornerRadius = CornerRadius(1.5f * s, 1.5f * s)
    )

    // Head
    val headCenter = Offset(center.x, center.y - 14f * s + bobY)
    drawCircle(
        color = avatar.skinColor,
        radius = 10f * s,
        center = headCenter
    )

    // Hair crown
    drawArc(
        color = avatar.hairColor,
        startAngle = 180f,
        sweepAngle = 180f,
        useCenter = true,
        topLeft = Offset(headCenter.x - 10.5f * s, headCenter.y - 10.5f * s),
        size = Size(21f * s, 14f * s)
    )

    // Optional Hat (Beret / Explorer cap)
    if (avatar.hasHat) {
        drawOval(
            color = cloakColor.copy(alpha = 0.92f),
            topLeft = Offset(headCenter.x - 12f * s, headCenter.y - 13.5f * s),
            size = Size(24f * s, 8f * s)
        )
        drawCircle(
            color = BrassGold,
            radius = 2f * s,
            center = Offset(headCenter.x, headCenter.y - 14f * s)
        )
    }

    // Eyes
    val eyeOffsetX = dir * 2.2f * s
    drawCircle(
        color = MahoganyDark,
        radius = 1.4f * s,
        center = Offset(headCenter.x - 3.2f * s + eyeOffsetX, headCenter.y - 1f * s)
    )
    drawCircle(
        color = MahoganyDark,
        radius = 1.4f * s,
        center = Offset(headCenter.x + 3.2f * s + eyeOffsetX, headCenter.y - 1f * s)
    )

    // Optional Glasses
    if (avatar.hasGlasses) {
        drawCircle(
            color = BrassGold,
            radius = 3.2f * s,
            center = Offset(headCenter.x - 3.2f * s + eyeOffsetX, headCenter.y - 1f * s),
            style = Stroke(width = 1.1f * s)
        )
        drawCircle(
            color = BrassGold,
            radius = 3.2f * s,
            center = Offset(headCenter.x + 3.2f * s + eyeOffsetX, headCenter.y - 1f * s),
            style = Stroke(width = 1.1f * s)
        )
        drawLine(
            color = BrassGold,
            start = Offset(headCenter.x + eyeOffsetX, headCenter.y - 1f * s),
            end = Offset(headCenter.x + eyeOffsetX, headCenter.y - 1f * s),
            strokeWidth = 1f * s
        )
    }

    // Gentle smile
    drawArc(
        color = Color(0xFF6B3A2E),
        startAngle = 20f,
        sweepAngle = 140f,
        useCenter = false,
        topLeft = Offset(headCenter.x - 3f * s + eyeOffsetX * 0.5f, headCenter.y + 1.5f * s),
        size = Size(6f * s, 3.5f * s),
        style = Stroke(width = 1.2f * s)
    )

    // Carried stack of books in character's arms
    if (carriedBooksCount > 0) {
        val stackX = center.x + dir * 7f * s
        val baseStackY = center.y + 5f * s + bobY
        val bookColors = listOf(BurgundyLeather, Color(0xFF1F5E4B), Color(0xFF3949AB), Color(0xFFC7771A))
        repeat(carriedBooksCount.coerceAtMost(4)) { idx ->
            val y = baseStackY - idx * 3.8f * s
            drawRoundRect(
                color = bookColors[idx % bookColors.size],
                topLeft = Offset(stackX - 6f * s, y),
                size = Size(12f * s, 3.4f * s),
                cornerRadius = CornerRadius(1f * s, 1f * s)
            )
            drawLine(
                color = ParchmentCream,
                start = Offset(stackX - 4.5f * s, y + 1.7f * s),
                end = Offset(stackX + 4.5f * s, y + 1.7f * s),
                strokeWidth = 1f * s
            )
        }
    }
}

@Composable
fun CharacterPreviewSprite(
    avatar: CharacterAvatar,
    cloakColor: Color,
    size: Dp = 64.dp,
    carriedBooksCount: Int = 1,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(size)) {
        val scale = this.size.minDimension / 52f
        drawLibraryCharacter(
            center = Offset(this.size.width / 2f, this.size.height * 0.56f),
            scale = scale,
            avatar = avatar,
            cloakColor = cloakColor,
            walkPhase = 0f,
            isWalking = false,
            facingRight = true,
            carriedBooksCount = carriedBooksCount,
            isSeated = false
        )
    }
}
