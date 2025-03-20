package pl.marianjureczko.poszukiwacz.screen.treasureselector

import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.screen.searching.SelectorSharedViewModel

/**
 * In the custom version user cannot change the "is collected" status.
 */
fun createTreasureCollectedCheckboxOnClickHandler(
    treasure: TreasureDescription,
    selectorSharedViewModel: SelectorSharedViewModel
): () -> Unit = {}