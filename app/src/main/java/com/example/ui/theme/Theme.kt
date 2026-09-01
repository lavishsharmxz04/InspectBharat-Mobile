package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = GovNavyPrimary,
    onPrimary = Color.White,
    primaryContainer = GovNavyContainer,
    onPrimaryContainer = GovNavyDark,
    secondary = GovIndiaGreen,
    onSecondary = Color.White,
    secondaryContainer = GovGreenLight,
    onSecondaryContainer = GovGreenDark,
    tertiary = GovSaffronAccent,
    onTertiary = Color.White,
    tertiaryContainer = GovSaffronLight,
    onTertiaryContainer = GovAmberWarning,
    background = GovBackground,
    onBackground = GovTextPrimary,
    surface = GovSurface,
    onSurface = GovTextPrimary,
    surfaceVariant = GovSurfaceVariant,
    onSurfaceVariant = GovTextSecondary,
    outline = GovBorder,
    outlineVariant = GovBorderLight,
    error = GovRedDanger,
    onError = Color.White,
    errorContainer = GovRedLight,
    onErrorContainer = GovRedDanger
)

private val DarkColorScheme = darkColorScheme(
    primary = GovNavyLight,
    onPrimary = Color.White,
    primaryContainer = GovDarkSurfaceVariant,
    onPrimaryContainer = Color.White,
    secondary = GovIndiaGreen,
    onSecondary = Color.White,
    secondaryContainer = GovDarkSurfaceVariant,
    onSecondaryContainer = GovGreenLight,
    tertiary = GovSaffronAccent,
    onTertiary = Color.White,
    background = GovDarkBackground,
    onBackground = GovDarkTextPrimary,
    surface = GovDarkSurface,
    onSurface = GovDarkTextPrimary,
    surfaceVariant = GovDarkSurfaceVariant,
    onSurfaceVariant = GovDarkTextSecondary,
    outline = GovDarkBorder,
    error = GovRedDanger,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
