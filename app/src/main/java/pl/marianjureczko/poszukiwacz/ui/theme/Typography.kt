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
    // Set default text style for the entire app
    //TODO t: remove
    displayLarge = TextStyle(
        fontFamily = FANCY_FONT,
        fontSize = 57.sp,
    ),
//    displayMedium = TextStyle(
//        fontFamily = FANCY_FONT,
//    ),
//    displaySmall = TextStyle(
//        fontFamily = FANCY_FONT,
//    ),
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
//    titleLarge = TextStyle(
//        fontFamily = FANCY_FONT,
//    ),
    titleMedium = TextStyle(
        fontFamily = FANCY_FONT,
    ),
//    titleSmall = TextStyle(
//        fontFamily = FANCY_FONT,
//    ),
//    bodySmall = TextStyle(
//        fontFamily = FANCY_FONT,
//    ),
//    labelLarge = TextStyle(
//        fontFamily = FANCY_FONT,
//        fontWeight = FontWeight.W800,
//        fontSize = 19.sp,
//        lineHeight = 24.sp,
//        letterSpacing = 0.sp
//    ),
//    labelMedium = TextStyle(
//        fontFamily = FANCY_FONT,
//        fontSize = 28.sp,
//    ),
//    labelSmall = TextStyle(
//        fontFamily = FANCY_FONT,
//    )
)