package pl.marianjureczko.poszukiwacz.screen.treasureselector

import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.screen.searching.SelectorSharedViewModel

fun createTreasureCollectedCheckboxOnClickHandler(
    treasure: TreasureDescription,
    selectorSharedViewModel: SelectorSharedViewModel
): () -> Unit {
    return { selectorSharedViewModel.toggleTreasureDescriptionCollected(treasure.id) }
}