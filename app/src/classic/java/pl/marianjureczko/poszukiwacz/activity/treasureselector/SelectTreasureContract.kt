//package pl.marianjureczko.poszukiwacz.activity.treasureselector
//
//import android.app.Activity
//import android.content.Context
//import android.content.Intent
//import androidx.activity.result.contract.ActivityResultContract
//import pl.marianjureczko.poszukiwacz.model.Route
//import pl.marianjureczko.poszukiwacz.model.TreasureDescription
//import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
//import pl.marianjureczko.poszukiwacz.shared.XmlHelper
//import java.io.Serializable
//
//data class SelectTreasureInputData(
//    val newTreasureCollected: Boolean,
//    val route: Route,
//    val progress: TreasuresProgress,
//    val currentCoordinates: pl.marianjureczko.poszukiwacz.shared.Coordinates?,
//    val justFoundTreasureDescription: TreasureDescription?
//) : Serializable
//
//data class SelectTreasureOutputData(
//    val progress: TreasuresProgress,
//)
//
//class SelectTreasureContract : ActivityResultContract<SelectTreasureInputData, SelectTreasureOutputData?>() {
//    companion object {
//        private val xmlHelper = XmlHelper()
//    }
//
//    override fun createIntent(context: Context, input: SelectTreasureInputData): Intent {
//        return Intent(context, TreasureSelectorActivity::class.java).apply {
//            input.justFoundTreasureDescription?.let {
//                putExtra(TreasureSelectorActivity.TREASURE_DESCRIPTION, input.justFoundTreasureDescription)
//            }
//            putExtra(TreasureSelectorActivity.ROUTE, xmlHelper.writeToString(input.route))
//            putExtra(TreasureSelectorActivity.PROGRESS, xmlHelper.writeToString(input.progress))
//            putExtra(TreasureSelectorActivity.NEW_TREASURE_COLLECTED, input.newTreasureCollected)
//            input.currentCoordinates?.let {
//                putExtra(TreasureSelectorActivity.LOCATION, it)
//            }
//        }
//    }
//
//    override fun parseResult(resultCode: Int, result: Intent?): SelectTreasureOutputData? {
//        if (resultCode == Activity.RESULT_OK || resultCode == Activity.RESULT_CANCELED) {
//            result?.getStringExtra(TreasureSelectorActivity.RESULT_PROGRESS)?.let {
//                return SelectTreasureOutputData(xmlHelper.loadFromString<TreasuresProgress>(it))
//            }
//        }
//        return null
//    }
//}