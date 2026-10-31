package com.example.ui.theme

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

private val ShinobiColorScheme = darkColorScheme(
    primary = UzumakiOrange,
    onPrimary = KonohaBackground,
    primaryContainer = FlameAmber,
    onPrimaryContainer = KonohaBackground,
    secondary = RasenganCyan,
    onSecondary = KonohaBackground,
    secondaryContainer = RasenganBlue,
    onSecondaryContainer = TextNinjaPrimary,
    tertiary = FireCrimson,
    onTertiary = TextNinjaPrimary,
    background = KonohaBackground,
    onBackground = TextNinjaPrimary,
    surface = KonohaSurface,
    onSurface = TextNinjaPrimary,
    surfaceVariant = KonohaCard,
    onSurfaceVariant = TextNinjaSecondary,
    outline = KonohaBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Immersive Vontade do Fogo dark scheme
    val colorScheme = ShinobiColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = KonohaBackground.toArgb()
            window.navigationBarColor = KonohaBackground.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
