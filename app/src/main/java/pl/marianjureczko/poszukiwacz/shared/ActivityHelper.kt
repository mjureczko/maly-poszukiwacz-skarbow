package pl.marianjureczko.poszukiwacz.shared

import androidx.appcompat.app.ActionBar
import pl.marianjureczko.poszukiwacz.R

fun addIconToActionBar(actionBar: ActionBar?) {
    actionBar?.setDisplayShowHomeEnabled(true)
    actionBar?.setIcon(R.drawable.chest_very_small)
}