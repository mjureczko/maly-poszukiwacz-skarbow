package pl.marianjureczko.poszukiwacz.screen.treasureselector

import androidx.compose.foundation.clickable
import androidx.compose.ui.Modifier
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.screen.searching.SelectorSharedViewModel

fun Modifier.treasureCollectedClickable(
    treasure: TreasureDescription,
    selectorSharedViewModel: SelectorSharedViewModel
): Modifier =
    this.clickable(
        onClick = { selectorSharedViewModel.toggleTreasureDescriptionCollected(treasure.id) }
    )
