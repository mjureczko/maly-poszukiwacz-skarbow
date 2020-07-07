package pl.marianjureczko.poszukiwacz.listener

import android.view.View
import pl.marianjureczko.poszukiwacz.activity.TreasureSelectorView

class ChangeTreasureButtonListener(private val selector: TreasureSelectorView) : View.OnClickListener {
    override fun onClick(v: View?) {
        selector.selectTreasureForSearching()
    }
}