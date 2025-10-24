package pl.marianjureczko.poszukiwacz.screen.searching

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
import pl.marianjureczko.poszukiwacz.ui.Screen.dh
import pl.marianjureczko.poszukiwacz.ui.components.Score

@Composable
fun Scores(modifier: Modifier = Modifier, score: TreasuresProgress) {
    Row(
        modifier = modifier
            .padding(10.dp)
            .fillMaxWidth()
            .background(Color.Transparent)
            .height(0.05.dh),
    ) {
        Score(score.golds, R.drawable.gold, "gold image")
        Score(score.rubies, R.drawable.ruby, "ruby image")
        Score(score.diamonds, R.drawable.diamond, "diamond image")
    }
}
