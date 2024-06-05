package pl.marianjureczko.poszukiwacz.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import pl.marianjureczko.poszukiwacz.R

val FANCY_FONT = FontFamily(Font(R.font.akaya_telivigala))

val Typography = Typography(
    defaultFontFamily = FANCY_FONT,
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    button = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.W800,
        fontSize = 18.sp,
    )
)