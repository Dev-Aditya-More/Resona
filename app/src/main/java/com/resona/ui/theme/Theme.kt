package com.resona.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private fun colorSchemeFor(accent: AccentColor) = darkColorScheme(
    primary = accent.primary,
    onPrimary = accent.onPrimary,
    secondary = accent.secondary,
    onSecondary = accent.onPrimary,
    background = accent.background,
    onBackground = OnSurfaceDark,
    surface = accent.surface,
    onSurface = OnSurfaceDark,
    surfaceVariant = accent.surfaceVariant,
    onSurfaceVariant = accent.onSurfaceVariant,
    outline = accent.outline,
)

@Composable
fun ResonaTheme(accent: AccentColor = AccentColor.Default, content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = colorSchemeFor(accent),
        typography = Typography,
        content = content
    )
}
