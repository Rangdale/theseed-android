package com.theseed.app.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val TheSeedColorScheme = lightColorScheme(
    primary = ForestGreen,
    background = BackgroundOffWhite,
    surface = SurfaceWhite,
    onPrimary = SurfaceWhite,
    onBackground = CharcoalText,
    onSurface = CharcoalText,
    outline = BorderGray,
    error = Color(0xFFEC5E5E)
)

@Composable
fun TheSeedTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = TheSeedColorScheme,
        content = content
    )
}