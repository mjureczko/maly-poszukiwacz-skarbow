package pl.marianjureczko.poszukiwacz.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pl.marianjureczko.poszukiwacz.ui.dp2SameSizeSp
import pl.marianjureczko.poszukiwacz.ui.theme.FANCY_FONT

@Composable
fun Score(value: Int, @DrawableRes painterResourceId: Int, contentDescription: String, maxHeight: Dp) {
    val textStyle = TextStyle(
        fontFamily = FANCY_FONT,
        fontSize = dp2SameSizeSp(maxHeight),
    )
    Image(
        painterResource(painterResourceId),
        contentDescription = contentDescription,
        contentScale = ContentScale.Inside,
    )
    Text(
        modifier = Modifier.offset(y = (-5).dp),
        color = Color.Gray,
        text = value.toString(),
        style = textStyle,
    )
}
