package pl.marianjureczko.poszukiwacz.activity.searching

import android.view.View

interface TreasureSelectorView {
    fun showTreasureSelectionDialog()
}

class ChangeTreasureButtonListener(private val selector: TreasureSelectorView) : View.OnClickListener {
    override fun onClick(v: View?) {
        selector.showTreasureSelectionDialog()
    }
}