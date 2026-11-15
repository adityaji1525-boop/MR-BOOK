package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.LibraryFloorLayout
import com.example.model.BookshelfInfo
import com.example.model.CharacterAvatar
import com.example.model.LampAmbience
import com.example.model.StudyTableInfo
import com.example.model.TableCoverStyle
import com.example.ui.theme.BrassBright
import com.example.ui.theme.BrassGold
import com.example.ui.theme.BurgundyLeather
import com.example.ui.theme.EmeraldLibrarian
import com.example.ui.theme.MahoganyDark
import com.example.ui.theme.MahoganyRich
import com.example.ui.theme.OakWarm
import com.example.ui.theme.ParchmentCream
import com.example.ui.theme.ParchmentSoft
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun LibraryHallCanvas(
    characterName: String,
    avatar: CharacterAvatar,
    cloakColor: Color,
    charX: Float,
    charY: Float,
    targetX: Float,
    targetY: Float,
    isWalking: Boolean,
    facingRight: Boolean,
    walkPhase: Float,
    carriedBooksCount: Int,
    seatedTableId: String?,
    highlightedShelfNumber: Int?,
    nearbyShelfNumber: Int? = null,
    nearbyLibrarian: Boolean = false,
    isLampOn: Boolean = true,
    lampAmbience: LampAmbience = LampAmbience.GOLDEN_STUDY,
    lampBrightness: Float = 0.85f,
    tableCover: TableCoverStyle = TableCoverStyle.ROYAL_BURGUNDY,
    onWalkToFloor: (Float, Float) -> Unit,
    onTapShelf: (Int) -> Unit,
    onTapTable: (String) -> Unit,
    onTapLibrarian: () -> Unit,
    onJoystickMove: (Float, Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val woodBitmap = ImageBitmap.imageResource(id = R.drawable.img_wood_shelf_texture)

    val infiniteTransition = rememberInfiniteTransition(label = "hall_glow")
    val lampPulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "lamp_pulse"
    )
    val motePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2f * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(6500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "hall_motes"
    )

    val animatedBrightness by animateFloatAsState(
        targetValue = if (isLampOn) lampBrightness.coerceIn(0.25f, 1.0f) else 0.10f,
        animationSpec = tween(420),
        label = "hall_brightness"
    )

    val targetTileTint = when {
        !isLampOn -> Color(0x9E0E1320)
        lampAmbience == LampAmbience.CANDLELIGHT -> Color(0x38FF7D2E)
        lampAmbience == LampAmbience.STARLIGHT -> Color(0x461E2B58)
        lampAmbience == LampAmbience.MORNING_WINDOW -> Color(0x10FFFFFF)
        lampAmbience == LampAmbience.EMERALD_SANCTUARY -> Color(0x2E208256)
        else -> Color(0x24FFCA68)
    }
    val tileTint by animateColorAsState(targetTileTint, tween(450), label = "tile_tint")
    val activeGlowColor by animateColorAsState(
        targetValue = if (isLampOn) lampAmbience.glowColor else Color(0xFF7D95C9),
        tween(450),
        label = "hall_glow_color"
    )

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val density = LocalDensity.current
        val widthPx = with(density) { maxWidth.toPx() }
        val heightPx = with(density) { maxHeight.toPx() }

        // 1. High-Resolution White Design Tiles Library Floor
        Image(
            painter = painterResource(id = R.drawable.img_white_tile_floor),
            contentDescription = "White marble design tile library floor",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // 2. Interactive Architectural Overlay (Wooden Shelves, Wooden Tables + Covers, Rug, Lamps & Character)
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .testTag("library_hall_canvas")
                .pointerInput(widthPx, heightPx) {
                    detectTapGestures { offset ->
                        val nx = (offset.x / widthPx).coerceIn(0f, 1f)
                        val ny = (offset.y / heightPx).coerceIn(0f, 1f)

                        val (lx, ly) = LibraryFloorLayout.librarianSpot
                        if (hypot(nx - lx, ny - ly) < 0.09f) {
                            onTapLibrarian()
                            return@detectTapGestures
                        }

                        val hitShelf = LibraryFloorLayout.bookshelves.firstOrNull { shelf ->
                            kotlin.math.abs(nx - shelf.normX) < 0.11f &&
                                kotlin.math.abs(ny - shelf.normY) < 0.065f
                        }
                        if (hitShelf != null) {
                            onTapShelf(hitShelf.shelfNumber)
                            return@detectTapGestures
                        }

                        val hitTable = LibraryFloorLayout.studyTables.firstOrNull { table ->
                            hypot(nx - table.normX, ny - table.normY) < 0.095f
                        }
                        if (hitTable != null) {
                            onTapTable(hitTable.tableId)
                            return@detectTapGestures
                        }

                        onWalkToFloor(nx, ny)
                    }
                }
        ) {
            val w = size.width
            val h = size.height

            // Atmospheric Tint over the White Design Tiles (brightness + 5-color lamp theme)
            val tintFactor = if (isLampOn) (1.15f - animatedBrightness * 0.45f).coerceIn(0.4f, 1.0f) else 1f
            drawRect(color = tileTint.copy(alpha = (tileTint.alpha * tintFactor).coerceIn(0f, 0.72f)))

            // Soft Ambient Lamp Wash across the White Tile Floor
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        activeGlowColor.copy(alpha = 0.24f * animatedBrightness * lampPulse),
                        activeGlowColor.copy(alpha = 0.07f * animatedBrightness),
                        Color.Transparent
                    ),
                    center = Offset(w * 0.50f, h * 0.50f),
                    radius = h * 0.65f
                ),
                center = Offset(w * 0.50f, h * 0.50f),
                radius = h * 0.65f
            )

            // Ornate Central Reading Runner Rug over the White Tiles (leaves side tile borders clearly visible)
            val rugLeft = w * 0.28f
            val rugTop = h * 0.27f
            val rugW = w * 0.44f
            val rugH = h * 0.58f
            drawRoundRect(
                color = Color(0x38000000),
                topLeft = Offset(rugLeft + 4f, rugTop + 6f),
                size = Size(rugW, rugH),
                cornerRadius = CornerRadius(18f, 18f)
            )
            drawRoundRect(
                color = BurgundyLeather.copy(alpha = if (isLampOn) 0.80f else 0.62f),
                topLeft = Offset(rugLeft, rugTop),
                size = Size(rugW, rugH),
                cornerRadius = CornerRadius(18f, 18f)
            )
            drawRoundRect(
                color = BrassGold.copy(alpha = 0.45f + 0.35f * animatedBrightness),
                topLeft = Offset(rugLeft + 8f, rugTop + 8f),
                size = Size(rugW - 16f, rugH - 16f),
                cornerRadius = CornerRadius(14f, 14f),
                style = Stroke(width = 2.5f)
            )

            // Wooden Librarian Circulation Desk (top center)
            val (lx, ly) = LibraryFloorLayout.librarianSpot
            val deskCenter = Offset(w * lx, h * ly)
            val libDeskTopLeft = Offset(deskCenter.x - w * 0.16f, deskCenter.y - h * 0.032f)
            val libDeskSize = Size(w * 0.32f, h * 0.068f)
            if (nearbyLibrarian) {
                drawRoundRect(
                    color = BrassBright.copy(alpha = 0.45f * lampPulse),
                    topLeft = Offset(libDeskTopLeft.x - 6f, libDeskTopLeft.y - 6f),
                    size = Size(libDeskSize.width + 12f, libDeskSize.height + 12f),
                    cornerRadius = CornerRadius(20f, 20f)
                )
            }
            drawRoundRect(
                color = Color(0x45000000),
                topLeft = Offset(libDeskTopLeft.x + 3f, libDeskTopLeft.y + 5f),
                size = libDeskSize,
                cornerRadius = CornerRadius(16f, 16f)
            )
            drawTexturedWoodRect(
                woodBitmap = woodBitmap,
                topLeft = libDeskTopLeft,
                size = libDeskSize,
                cornerRadius = 16f
            )
            drawRoundRect(
                color = if (nearbyLibrarian) BrassBright else BrassGold,
                topLeft = libDeskTopLeft,
                size = libDeskSize,
                cornerRadius = CornerRadius(16f, 16f),
                style = Stroke(width = if (nearbyLibrarian) 3.2f else 2.2f)
            )

            // Librarian desk lamp glow
            if (isLampOn) {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            activeGlowColor.copy(alpha = 0.55f * animatedBrightness * lampPulse),
                            Color.Transparent
                        ),
                        center = Offset(deskCenter.x + w * 0.09f, deskCenter.y),
                        radius = w * 0.10f
                    ),
                    center = Offset(deskCenter.x + w * 0.09f, deskCenter.y),
                    radius = w * 0.10f
                )
            }
            // Librarian Eleanor silhouette behind desk
            drawCircle(
                color = Color(0xFFF3CBB2),
                radius = 12f,
                center = Offset(deskCenter.x, deskCenter.y - h * 0.042f)
            )
            drawCircle(
                color = Color(0xFFBDB6AE),
                radius = 7f,
                center = Offset(deskCenter.x, deskCenter.y - h * 0.056f)
            )
            drawRoundRect(
                color = EmeraldLibrarian,
                topLeft = Offset(deskCenter.x - 14f, deskCenter.y - h * 0.03f),
                size = Size(28f, 16f),
                cornerRadius = CornerRadius(6f, 6f)
            )

            // Draw 10 High-Resolution Wooden Bookshelves
            LibraryFloorLayout.bookshelves.forEach { shelf ->
                drawWoodenBookshelfUnit(
                    shelf = shelf,
                    woodBitmap = woodBitmap,
                    canvasW = w,
                    canvasH = h,
                    isHighlighted = highlightedShelfNumber == shelf.shelfNumber || nearbyShelfNumber == shelf.shelfNumber,
                    pulse = lampPulse
                )
            }

            // Draw 4 Wooden Study Tables with Selected Attractive Table Cover & Glowing Lamps
            LibraryFloorLayout.studyTables.forEach { table ->
                drawWoodenStudyTableUnit(
                    table = table,
                    woodBitmap = woodBitmap,
                    tableCover = tableCover,
                    canvasW = w,
                    canvasH = h,
                    isOccupied = seatedTableId == table.tableId,
                    isLampOn = isLampOn,
                    glowColor = activeGlowColor,
                    brightness = animatedBrightness,
                    pulse = lampPulse
                )
            }

            // Tap-to-walk target indicator
            if (isWalking) {
                val targetPx = Offset(w * targetX, h * targetY)
                drawCircle(
                    color = BrassBright.copy(alpha = 0.75f),
                    radius = 12f * lampPulse,
                    center = targetPx,
                    style = Stroke(width = 2.5f)
                )
            }

            // Player Character Sprite
            val charCenter = Offset(w * charX, h * charY)
            drawLibraryCharacter(
                center = charCenter,
                scale = (w / 390f).coerceIn(0.85f, 1.35f),
                avatar = avatar,
                cloakColor = cloakColor,
                walkPhase = walkPhase,
                isWalking = isWalking,
                facingRight = facingRight,
                carriedBooksCount = carriedBooksCount,
                isSeated = seatedTableId != null
            )

            // Cozy Floating Dust Motes / Starlight Sparkles
            val moteCount = if (!isLampOn || lampAmbience == LampAmbience.STARLIGHT) 18 else 12
            for (i in 0 until moteCount) {
                val angle = motePhase + i * 0.85f
                val mx = w * (0.12f + ((i * 41) % 76) / 100f) + sin(angle) * 16f
                val my = h * (0.16f + ((i * 59) % 70) / 100f) + cos(angle) * 12f
                val radius = if (lampAmbience == LampAmbience.STARLIGHT && isLampOn) 2.8f + (i % 2) else 2.2f + (i % 2)
                val moteAlpha = if (isLampOn) 0.45f * animatedBrightness else 0.18f
                drawCircle(
                    color = activeGlowColor.copy(alpha = moteAlpha),
                    radius = radius,
                    center = Offset(mx, my)
                )
            }

            // Soft Peripheral Vignette (subtle on bright white tiles, cozy when dimmed)
            val edgeShadowAlpha = (0.58f - animatedBrightness * 0.42f).coerceIn(0.10f, 0.62f)
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(Color.Transparent, Color(0xFF140D0B).copy(alpha = edgeShadowAlpha)),
                    center = Offset(w * 0.50f, h * 0.52f),
                    radius = h * 0.74f
                )
            )
        }

        // Crisp Overlaid Labels for Shelves, Tables, Librarian, and Character Name Tag
        LibraryFloorLayout.bookshelves.forEach { shelf ->
            val xPx = (shelf.normX * widthPx).roundToInt()
            val yPx = (shelf.normY * heightPx).roundToInt()
            val isShelfActive = highlightedShelfNumber == shelf.shelfNumber || nearbyShelfNumber == shelf.shelfNumber
            Surface(
                color = if (isShelfActive) BrassGold else MahoganyDark.copy(alpha = 0.92f),
                shape = RoundedCornerShape(8.dp),
                shadowElevation = 3.dp,
                modifier = Modifier
                    .offset {
                        IntOffset(
                            x = (xPx - with(density) { 42.dp.roundToPx() }).coerceAtLeast(4),
                            y = yPx + with(density) { 12.dp.roundToPx() }
                        )
                    }
                    .clickable { onTapShelf(shelf.shelfNumber) }
                    .testTag("shelf_badge_${shelf.shelfNumber}")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(shelf.category.accentColor)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "#${shelf.shelfNumber} ${shelf.category.displayName.take(7)}",
                        color = if (isShelfActive) MahoganyDark else ParchmentCream,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        // Table Lamps Labels
        LibraryFloorLayout.studyTables.forEach { table ->
            val xPx = (table.normX * widthPx).roundToInt()
            val yPx = (table.normY * heightPx).roundToInt()
            Surface(
                color = MahoganyRich.copy(alpha = 0.92f),
                shape = RoundedCornerShape(8.dp),
                shadowElevation = 3.dp,
                modifier = Modifier
                    .offset {
                        IntOffset(
                            x = xPx - with(density) { 30.dp.roundToPx() },
                            y = yPx + with(density) { 20.dp.roundToPx() }
                        )
                    }
                    .clickable { onTapTable(table.tableId) }
                    .testTag("table_badge_${table.tableId}")
            ) {
                Text(
                    text = table.label,
                    color = BrassBright,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                )
            }
        }

        // Librarian Badge
        val (lx, ly) = LibraryFloorLayout.librarianSpot
        Surface(
            color = if (nearbyLibrarian) BrassGold else EmeraldLibrarian,
            shape = RoundedCornerShape(10.dp),
            shadowElevation = 3.dp,
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = (lx * widthPx).roundToInt() - with(density) { 68.dp.roundToPx() },
                        y = (ly * heightPx).roundToInt() + with(density) { 14.dp.roundToPx() }
                    )
                }
                .clickable { onTapLibrarian() }
                .testTag("librarian_desk_badge")
        ) {
            Text(
                text = "LIBRARY CENTRE • ELEANOR",
                color = if (nearbyLibrarian) MahoganyDark else Color.White,
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
            )
        }

        // Floating Character Name Pill
        Surface(
            color = ParchmentSoft.copy(alpha = 0.95f),
            shape = RoundedCornerShape(10.dp),
            shadowElevation = 3.dp,
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = (charX * widthPx).roundToInt() - with(density) { 40.dp.roundToPx() },
                        y = (charY * heightPx).roundToInt() - with(density) { 46.dp.roundToPx() }
                    )
                }
                .border(1.dp, BrassGold, RoundedCornerShape(10.dp))
        ) {
            Text(
                text = characterName.uppercase(),
                color = MahoganyDark,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
            )
        }

        // Smooth 360-degree Analog Joystick for walking across the library hall
        VirtualJoystick(
            onDirectionChange = onJoystickMove,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .navigationBarsPadding()
                .padding(end = 14.dp, bottom = 14.dp)
        )
    }
}

