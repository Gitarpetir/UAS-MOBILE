package com.uas.myapplication.presentation.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
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
    error            = DangerRed,
    onError          = White,
)

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

    // Mengatur warna status bar agar sesuai dengan tema
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = CariInTypography,
        content     = content
    )
}