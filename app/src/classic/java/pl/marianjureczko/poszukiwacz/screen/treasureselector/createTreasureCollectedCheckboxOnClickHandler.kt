package pl.marianjureczko.poszukiwacz.screen.treasureselector

import pl.marianjureczko.poszukiwacz.activity.searching.n.SelectorSharedViewModel
import pl.marianjureczko.poszukiwacz.model.TreasureDescription

fun createTreasureCollectedCheckboxOnClickHandler(
    treasure: TreasureDescription,
    selectorSharedViewModel: SelectorSharedViewModel
): () -> Unit {
    return { selectorSharedViewModel.toggleTreasureDescriptionCollected(treasure.id) }
}