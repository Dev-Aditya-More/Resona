package com.resona.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val ResonaDarkColorScheme = darkColorScheme(
    primary = Crimson,
    onPrimary = OnSurfaceDark,
    secondary = CrimsonBright,
    onSecondary = OnSurfaceDark,
    background = BackgroundDark,
    onBackground = OnSurfaceDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceMuted,
    outline = OutlineDark,
)

@Composable
fun ResonaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ResonaDarkColorScheme,
        typography = Typography,
        content = content
    )
}
