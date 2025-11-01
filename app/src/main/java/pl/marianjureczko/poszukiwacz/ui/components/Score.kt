package pl.marianjureczko.poszukiwacz.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import pl.marianjureczko.poszukiwacz.ui.theme.FANCY_FONT

@Composable
fun Score(value: Int, @DrawableRes painterResourceId: Int, contentDescription: String) {
    val textStyle = TextStyle(
        fontFamily = FANCY_FONT,
        fontSize = 32.sp,
    )
    Image(
        painterResource(painterResourceId),
        contentDescription = contentDescription,
        contentScale = ContentScale.Inside,
    )
    Text(
        color = Color.Gray,
        text = value.toString(),
        style = textStyle,
    )
}
