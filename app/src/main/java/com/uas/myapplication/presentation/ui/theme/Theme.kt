package com.uas.myapplication.presentation.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// =============================================
// COLOR SCHEME — LIGHT MODE
// =============================================
private val LightColorScheme = lightColorScheme(
    primary          = Blue700,
    onPrimary        = White,
    primaryContainer = Blue100,
    onPrimaryContainer = Blue800,

    background       = SlateWhite,
    onBackground     = TextMain,

    surface          = White,
    onSurface        = TextMain,
    surfaceVariant   = SlateGray50,
    onSurfaceVariant = TextSub,

    outline          = SlateGray200,
    outlineVariant   = SlateGray100,
    error            = DangerRed,
    onError          = White,
)

// =============================================
// COLOR SCHEME — DARK MODE
// =============================================
private val DarkColorScheme = darkColorScheme(
    primary          = Blue700,
    onPrimary        = White,
    primaryContainer = Blue800,
    onPrimaryContainer = Blue100,

    background       = DarkBackground,
    onBackground     = TextMainDark,

    surface          = DarkSurface,
    onSurface        = TextMainDark,
    surfaceVariant   = DarkSurface2,
    onSurfaceVariant = TextSubDark,

    outline          = DarkSurface2,
    outlineVariant   = SlateGray100Dark,
    error            = DangerRed,
    onError          = White,
)

// =============================================
// WARNA KUSTOM CARI.IN — Yang tidak ada di Material3
// Akses via CariInTheme.colors.xxx
// =============================================
@Immutable
data class CariInColors(
    val textHint: Color,
    val divider: Color,
    val imagePlaceholder: Color,
    val switchCheckedThumb: Color,
    val switchCheckedTrack: Color,
    val switchUncheckedThumb: Color,
    val switchUncheckedTrack: Color,
)

val LightCariInColors = CariInColors(
    textHint             = TextHint,
    divider              = SlateGray100,
    imagePlaceholder     = SlateGray100,
    switchCheckedThumb   = White,
    switchCheckedTrack   = Blue700,
    switchUncheckedThumb = White,
    switchUncheckedTrack = SlateGray200,
)

val DarkCariInColors = CariInColors(
    textHint             = TextHintDark,
    divider              = SlateGray100Dark,
    imagePlaceholder     = DarkSurface2,
    switchCheckedThumb   = White,
    switchCheckedTrack   = Blue700,
    switchUncheckedThumb = SlateGray200,
    switchUncheckedTrack = DarkSurface2,
)

val LocalCariInColors = staticCompositionLocalOf { LightCariInColors }

// =============================================
// AKSES TEMA KUSTOM — CariInTheme.colors.xxx
// =============================================
object CariInTheme {
    val colors: CariInColors
        @Composable
        get() = LocalCariInColors.current
}

// =============================================
// TEMA UTAMA APLIKASI CARI.IN
// =============================================
@Composable
fun MyApplicationTheme(
    // Ikuti pengaturan sistem (dark/light) secara otomatis
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val cariInColors = if (darkTheme) DarkCariInColors else LightCariInColors

    CompositionLocalProvider(
        LocalCariInColors provides cariInColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography  = CariInTypography,
            content     = content
        )
    }
}