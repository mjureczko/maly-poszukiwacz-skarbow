package pl.marianjureczko.poszukiwacz.screen.treasureselector

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.screen.searching.SelectorSharedState
import pl.marianjureczko.poszukiwacz.shared.GoToResultWithTreasure

@Composable
fun ShowMovieButton(state: SelectorSharedState, treasure: TreasureDescription, goToResult: GoToResultWithTreasure) {
    if (state.isTreasureCollected(treasure.id)) {
        Image(
            painterResource(R.drawable.movie),
            modifier = Modifier
                .padding(2.dp)
                .height(35.dp)
                .clickable { goToResult(treasure.id) },
            contentDescription = "Show treasure movie",
            contentScale = ContentScale.Inside,
        )
    }
}