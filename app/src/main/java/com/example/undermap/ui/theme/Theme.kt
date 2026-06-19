package com.example.undermap.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color


val LocalCustomColors = staticCompositionLocalOf{
    CustomColors(
        bkgColor = Color.Unspecified,

        textDarkColor = Color.Unspecified,
        textLightColor = Color.Unspecified,
        textColdColor = Color.Unspecified,
        textWarmColor = Color.Unspecified,

        deepBackColor = Color.Unspecified,
        shallowBackColor = Color.Unspecified,

        accentColor = Color.Unspecified,

        navigationBarColor = Color.Unspecified,

        iconColor = Color.Unspecified,
    )
}


@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val customColors = if (darkTheme) ColorDark else ColorLight

    CompositionLocalProvider(LocalCustomColors provides customColors) {

        val colors = if (darkTheme) darkColorScheme() else lightColorScheme()

        MaterialTheme(
            colorScheme = colors,
            typography = Typography,
            content = content
        )
    }
}

object AppColors {
    val current: CustomColors
        @Composable
        get() = LocalCustomColors.current
}