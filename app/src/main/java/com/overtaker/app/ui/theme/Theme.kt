package com.overtaker.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = OvertakerYellow,
    secondary = OvertakerRed,
    tertiary = OvertakerGreenLight,
    background = OvertakerDarkBlue,
    surface = OvertakerDarkBlue,
    onPrimary = Color.Black,
    onBackground = OvertakerWhite,
    onSurface = OvertakerWhite,
    error = OvertakerRed
)

private val LightColorScheme = lightColorScheme(
    primary = OvertakerDarkBlue,
    secondary = OvertakerRed,
    tertiary = OvertakerGreenDark,
    background = OvertakerWhite,
    surface = OvertakerWhite,
    onPrimary = OvertakerWhite,
    onBackground = OvertakerDarkBlue,
    onSurface = OvertakerDarkBlue,
    error = OvertakerRed
)

@Composable
fun OvertakerTheme(
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
