package com.example.undermap.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    background = ColorDark.BkgColor,
    surface = ColorDark.ShallowBackColor,

    primary = ColorDark.DeepBackColor,
    secondary = ColorDark.TextLightColor,
    tertiary = ColorDark.AccentColor,


    onBackground = ColorDark.TextLightColor,
    onSurface = ColorDark.TextColdColor,

    onPrimary = ColorDark.TextWarmColor,
    onSecondary = ColorDark.TextDarkColor,
    onTertiary = ColorDark.TextLightColor,
)

private val LightColorScheme = lightColorScheme(
    background = ColorLight.BkgColor,
    surface = ColorLight.ShallowBackColor,

    primary = ColorLight.DeepBackColor,
    secondary = ColorLight.TextDarkColor,
    tertiary = ColorLight.AccentColor,


    onBackground = ColorLight.TextDarkColor,
    onSurface = ColorLight.TextColdColor,

    onPrimary = ColorLight.TextWarmColor,
    onSecondary = ColorLight.TextLightColor,
    onTertiary = ColorLight.TextDarkColor
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {

    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}