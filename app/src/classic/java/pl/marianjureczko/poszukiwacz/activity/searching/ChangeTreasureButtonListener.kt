//package pl.marianjureczko.poszukiwacz.activity.searching
//
//import android.view.View
//import androidx.activity.result.ActivityResultLauncher
//import pl.marianjureczko.poszukiwacz.model.TreasureDescription
//
//interface TreasuresStorage {
//    fun getTreasureSelectorInputData(treasureCollected: Boolean, justFoundTreasureDesc: TreasureDescription?): pl.marianjureczko.poszukiwacz.activity.treasureselector.SelectTreasureInputData
//}
//
//class ChangeTreasureButtonListener (
//    private val activityLauncher: ActivityResultLauncher<pl.marianjureczko.poszukiwacz.activity.treasureselector.SelectTreasureInputData>,
//    private val treasures: TreasuresStorage
//) : View.OnClickListener {
//
//    override fun onClick(v: View?) {
//        activityLauncher.launch(treasures.getTreasureSelectorInputData(false, null))
//    }
//}
