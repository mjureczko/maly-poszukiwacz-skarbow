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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
import pl.marianjureczko.poszukiwacz.ui.Screen.dh
import pl.marianjureczko.poszukiwacz.ui.dp2SameSizeSp
import pl.marianjureczko.poszukiwacz.ui.theme.FANCY_FONT

const val KNOWLEDGE_SCORE_TEXT = "Knowledge score"

@Composable
fun Scores(modifier: Modifier = Modifier, score: TreasuresProgress) {
    val scoresHeight = 0.05.dh
    val textStyle = TextStyle(
        fontFamily = FANCY_FONT,
        fontSize = dp2SameSizeSp(scoresHeight),
    )
    Row(
        modifier = modifier
            .padding(10.dp)
            .fillMaxWidth()
            .background(Color.Transparent)
            .height(scoresHeight),
    ) {
        Image(
            painterResource(R.drawable.chest_small),
            contentDescription = "tourist treasure image",
            contentScale = ContentScale.Inside,
            modifier = Modifier.padding(end = 5.dp)
        )
        Text(
            color = Color.Gray,
            text = score.knowledge.toString(),
            style = textStyle,
            modifier = Modifier
                .padding(end = 5.dp)
                .semantics { contentDescription = KNOWLEDGE_SCORE_TEXT }
        )
    }

}
