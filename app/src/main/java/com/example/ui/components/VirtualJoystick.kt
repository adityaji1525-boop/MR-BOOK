package com.example.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BrassBright
import com.example.ui.theme.BrassGold
import com.example.ui.theme.BurgundyLeather
import com.example.ui.theme.MahoganyDark
import com.example.ui.theme.MahoganyRich
import com.example.ui.theme.ParchmentCream
import com.example.ui.theme.PlusJakartaSansFontFamily
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.hypot
import kotlin.math.roundToInt

@Composable
fun VirtualJoystick(
    onDirectionChange: (dx: Float, dy: Float) -> Unit,
    modifier: Modifier = Modifier,
    padSize: Dp = 114.dp,
    knobSize: Dp = 46.dp
) {
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()
    val maxRadiusPx = with(density) { ((padSize - knobSize) / 2f).toPx() }.coerceAtLeast(1f)

    var rawOffset by remember { mutableStateOf(Offset.Zero) }
    var isPressed by remember { mutableStateOf(false) }

    val animatedKnobOffset by animateOffsetAsState(
        targetValue = rawOffset,
        animationSpec = if (isPressed) {
            spring(stiffness = Spring.StiffnessHigh, dampingRatio = Spring.DampingRatioNoBouncy)
        } else {
            spring(stiffness = Spring.StiffnessMedium, dampingRatio = Spring.DampingRatioMediumBouncy)
        },
        label = "joystick_knob_offset"
    )

    fun clampAndEmit(offset: Offset) {
        val dist = hypot(offset.x, offset.y)
        val clamped = if (dist > maxRadiusPx) {
            Offset(offset.x / dist * maxRadiusPx, offset.y / dist * maxRadiusPx)
        } else {
            offset
        }
        rawOffset = clamped
        onDirectionChange(
            (clamped.x / maxRadiusPx).coerceIn(-1f, 1f),
            (clamped.y / maxRadiusPx).coerceIn(-1f, 1f)
        )
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(padSize)
            .shadow(10.dp, CircleShape)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(MahoganyRich.copy(alpha = 0.90f), MahoganyDark.copy(alpha = 0.94f))
                )
            )
            .border(2.dp, BrassGold.copy(alpha = 0.85f), CircleShape)
            .testTag("library_joystick")
            .pointerInput(maxRadiusPx) {
                val center = Offset(size.width / 2f, size.height / 2f)
                detectDragGestures(
                    onDragStart = { startPos ->
                        isPressed = true
                        clampAndEmit(startPos - center)
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        clampAndEmit(rawOffset + dragAmount)
                    },
                    onDragEnd = {
                        isPressed = false
                        rawOffset = Offset.Zero
                        onDirectionChange(0f, 0f)
                    },
                    onDragCancel = {
                        isPressed = false
                        rawOffset = Offset.Zero
                        onDirectionChange(0f, 0f)
                    }
                )
            }
            .pointerInput(maxRadiusPx) {
                val center = Offset(size.width / 2f, size.height / 2f)
                detectTapGestures { tapPos ->
                    isPressed = true
                    clampAndEmit(tapPos - center)
                    scope.launch {
                        delay(180L)
                        isPressed = false
                        rawOffset = Offset.Zero
                        onDirectionChange(0f, 0f)
                    }
                }
            }
    ) {
        Canvas(modifier = Modifier.size(padSize)) {
            val r = size.minDimension / 2f
            val c = Offset(r, r)
            // Inner brass guide circle
            drawCircle(
                color = BrassGold.copy(alpha = 0.28f),
                radius = r * 0.64f,
                center = c,
                style = Stroke(width = 1.5f)
            )
            // Subtle N/S/E/W compass tick marks
            val tickInner = r * 0.76f
            val tickOuter = r * 0.88f
            val dirs = listOf(
                Offset(0f, -1f),
                Offset(0f, 1f),
                Offset(-1f, 0f),
                Offset(1f, 0f)
            )
            dirs.forEach { d ->
                drawLine(
                    color = BrassBright.copy(alpha = 0.60f),
                    start = c + d * tickInner,
                    end = c + d * tickOuter,
                    strokeWidth = 2.2f
                )
            }
        }

        // Thumbstick Knob
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .offset {
                    IntOffset(
                        animatedKnobOffset.x.roundToInt(),
                        animatedKnobOffset.y.roundToInt()
                    )
                }
                .size(knobSize)
                .shadow(6.dp, CircleShape)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(BrassBright, BurgundyLeather)
                    )
                )
                .border(2.dp, ParchmentCream.copy(alpha = 0.85f), CircleShape)
                .testTag("library_joystick_knob")
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(ParchmentCream.copy(alpha = 0.55f))
            )
        }

        if (!isPressed) {
            Text(
                text = "MOVE",
                fontFamily = PlusJakartaSansFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 8.sp,
                letterSpacing = 0.8.sp,
                color = BrassBright.copy(alpha = 0.72f),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 5.dp)
            )
        }
    }
}
