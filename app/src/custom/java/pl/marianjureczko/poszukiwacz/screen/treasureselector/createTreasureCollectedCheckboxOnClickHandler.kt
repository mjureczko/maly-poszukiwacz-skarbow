package pl.marianjureczko.poszukiwacz.screen.treasureselector

import pl.marianjureczko.poszukiwacz.activity.searching.n.SelectorSharedViewModel
import pl.marianjureczko.poszukiwacz.model.TreasureDescription

/**
 * In the custom version user cannot change the "is collected" status.
 */
fun createTreasureCollectedCheckboxOnClickHandler(
    treasure: TreasureDescription,
    selectorSharedViewModel: SelectorSharedViewModel
): () -> Unit = {}