private fun DrawScope.drawTexturedWoodRect(
    woodBitmap: ImageBitmap,
    topLeft: Offset,
    size: Size,
    cornerRadius: Float
) {
    val clip = Path().apply {
        addRoundRect(
            RoundRect(
                left = topLeft.x,
                top = topLeft.y,
                right = topLeft.x + size.width,
                bottom = topLeft.y + size.height,
                cornerRadius = CornerRadius(cornerRadius, cornerRadius)
            )
        )
    }
    clipPath(clip) {
        drawImage(
            image = woodBitmap,
            dstOffset = IntOffset(topLeft.x.roundToInt(), topLeft.y.roundToInt()),
            dstSize = IntSize(size.width.roundToInt().coerceAtLeast(1), size.height.roundToInt().coerceAtLeast(1))
        )
        // Subtle timber bevel highlight along top edge
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0x33FFE0B2), Color.Transparent, Color(0x441B0D08)),
                startY = topLeft.y,
                endY = topLeft.y + size.height
            ),
            topLeft = topLeft,
            size = size
        )
    }
}

private fun DrawScope.drawWoodenBookshelfUnit(
    shelf: BookshelfInfo,
    woodBitmap: ImageBitmap,
    canvasW: Float,
    canvasH: Float,
    isHighlighted: Boolean,
    pulse: Float
) {
    val cx = canvasW * shelf.normX
    val cy = canvasH * shelf.normY
    val sw = canvasW * 0.19f
    val sh = canvasH * 0.082f
    val topLeft = Offset(cx - sw / 2f, cy - sh / 2f)

    if (isHighlighted) {
        drawRoundRect(
            color = BrassBright.copy(alpha = 0.50f * pulse),
            topLeft = Offset(topLeft.x - 6f, topLeft.y - 6f),
            size = Size(sw + 12f, sh + 12f),
            cornerRadius = CornerRadius(14f, 14f)
        )
    }

    // Drop Shadow on White Design Tile Floor
    drawRoundRect(
        color = Color(0x42000000),
        topLeft = Offset(topLeft.x + 3f, topLeft.y + 5f),
        size = Size(sw, sh),
        cornerRadius = CornerRadius(10f, 10f)
    )

    // Real High-Res Wooden Bookshelf Frame
    drawTexturedWoodRect(
        woodBitmap = woodBitmap,
        topLeft = topLeft,
        size = Size(sw, sh),
        cornerRadius = 10f
    )
    drawRoundRect(
        color = Color(0xFF472716),
        topLeft = topLeft,
        size = Size(sw, sh),
        cornerRadius = CornerRadius(10f, 10f),
        style = Stroke(width = 2.2f)
    )

    // Recessed Dark Timber Interior Shelf Cavity
    drawRoundRect(
        color = Color(0xCC24130B),
        topLeft = Offset(topLeft.x + 5f, topLeft.y + 5f),
        size = Size(sw - 10f, sh - 10f),
        cornerRadius = CornerRadius(6f, 6f)
    )

    // Colorful book spines arranged neatly inside wooden shelf
    val bookCount = 7
    val innerStart = topLeft.x + 8f
    val spineW = (sw - 16f) / bookCount
    val palette = listOf(
        shelf.category.accentColor,
        BrassGold,
        BurgundyLeather,
        EmeraldLibrarian,
        shelf.category.accentColor.copy(alpha = 0.85f),
        Color(0xFF455A64),
        Color(0xFFB5691E)
    )
    for (i in 0 until bookCount) {
        val bx = innerStart + i * spineW + 1f
        val bh = sh * (0.62f + ((i + shelf.shelfNumber) % 3) * 0.06f)
        val bw = (spineW - 2f).coerceAtLeast(3f)
        val by = topLeft.y + (sh - bh) / 2f
        drawRoundRect(
            color = palette[(i + shelf.shelfNumber) % palette.size],
            topLeft = Offset(bx, by),
            size = Size(bw, bh),
            cornerRadius = CornerRadius(2f, 2f)
        )
        // Gilt book spine band
        drawLine(
            color = BrassBright.copy(alpha = 0.65f),
            start = Offset(bx + 1f, by + bh * 0.25f),
            end = Offset(bx + bw - 1f, by + bh * 0.25f),
            strokeWidth = 1.2f
        )
    }
}

