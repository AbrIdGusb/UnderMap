package com.example.undermap.ui.theme

import androidx.compose.ui.graphics.Color

data class CustomColors(
    val bkgColor : Color,

    val textLightColor : Color,
    val textColdColor : Color,
    val textWarmColor : Color,
    val textDarkColor : Color,

    val deepBackColor : Color,
    val shallowBackColor : Color,

    val accentColor : Color,

    val navigationBarColor : Color,

    val iconColor : Color,
)

val ColorDark = CustomColors(
    bkgColor = Color(0xFF272727),

    textLightColor = Color(0xFFF2F2F2),
    textColdColor = Color(0xFF808692),
    textWarmColor = Color(0xFFA5ABA7),
    textDarkColor = Color(0xFF303337),

    deepBackColor = Color(0xFF202427),
    shallowBackColor = Color(0xFF303337),

    accentColor = Color(0xFF505658),

    navigationBarColor = Color(0xFF343438),

    iconColor = Color(0xFFA5ABA7),
)

val ColorLight = CustomColors(

    bkgColor = Color(0xFFFFFFFF),

    textDarkColor = Color(0xFF151719),
    textLightColor = Color(0xFF999999),
    textColdColor = Color(0xFF858789),
    textWarmColor = Color(0xFF505658),

    deepBackColor = Color(0xFFF5F5F5),
    shallowBackColor = Color(0xFFFFFFFF),

    accentColor = Color(0xFFCACDCC),

    navigationBarColor = Color(0xFFFFFFFF),

    iconColor = Color(0xFF858789),
)