package com.resona.ui.theme

import androidx.compose.ui.graphics.Color

val Crimson = Color(0xFFC62828)
val CrimsonBright = Color(0xFFEF5350)
val BackgroundDark = Color(0xFF0F0A0A)
val SurfaceDark = Color(0xFF1C1010)
val SurfaceVariantDark = Color(0xFF2A1515)
val OutlineDark = Color(0xFF3D2020)
val OnSurfaceDark = Color(0xFFF5F0F0)
val OnSurfaceMuted = Color(0xFFA08080)

/**
 * A selectable theme seed. Each entry pairs an accent (primary/secondary) with a
 * dark surface set subtly tinted toward that hue, so switching seeds feels cohesive
 * rather than just swapping one highlight color.
 */
enum class AccentColor(
    val label: String,
    val swatch: Color,
    val primary: Color,
    val onPrimary: Color,
    val secondary: Color,
    val background: Color,
    val surface: Color,
    val surfaceVariant: Color,
    val outline: Color,
    val onSurfaceVariant: Color,
) {
    Crimson(
        label = "Crimson",
        swatch = Color(0xFFC62828),
        primary = Color(0xFFC62828),
        onPrimary = OnSurfaceDark,
        secondary = Color(0xFFEF5350),
        background = Color(0xFF0F0A0A),
        surface = Color(0xFF1C1010),
        surfaceVariant = Color(0xFF2A1515),
        outline = Color(0xFF3D2020),
        onSurfaceVariant = Color(0xFFA08080),
    ),
    Amber(
        label = "Amber",
        swatch = Color(0xFFC6791A),
        primary = Color(0xFFC6791A),
        onPrimary = OnSurfaceDark,
        secondary = Color(0xFFEF9F50),
        background = Color(0xFF0F0C0A),
        surface = Color(0xFF1C1610),
        surfaceVariant = Color(0xFF2A2015),
        outline = Color(0xFF3D2E1C),
        onSurfaceVariant = Color(0xFFA08F70),
    ),
    Violet(
        label = "Violet",
        swatch = Color(0xFF7B3FC6),
        primary = Color(0xFF7B3FC6),
        onPrimary = OnSurfaceDark,
        secondary = Color(0xFFAB7AEF),
        background = Color(0xFF0F0A14),
        surface = Color(0xFF1B1024),
        surfaceVariant = Color(0xFF291533),
        outline = Color(0xFF3B2050),
        onSurfaceVariant = Color(0xFF9A88A8),
    ),
    Azure(
        label = "Azure",
        swatch = Color(0xFF2864C6),
        primary = Color(0xFF2864C6),
        onPrimary = OnSurfaceDark,
        secondary = Color(0xFF66A0F2),
        background = Color(0xFF0A0D14),
        surface = Color(0xFF101824),
        surfaceVariant = Color(0xFF152233),
        outline = Color(0xFF203150),
        onSurfaceVariant = Color(0xFF8098A8),
    ),
    Teal(
        label = "Teal",
        swatch = Color(0xFF1C8C7D),
        primary = Color(0xFF1C8C7D),
        onPrimary = OnSurfaceDark,
        secondary = Color(0xFF5FD1BE),
        background = Color(0xFF0A0F0E),
        surface = Color(0xFF10201C),
        surfaceVariant = Color(0xFF152E28),
        outline = Color(0xFF20423A),
        onSurfaceVariant = Color(0xFF80A89F),
    ),
    Forest(
        label = "Forest",
        swatch = Color(0xFF3B8C3A),
        primary = Color(0xFF3B8C3A),
        onPrimary = OnSurfaceDark,
        secondary = Color(0xFF7BCB6B),
        background = Color(0xFF0A0F0A),
        surface = Color(0xFF101C10),
        surfaceVariant = Color(0xFF152A15),
        outline = Color(0xFF203D20),
        onSurfaceVariant = Color(0xFF87A880),
    );

    companion object {
        val Default = Crimson

        fun fromName(name: String?): AccentColor =
            entries.firstOrNull { it.name == name } ?: Default
    }
}
