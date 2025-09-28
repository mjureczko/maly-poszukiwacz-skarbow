package pl.marianjureczko.poszukiwacz.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import pl.marianjureczko.poszukiwacz.R

val FANCY_FONT = FontFamily(Font(R.font.akaya_telivigala))

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    displayLarge = TextStyle(
        fontFamily = FANCY_FONT,
        fontSize = 57.sp,
    ),
    headlineLarge = TextStyle(
        fontFamily = FANCY_FONT,
        fontSize = 32.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = FANCY_FONT,
        fontSize = 28.sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = FANCY_FONT,
        fontSize = 22.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = FANCY_FONT,
    ),
)