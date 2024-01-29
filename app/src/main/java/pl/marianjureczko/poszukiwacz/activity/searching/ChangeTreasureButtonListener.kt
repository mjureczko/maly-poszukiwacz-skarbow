package pl.marianjureczko.poszukiwacz.activity.searching

import android.view.View
import androidx.activity.result.ActivityResultLauncher
import pl.marianjureczko.poszukiwacz.activity.treasureselector.SelectTreasureInputData
import pl.marianjureczko.poszukiwacz.model.Treasure
import pl.marianjureczko.poszukiwacz.model.TreasureDescription

interface TreasuresStorage {
    fun getTreasureSelectorActivityInputData(justFoundTreasureDesc: TreasureDescription?): SelectTreasureInputData
}

class ChangeTreasureButtonListener (
    private val activityLauncher: ActivityResultLauncher<SelectTreasureInputData>,
    private val treasures: TreasuresStorage
) : View.OnClickListener {

    override fun onClick(v: View?) {
        activityLauncher.launch(treasures.getTreasureSelectorActivityInputData(null))
    }
}
