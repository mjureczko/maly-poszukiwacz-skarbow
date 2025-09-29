package pl.marianjureczko.poszukiwacz.screen.searching

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
import pl.marianjureczko.poszukiwacz.ui.Screen.dh
import pl.marianjureczko.poszukiwacz.ui.theme.FANCY_FONT

@Composable
fun Scores(modifier: Modifier = Modifier, score: TreasuresProgress) {
    Row(
        modifier = modifier
            .padding(10.dp)
            .fillMaxWidth()
            .background(Color.Transparent)
            .height(0.05.dh),
    ) {
        val textStyle = TextStyle(
            fontFamily = FANCY_FONT,
            fontSize = 42.sp,
        )
        Image(
            painterResource(R.drawable.gold),
            contentDescription = "gold image",
            contentScale = ContentScale.Inside,
        )
        Text(
            color = Color.Gray,
            text = score.golds.toString(),
            style = textStyle,
        )
        Image(
            painterResource(R.drawable.ruby),
            contentDescription = "ruby image",
            contentScale = ContentScale.Inside,
            modifier = Modifier.padding(start = 20.dp)
        )
        Text(
            color = Color.Gray,
            text = score.rubies.toString(),
            style = textStyle,
        )
        Image(
            painterResource(R.drawable.diamond),
            contentDescription = "diamond image",
            contentScale = ContentScale.Inside,
            modifier = Modifier.padding(start = 20.dp)
        )
        Text(
            color = Color.Gray,
            text = score.diamonds.toString(),
            style = textStyle
        )
    }
}

//@Composable
//@Preview(showBackground = true)
//fun ScoresPreview() {
//    Scores(score = TreasuresProgress())
//}