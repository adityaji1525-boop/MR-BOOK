package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val LightColorScheme = lightColorScheme(
    primary = BurgundyLeather,
    onPrimary = ParchmentCream,
    primaryContainer = ParchmentAged,
    onPrimaryContainer = MahoganyDark,
    secondary = EmeraldLibrarian,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD5EBE2),
    onSecondaryContainer = Color(0xFF0E382B),
    tertiary = BrassGold,
    onTertiary = MahoganyDark,
    tertiaryContainer = Color(0xFFFFEBC2),
    onTertiaryContainer = Color(0xFF3E2900),
    background = ParchmentCream,
    onBackground = InkCharcoal,
    surface = ParchmentSoft,
    onSurface = InkCharcoal,
    surfaceVariant = ParchmentAged,
    onSurfaceVariant = InkMuted,
    outline = OakLight
)

private val DarkColorScheme = darkColorScheme(
    primary = BrassBright,
    onPrimary = MahoganyDark,
    primaryContainer = BurgundyLeather,
    onPrimaryContainer = ParchmentCream,
    secondary = EmeraldSoft,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF174537),
    onSecondaryContainer = Color(0xFFBDE8D8),
    tertiary = LampAmber,
    onTertiary = MahoganyDark,
    background = MahoganyDark,
    onBackground = ParchmentCream,
    surface = Color(0xFF37231E),
    onSurface = ParchmentCream,
    surfaceVariant = MahoganyRich,
    onSurfaceVariant = ParchmentAged,
    outline = OakWarm
)

private val MRBookShapes = Shapes(
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp)
)

@Composable
fun MRBookTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = MRBookTypography,
        shapes = MRBookShapes,
        content = content
    )
}
