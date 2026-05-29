package pl.marianjureczko.poszukiwacz.screen.treasureselector

import androidx.compose.ui.Modifier
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.screen.searching.SelectorSharedViewModel

/**
 * In the custom version user cannot change the "is collected" status nd the checkbox should be transparent for clicks.
 */
fun Modifier.treasureCollectedClickable(
    treasure: TreasureDescription,
    selectorSharedViewModel: SelectorSharedViewModel
): Modifier = this
