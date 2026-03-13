package com.example.undermap.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.undermap.R

val RobotoFamily = FontFamily(
    Font(R.font.roboto, FontWeight.Normal),
)
val MPlusFamily = FontFamily(
    Font(R.font.mplus2_light, FontWeight.Light),
    Font(R.font.mplus2, FontWeight.Normal),
)
val VarelaFamily = FontFamily(
    Font(R.font.varela, FontWeight.Normal),
)



val Typography = Typography(
    labelMedium = TextStyle(
        fontFamily = RobotoFamily,
        fontWeight = FontWeight.W500,
        fontSize = 7.2.sp,
        lineHeight = 24.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = MPlusFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 7.5.sp,
        lineHeight = 24.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = VarelaFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 7.5.sp,
        lineHeight = 24.sp,
    ),
)