package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CustomBaseChassis
import com.example.model.CustomPrimitiveShape
import com.example.model.ScienceProjectLabState
import com.example.ui.theme.BrassBright
import com.example.ui.theme.BrassGold
import com.example.ui.theme.EmeraldLibrarian
import com.example.ui.theme.ParchmentCream
import com.example.ui.theme.PlusJakartaSansFontFamily
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private data class Vec3(val x: Float, val y: Float, val z: Float)

private data class ProjectedPoint(val x: Float, val y: Float, val depth: Float, val scale: Float)

private data class DepthDrawable(val depth: Float, val draw: DrawScope.() -> Unit)

@Composable
fun ScienceModel3DViewport(
    state: ScienceProjectLabState,
    isWorkingAnimationActive: Boolean,
    errorCount: Int,
    onRotateDelta: (dYaw: Float, dPitch: Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "science_3d_sim")
    val animPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2.0 * PI).toFloat(),
        animationSpec = infiniteRepeatable(tween(4000, easing = LinearEasing), RepeatMode.Restart),
        label = "sim_phase"
    )

    val speedFactor = if (isWorkingAnimationActive && errorCount == 0) {
        0.35f + state.driveLevel * 1.65f
    } else {
        0f
    }
    val activePhase = animPhase * speedFactor

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xFF1F2B38), Color(0xFF0E141C))
                )
            )
            .border(1.5.dp, if (errorCount == 0 && isWorkingAnimationActive) EmeraldLibrarian else BrassGold, RoundedCornerShape(14.dp))
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    onRotateDelta(dragAmount.x * 0.55f, -dragAmount.y * 0.45f)
                }
            }
            .testTag("science_3d_viewport")
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val yawRad = Math.toRadians(state.yawDeg.toDouble()).toFloat()
            val pitchRad = Math.toRadians(state.pitchDeg.toDouble()).toFloat()
            val baseScale = size.minDimension * 0.25f * state.zoom.coerceIn(0.65f, 1.5f)
            val center = Offset(size.width * 0.5f, size.height * 0.53f)
            val explode = if (state.isExploded) 0.42f else 0f

            fun project(v: Vec3): ProjectedPoint {
                val cosY = cos(yawRad)
                val sinY = sin(yawRad)
                val x1 = v.x * cosY + v.z * sinY
                val z1 = -v.x * sinY + v.z * cosY

                val cosP = cos(pitchRad)
                val sinP = sin(pitchRad)
                val y2 = v.y * cosP - z1 * sinP
                val z2 = v.y * sinP + z1 * cosP

                val cameraDist = 4.6f
                val perspective = cameraDist / (cameraDist - z2).coerceAtLeast(1.1f)
                return ProjectedPoint(
                    x = center.x + x1 * baseScale * perspective,
                    y = center.y - y2 * baseScale * perspective,
                    depth = z2,
                    scale = baseScale * perspective
                )
            }

            val drawQueue = mutableListOf<DepthDrawable>()

            fun enqueueBox(
                centerPos: Vec3,
                halfW: Float,
                halfH: Float,
                halfD: Float,
                fillColor: Color,
                wireOnly: Boolean = state.showWireframe
            ) {
                val corners = listOf(
                    Vec3(centerPos.x - halfW, centerPos.y - halfH, centerPos.z - halfD),
                    Vec3(centerPos.x + halfW, centerPos.y - halfH, centerPos.z - halfD),
                    Vec3(centerPos.x + halfW, centerPos.y + halfH, centerPos.z - halfD),
                    Vec3(centerPos.x - halfW, centerPos.y + halfH, centerPos.z - halfD),
                    Vec3(centerPos.x - halfW, centerPos.y - halfH, centerPos.z + halfD),
                    Vec3(centerPos.x + halfW, centerPos.y - halfH, centerPos.z + halfD),
                    Vec3(centerPos.x + halfW, centerPos.y + halfH, centerPos.z + halfD),
                    Vec3(centerPos.x - halfW, centerPos.y + halfH, centerPos.z + halfD)
                ).map(::project)

                val faces = listOf(
                    listOf(0, 1, 2, 3) to 0.72f,
                    listOf(4, 5, 6, 7) to 0.96f,
                    listOf(0, 1, 5, 4) to 0.65f,
                    listOf(2, 3, 7, 6) to 1.05f,
                    listOf(0, 3, 7, 4) to 0.80f,
                    listOf(1, 2, 6, 5) to 0.88f
                )
                faces.forEach { (idxList, shade) ->
                    val avgDepth = idxList.map { corners[it].depth }.average().toFloat()
                    drawQueue += DepthDrawable(avgDepth) {
                        val path = Path().apply {
                            moveTo(corners[idxList[0]].x, corners[idxList[0]].y)
                            for (k in 1 until idxList.size) {
                                lineTo(corners[idxList[k]].x, corners[idxList[k]].y)
                            }
                            close()
                        }
                        if (!wireOnly) {
                            drawPath(
                                path = path,
                                color = Color(
                                    red = (fillColor.red * shade).coerceIn(0f, 1f),
                                    green = (fillColor.green * shade).coerceIn(0f, 1f),
                                    blue = (fillColor.blue * shade).coerceIn(0f, 1f),
                                    alpha = fillColor.alpha
                                )
                            )
                        }
                        drawPath(
                            path = path,
                            color = if (wireOnly) fillColor else BrassGold.copy(alpha = 0.6f),
                            style = Stroke(width = if (wireOnly) 2.2f else 1.1f)
                        )
                    }
                }
            }

            fun enqueueSphere(centerPos: Vec3, radius: Float, color: Color, glow: Boolean = false) {
                val p = project(centerPos)
                val rPx = (radius * p.scale).coerceAtLeast(3f)
                drawQueue += DepthDrawable(p.depth) {
                    if (glow) {
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(color.copy(alpha = 0.75f), Color.Transparent),
                                center = Offset(p.x, p.y),
                                radius = rPx * 2.1f
                            ),
                            radius = rPx * 2.1f,
                            center = Offset(p.x, p.y)
                        )
                    }
                    if (!state.showWireframe) {
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(
                                        (color.red * 1.25f).coerceIn(0f, 1f),
                                        (color.green * 1.25f).coerceIn(0f, 1f),
                                        (color.blue * 1.25f).coerceIn(0f, 1f),
                                        color.alpha
                                    ),
                                    color,
                                    Color(color.red * 0.45f, color.green * 0.45f, color.blue * 0.45f, color.alpha)
                                ),
                                center = Offset(p.x - rPx * 0.3f, p.y - rPx * 0.3f),
                                radius = rPx * 1.25f
                            ),
                            radius = rPx,
                            center = Offset(p.x, p.y)
                        )
                    } else {
                        drawCircle(
                            color = color,
                            radius = rPx,
                            center = Offset(p.x, p.y),
                            style = Stroke(width = 2f)
                        )
                    }
                }
            }

            fun enqueueRing(centerPos: Vec3, radius: Float, color: Color, segments: Int = 32, tiltX: Float = 0f) {
                val cosT = cos(tiltX)
                val sinT = sin(tiltX)
                for (i in 0 until segments) {
                    val a1 = (i.toFloat() / segments) * (2f * PI.toFloat())
                    val a2 = ((i + 1).toFloat() / segments) * (2f * PI.toFloat())
                    val rx1 = radius * cos(a1)
                    val rz1 = radius * sin(a1)
                    val rx2 = radius * cos(a2)
                    val rz2 = radius * sin(a2)
                    val v1 = Vec3(centerPos.x + rx1, centerPos.y + rz1 * sinT, centerPos.z + rz1 * cosT)
                    val v2 = Vec3(centerPos.x + rx2, centerPos.y + rz2 * sinT, centerPos.z + rz2 * cosT)
                    val p1 = project(v1)
                    val p2 = project(v2)
                    drawQueue += DepthDrawable((p1.depth + p2.depth) * 0.5f) {
                        drawLine(
                            color = color,
                            start = Offset(p1.x, p1.y),
                            end = Offset(p2.x, p2.y),
                            strokeWidth = 2.4f,
                            cap = StrokeCap.Round
                        )
                    }
                }
            }

            fun enqueueLine3D(a: Vec3, b: Vec3, color: Color, strokeWidth: Float = 4f) {
                val p1 = project(a)
                val p2 = project(b)
                drawQueue += DepthDrawable((p1.depth + p2.depth) * 0.5f) {
                    drawLine(
                        color = color,
                        start = Offset(p1.x, p1.y),
                        end = Offset(p2.x, p2.y),
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Round
                    )
                }
            }

            // Blueprint 3D Floor Grid
            for (g in -2..2) {
                val fg = g * 0.55f
                enqueueLine3D(Vec3(-1.1f, -0.85f, fg), Vec3(1.1f, -0.85f, fg), Color(0x337EB4FF), 1.3f)
                enqueueLine3D(Vec3(fg, -0.85f, -1.1f), Vec3(fg, -0.85f, 1.1f), Color(0x337EB4FF), 1.3f)
            }

            // Render Structural 3D Chassis (Built-in or User Custom Template Chassis)
            val workingOk = isWorkingAnimationActive && errorCount == 0
            when (state.effectiveChassis) {
                CustomBaseChassis.ORBITAL_RIG -> {
                    // Base Stand
                    enqueueBox(Vec3(0f, -0.78f - explode * 0.4f, 0f), 0.55f, 0.08f, 0.55f, Color(0xFF6B4226))
                    enqueueLine3D(Vec3(0f, -0.70f, 0f), Vec3(0f, 0.05f + explode * 0.3f, 0f), BrassGold, 6f)
                    // Sun
                    enqueueSphere(Vec3(0f, 0.05f + explode * 0.3f, 0f), 0.25f, Color(0xFFFFC145), glow = state.isPowerOrTriggerOn)
                    val radii = listOf(0.46f, 0.68f, 0.92f, 1.16f)
                    val colors = listOf(Color(0xFFB5ABA0), Color(0xFFE0A96D), Color(0xFF4C96D7), Color(0xFFD15B3E))
                    val pSizes = listOf(0.06f, 0.085f, 0.095f, 0.075f)
                    val speeds = listOf(2.4f, 1.6f, 1.1f, 0.75f)
                    val tiltRad = Math.toRadians((state.primaryAngleDeg - 23).toDouble()).toFloat() * 0.4f
                    radii.forEachIndexed { idx, r ->
                        val ringY = 0.05f + explode * (0.25f * (idx - 1.5f))
                        enqueueRing(Vec3(0f, ringY, 0f), r, Color(0x99F0C66B), tiltX = tiltRad)
                        val ang = activePhase * speeds[idx] + idx * 1.4f
                        val px = r * cos(ang)
                        val pz = r * sin(ang)
                        enqueueSphere(
                            Vec3(px, ringY + pz * sin(tiltRad), pz * cos(tiltRad)),
                            pSizes[idx],
                            colors[idx]
                        )
                    }
                }

                CustomBaseChassis.MOTOR_CIRCUIT -> {
                    // Wooden Base & Poles
                    enqueueBox(Vec3(0f, -0.75f - explode * 0.3f, 0f), 1.0f, 0.10f, 0.65f, Color(0xFF7B4B2A))
                    // North & South Magnets
                    enqueueBox(Vec3(-0.68f - explode * 0.45f, -0.15f, 0f), 0.18f, 0.38f, 0.35f, Color(0xFFC93B3B))
                    enqueueBox(Vec3(0.68f + explode * 0.45f, -0.15f, 0f), 0.18f, 0.38f, 0.35f, Color(0xFF2D65B0))
                    // Battery Pack
                    enqueueBox(Vec3(-0.68f, -0.52f, 0.42f + explode * 0.25f), 0.22f, 0.14f, 0.14f, Color(0xFF24292F))
                    // Rotating Copper Coil Armature
                    val spinAngle = if (workingOk) activePhase * 3.0f else 0.4f
                    val cw = 0.32f
                    val ch = 0.22f
                    val cz = cos(spinAngle) * ch
                    val cy = sin(spinAngle) * ch
                    val rotorY = -0.15f + explode * 0.35f
                    enqueueLine3D(Vec3(-0.46f, rotorY, 0f), Vec3(0.46f, rotorY, 0f), Color(0xFFD9D9D9), 5f)
                    enqueueLine3D(Vec3(-cw, rotorY - cy, -cz), Vec3(-cw, rotorY + cy, cz), Color(0xFFD87A38), 6f)
                    enqueueLine3D(Vec3(-cw, rotorY + cy, cz), Vec3(cw, rotorY + cy, cz), Color(0xFFD87A38), 6f)
                    enqueueLine3D(Vec3(cw, rotorY + cy, cz), Vec3(cw, rotorY - cy, -cz), Color(0xFFD87A38), 6f)
                    enqueueLine3D(Vec3(cw, rotorY - cy, -cz), Vec3(-cw, rotorY - cy, -cz), Color(0xFFD87A38), 6f)
                    // Circuit Wire Status
                    val wireColor = if (state.isSealedOrWired) Color(0xFF47D18C) else Color(0xFFE05252)
                    enqueueLine3D(Vec3(-0.68f, -0.38f, 0.42f), Vec3(-0.45f, rotorY - 0.1f, 0.1f), wireColor, 4f)
                    enqueueLine3D(Vec3(0.45f, rotorY - 0.1f, 0f), Vec3(0.68f, -0.52f, 0.42f), wireColor, 4f)
                }

                CustomBaseChassis.REACTION_CONE -> {
                    // Catchment Tray & Cone Layers
                    enqueueBox(Vec3(0f, -0.78f - explode * 0.3f, 0f), 0.95f, 0.08f, 0.95f, Color(0xFF3E4C59))
                    val layers = 5
                    for (l in 0 until layers) {
                        val r = 0.78f - l * 0.13f
                        val y = -0.60f + l * 0.21f + explode * l * 0.12f
                        enqueueRing(Vec3(0f, y, 0f), r, Color(0xFF7B4A2E), segments = 24)
                        enqueueBox(Vec3(0f, y, 0f), r * 0.72f, 0.09f, r * 0.72f, Color(0xFF6B3E26))
                    }
                    // Magma Conduit & Eruption Plume
                    val conduitColor = if (state.isPowerOrTriggerOn) Color(0xFFFF5E2B) else Color(0xFF5A251B)
                    enqueueLine3D(Vec3(0f, -0.65f, 0f), Vec3(0f, 0.38f + explode * 0.4f, 0f), conduitColor, 12f)
                    if (workingOk) {
                        for (d in 0 until 7) {
                            val t = ((activePhase * 0.45f + d * 0.35f) % 1.4f)
                            val arcY = 0.42f + t * 0.55f - t * t * 0.48f
                            val arcR = t * 0.48f
                            val a = d * 0.95f
                            enqueueSphere(Vec3(cos(a) * arcR, arcY, sin(a) * arcR), 0.065f, Color(0xFFFF7A29), glow = true)
                        }
                    }
                }

                CustomBaseChassis.TURBINE_TOWER -> {
                    // Base & Tower
                    enqueueBox(Vec3(0f, -0.80f - explode * 0.3f, 0f), 0.55f, 0.08f, 0.55f, Color(0xFF4A5568))
                    enqueueLine3D(Vec3(0f, -0.72f, 0f), Vec3(0f, 0.32f, 0f), Color(0xFFE2E8F0), 11f)
                    // Nacelle
                    enqueueBox(Vec3(0f, 0.35f + explode * 0.25f, 0.05f), 0.15f, 0.14f, 0.28f, Color(0xFFCBD5E1))
                    // 3 Spinning Rotor Blades
                    val hub = Vec3(0f, 0.35f + explode * 0.25f, 0.34f + explode * 0.35f)
                    enqueueSphere(hub, 0.09f, BrassBright)
                    val baseAngle = if (workingOk) activePhase * 2.2f else 0.35f
                    for (b in 0 until 3) {
                        val a = baseAngle + b * (2f * PI.toFloat() / 3f)
                        val tip = Vec3(hub.x + cos(a) * 0.65f, hub.y + sin(a) * 0.65f, hub.z)
                        enqueueLine3D(hub, tip, Color(0xFFF8FAFC), 9f)
                    }
                    // LED Test Load at Base
                    enqueueSphere(Vec3(0.45f, -0.62f, 0.35f), 0.09f, if (workingOk) Color(0xFF47FF9C) else Color(0xFF4A5568), glow = workingOk)
                }

                CustomBaseChassis.HYDRAULIC_PISTON -> {
                    enqueueBox(Vec3(0f, -0.78f, 0f), 1.0f, 0.07f, 0.65f, Color(0xFF475569))
                    val liftProgress = if (workingOk) (0.5f + 0.5f * sin(activePhase * 1.4f)) * state.driveLevel else 0.15f
                    val deckHeight = -0.35f + liftProgress * 0.65f + explode * 0.3f
                    // Left Input Syringe & Right Lift Syringe
                    enqueueBox(Vec3(-0.62f, -0.45f, 0f), 0.12f, 0.28f, 0.12f, Color(0xFF60A5FA))
                    enqueueBox(Vec3(0.25f, -0.45f, 0f), 0.14f, 0.28f, 0.14f, Color(0xFF38BDF8))
                    // Hydraulic Line
                    val tubeColor = if (state.isSealedOrWired) Color(0xFF38BDF8) else Color(0xFFE05252)
                    enqueueLine3D(Vec3(-0.62f, -0.70f, 0f), Vec3(0.25f, -0.70f, 0f), tubeColor, 6f)
                    // Piston Rod & Hinged Bridge Deck
                    enqueueLine3D(Vec3(0.25f, -0.25f, 0f), Vec3(0.25f, deckHeight, 0f), BrassBright, 6f)
                    enqueueBox(Vec3(0.22f, deckHeight + 0.06f, 0f), 0.62f, 0.06f, 0.35f, Color(0xFFB45309))
                }

                CustomBaseChassis.OPTICS_TUBE -> {
                    // Vertical Viewing Shaft
                    enqueueBox(Vec3(0f, 0.0f, 0f), 0.20f, 0.72f, 0.20f, Color(0x33CBD5E1), wireOnly = true)
                    enqueueBox(Vec3(0.22f, 0.58f, 0f), 0.16f, 0.14f, 0.18f, Color(0xFF334155))
                    enqueueBox(Vec3(-0.22f, -0.58f, 0f), 0.16f, 0.14f, 0.18f, Color(0xFF334155))
                    // Top & Bottom 45-degree Mirrors
                    val tiltOffset = (state.primaryAngleDeg - 45) * 0.008f
                    enqueueLine3D(Vec3(-0.14f, 0.44f - tiltOffset, 0f), Vec3(0.14f, 0.70f + tiltOffset, 0f), Color(0xFF93C5FD), 7f)
                    enqueueLine3D(Vec3(-0.14f, -0.70f - tiltOffset, 0f), Vec3(0.14f, -0.44f + tiltOffset, 0f), Color(0xFF93C5FD), 7f)
                    // Light Beam Path
                    val beamColor = if (workingOk) Color(0xFFFDE047) else Color(0x88EF4444)
                    enqueueLine3D(Vec3(0.75f, 0.58f, 0f), Vec3(0f, 0.58f, 0f), beamColor, 5f)
                    enqueueLine3D(Vec3(0f, 0.58f, 0f), Vec3(0f, -0.58f, 0f), beamColor, 5f)
                    enqueueLine3D(
                        Vec3(0f, -0.58f, 0f),
                        Vec3(-0.75f, -0.58f + (state.primaryAngleDeg - 45) * 0.03f, 0f),
                        beamColor,
                        5f
                    )
                }

                CustomBaseChassis.FILTER_COLUMN -> {
                    // Stand & Collection Flask
                    enqueueBox(Vec3(0f, -0.82f, 0f), 0.55f, 0.07f, 0.55f, Color(0xFF475569))
                    enqueueSphere(Vec3(0f, -0.58f - explode * 0.25f, 0f), 0.22f, Color(0xFF38BDF8))
                    // 3 Filter Media Layers
                    enqueueBox(Vec3(0f, -0.15f, 0f), 0.24f, 0.11f, 0.24f, Color(0xFF1E293B)) // Charcoal
                    enqueueBox(Vec3(0f, 0.14f + explode * 0.2f, 0f), 0.28f, 0.12f, 0.28f, Color(0xFFEAB308)) // Fine Sand
                    enqueueBox(Vec3(0f, 0.44f + explode * 0.4f, 0f), 0.32f, 0.12f, 0.32f, Color(0xFF94A3B8)) // Gravel
                    if (workingOk) {
                        for (d in 0 until 4) {
                            val dropY = 0.65f - (((activePhase * 0.4f + d * 0.35f) % 1.15f))
                            enqueueSphere(Vec3(0f, dropY, 0.12f), 0.045f, Color(0xFF60A5FA), glow = true)
                        }
                    }
                }

                CustomBaseChassis.BLANK_WORKBENCH -> {
                    // Clean Custom Model Workbench Pedestal & Rotating Core Indicator
                    enqueueBox(Vec3(0f, -0.78f - explode * 0.2f, 0f), 0.85f, 0.08f, 0.85f, Color(0xFF5D4037))
                    enqueueRing(Vec3(0f, -0.68f, 0f), 0.72f, if (workingOk) Color(0xFF47D18C) else BrassGold)
                    if (workingOk) {
                        enqueueSphere(Vec3(0f, 0.05f, 0f), 0.10f, Color(0xFF47FF9C), glow = true)
                    }
                }
            }

            // Render any user-added custom 3D primitives (orbiting/animating when working on custom workbench)
            val customSpin = if (workingOk && state.effectiveChassis == CustomBaseChassis.BLANK_WORKBENCH) activePhase * 0.85f else 0f
            state.customParts.forEachIndexed { idx, part ->
                val cosA = cos(customSpin)
                val sinA = sin(customSpin)
                val rx = part.offsetX * cosA - part.offsetZ * sinA
                val rz = part.offsetX * sinA + part.offsetZ * cosA
                val bob = if (workingOk && state.effectiveChassis == CustomBaseChassis.BLANK_WORKBENCH) {
                    sin(activePhase * 1.8f + idx) * 0.06f
                } else {
                    0f
                }
                val pVec = Vec3(
                    rx * (1f + explode * 0.4f),
                    part.offsetY + bob + explode * 0.2f,
                    rz * (1f + explode * 0.4f)
                )
                when (part.shape) {
                    CustomPrimitiveShape.SPHERE -> enqueueSphere(pVec, 0.14f, part.color, glow = workingOk)
                    CustomPrimitiveShape.CUBE -> enqueueBox(pVec, 0.13f, 0.13f, 0.13f, part.color)
                    CustomPrimitiveShape.CYLINDER -> enqueueBox(pVec, 0.09f, 0.21f, 0.09f, part.color)
                    CustomPrimitiveShape.RING -> enqueueRing(pVec, 0.21f, part.color)
                }
            }

            // Sort Back-to-Front and Render
            drawQueue.sortedBy { it.depth }.forEach { item ->
                item.draw(this)
            }
        }

        // Corner 3D HUD Badges
        Surface(
            color = Color(0xAA0E141C),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp)
        ) {
            Text(
                text = "3D VIEW • DRAG TO ROTATE (${state.yawDeg.toInt()}°, ${state.pitchDeg.toInt()}°)",
                fontFamily = PlusJakartaSansFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 9.5.sp,
                color = ParchmentCream,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }

        Surface(
            color = if (errorCount == 0 && isWorkingAnimationActive) Color(0xCC1E6B52) else if (errorCount > 0) Color(0xCC9B2C2C) else Color(0xAA0E141C),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp)
        ) {
            Text(
                text = when {
                    errorCount > 0 -> "$errorCount ERROR(S) DETECTED"
                    isWorkingAnimationActive -> "WORKING SIMULATION ON"
                    else -> "MODEL PREVIEW READY"
                },
                fontFamily = PlusJakartaSansFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 9.5.sp,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}
