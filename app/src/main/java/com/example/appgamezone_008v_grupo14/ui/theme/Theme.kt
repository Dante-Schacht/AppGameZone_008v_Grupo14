package com.example.appgamezone_008v_grupo14.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = GameZoneBrighter,
    onPrimary = Color.White,
    secondary = GameZoneBright,
    onSecondary = Color.White,
    background = GameZoneDarkest,
    onBackground = Color(0xFFF5F5F5),
    surface = GameZoneDark,
    onSurface = Color(0xFFF5F5F5),
    tertiary = GameZoneMedium
)

private val LightColorScheme = lightColorScheme(
    primary = GameZoneBright,
    onPrimary = Color.White,
    secondary = GameZoneBrighter,
    onSecondary = Color.White,
    background = Color(0xFFFFF9F7),
    onBackground = GameZoneDarkest,
    surface = Color.White,
    onSurface = GameZoneDarkest,
    tertiary = GameZoneMedium
)

@Composable
fun AppGameZone_008v_Grupo14Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
