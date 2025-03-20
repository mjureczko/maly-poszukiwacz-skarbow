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
import pl.marianjureczko.poszukiwacz.screen.searching.SelectorSharedViewModel

const val TREASURE_COLLECTED_CHECKBOX = "Treasure collected"
const val TREASURE_NOT_COLLECTED_CHECKBOX = "Treasure not collected"

@Composable
fun TreasureCollectedCheckbox(
    state: SelectorSharedState,
    localState: SelectorState,
    treasure: TreasureDescription,
    selectorSharedViewModel: SelectorSharedViewModel
) {
    val (imageId, description) = if (treasure.id != localState.justFoundTreasureId && state.isTreasureCollected(treasure.id)) {
        R.drawable.checkbox_checked to TREASURE_COLLECTED_CHECKBOX
    } else {
        R.drawable.checkbox_empty to TREASURE_NOT_COLLECTED_CHECKBOX
    }
    Image(
        painterResource(imageId),
        modifier = Modifier
            .padding(2.dp)
            .height(40.dp)
            .clickable(onClick = createTreasureCollectedCheckboxOnClickHandler(treasure, selectorSharedViewModel)),
        contentDescription = description,
        contentScale = ContentScale.Inside,
    )
}