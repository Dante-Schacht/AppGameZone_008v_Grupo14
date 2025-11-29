package com.example.appgamezone_008v_grupo14.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val GameZoneColorScheme = darkColorScheme(
    primary = RedstoneBright,      // Botones y elementos principales
    onPrimary = Color.White,         // Texto sobre botones
    secondary = RedstoneAccent,    // Acentos y elementos secundarios
    onSecondary = Color.White,
    background = RedstoneDark,       // Fondo de la app
    onBackground = Color(0xFFF3F3F3),// Texto sobre el fondo
    surface = RedstoneDeep,        // Color para Cards, TopAppBar, etc.
    onSurface = Color(0xFFF3F3F3),  // Texto sobre las superficies
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005)
)

@Composable
fun AppGameZone_008v_Grupo14Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // <- Desactivamos el color dinámico
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = GameZoneColorScheme, // <- Usamos siempre nuestro tema
        typography = Typography,
        content = content
    )
}