private fun DrawScope.drawWoodenStudyTableUnit(
    table: StudyTableInfo,
    woodBitmap: ImageBitmap,
    tableCover: TableCoverStyle,
    canvasW: Float,
    canvasH: Float,
    isOccupied: Boolean,
    isLampOn: Boolean,
    glowColor: Color,
    brightness: Float,
    pulse: Float
) {
    val cx = canvasW * table.normX
    val cy = canvasH * table.normY
    val tw = canvasW * 0.165f
    val th = canvasH * 0.098f
    val tableTopLeft = Offset(cx - tw / 2f, cy - th / 2f)

    // Warm Lamp Glow Cone on White Tile Floor & Wooden Table
    if (isLampOn) {
        val baseAlpha = (if (isOccupied) 0.60f else 0.42f) * brightness * pulse
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    glowColor.copy(alpha = baseAlpha.coerceIn(0.10f, 0.75f)),
                    glowColor.copy(alpha = (baseAlpha * 0.30f).coerceIn(0.03f, 0.25f)),
                    Color.Transparent
                ),
                center = Offset(cx, cy),
                radius = tw * (0.98f + 0.25f * brightness)
            ),
            center = Offset(cx, cy),
            radius = tw * (0.98f + 0.25f * brightness)
        )
    }

    // Wooden Study Chair in front of table
    val chairTopLeft = Offset(cx - tw * 0.23f, cy + th * 0.42f)
    val chairSize = Size(tw * 0.46f, th * 0.38f)
    drawRoundRect(
        color = Color(0x38000000),
        topLeft = Offset(chairTopLeft.x + 2f, chairTopLeft.y + 4f),
        size = chairSize,
        cornerRadius = CornerRadius(6f, 6f)
    )
    drawTexturedWoodRect(
        woodBitmap = woodBitmap,
        topLeft = chairTopLeft,
        size = chairSize,
        cornerRadius = 6f
    )
    drawRoundRect(
        color = OakWarm,
        topLeft = chairTopLeft,
        size = chairSize,
        cornerRadius = CornerRadius(6f, 6f),
        style = Stroke(width = 1.6f)
    )

    // Shadow under Wooden Table
    drawRoundRect(
        color = Color(0x48000000),
        topLeft = Offset(tableTopLeft.x + 3f, tableTopLeft.y + 5f),
        size = Size(tw, th),
        cornerRadius = CornerRadius(12f, 12f)
    )

    // High-Resolution Wooden Tabletop
    drawTexturedWoodRect(
        woodBitmap = woodBitmap,
        topLeft = tableTopLeft,
        size = Size(tw, th),
        cornerRadius = 12f
    )
    drawRoundRect(
        color = Color(0xFF472716),
        topLeft = tableTopLeft,
        size = Size(tw, th),
        cornerRadius = CornerRadius(12f, 12f),
        style = Stroke(width = 2.2f)
    )

    // Optional Attractive Table Sheet / Cover Draped on the Wooden Table
    if (tableCover.hasClothCover) {
        val coverLeft = tableTopLeft.x + tw * 0.10f
        val coverTop = tableTopLeft.y + th * 0.10f
        val coverW = tw * 0.80f
        val coverH = th * 0.80f
        drawRoundRect(
            color = tableCover.primaryColor,
            topLeft = Offset(coverLeft, coverTop),
            size = Size(coverW, coverH),
            cornerRadius = CornerRadius(8f, 8f)
        )
        drawRoundRect(
            color = tableCover.trimColor,
            topLeft = Offset(coverLeft + 3f, coverTop + 3f),
            size = Size(coverW - 6f, coverH - 6f),
            cornerRadius = CornerRadius(6f, 6f),
            style = Stroke(width = 1.5f)
        )
        // Center Diamond Embroidery Motif
        val centerMotif = Path().apply {
            moveTo(cx, coverTop + 6f)
            lineTo(cx + coverW * 0.14f, cy)
            lineTo(cx, coverTop + coverH - 6f)
            lineTo(cx - coverW * 0.14f, cy)
            close()
        }
        drawPath(centerMotif, color = tableCover.accentPatternColor.copy(alpha = 0.7f))
        drawPath(centerMotif, color = tableCover.trimColor.copy(alpha = 0.8f), style = Stroke(width = 1f))
    }

    // Mini Desk Lamp & Open Book Icon on Table
    drawCircle(
        color = if (isLampOn) glowColor else Color(0xFF5C4B3B),
        radius = 6f,
        center = Offset(cx - tw * 0.22f, cy - th * 0.15f)
    )
    drawCircle(
        color = if (isLampOn) EmeraldLibrarian else Color(0xFF283A33),
        radius = 8f,
        center = Offset(cx - tw * 0.22f, cy - th * 0.15f),
        style = Stroke(width = 2.5f)
    )
    // Little open book pages on table
    drawRoundRect(
        color = ParchmentCream.copy(alpha = if (isLampOn) 1f else 0.72f),
        topLeft = Offset(cx + tw * 0.02f, cy - th * 0.18f),
        size = Size(tw * 0.30f, th * 0.36f),
        cornerRadius = CornerRadius(2f, 2f)
    )
}
