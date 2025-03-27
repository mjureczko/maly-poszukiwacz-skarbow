package pl.marianjureczko.poszukiwacz.screen.searching

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
import pl.marianjureczko.poszukiwacz.ui.Screen.dh

@Composable
fun Scores(modifier: Modifier = Modifier, score: TreasuresProgress) {
    Row(
        modifier = modifier
            .padding(10.dp)
            .fillMaxWidth()
            .background(Color.Transparent)
            .height(0.05.dh),
    ) {
        Image(
            painterResource(R.drawable.gold),
            contentDescription = "gold image",
            contentScale = ContentScale.Inside,
        )
        Text(
            color = Color.Gray,
            text = score.golds.toString(),
            fontSize = pl.marianjureczko.poszukiwacz.ui.theme.Typography.h5.fontSize
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
            fontSize = pl.marianjureczko.poszukiwacz.ui.theme.Typography.h5.fontSize
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
            fontSize = pl.marianjureczko.poszukiwacz.ui.theme.Typography.h5.fontSize
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ScoresPreview() {
    Scores(score = TreasuresProgress())
}