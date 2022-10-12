package pl.marianjureczko.poszukiwacz.activity.treasureselector

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.Treasure
import pl.marianjureczko.poszukiwacz.model.TreasureBag
import pl.marianjureczko.poszukiwacz.shared.XmlHelper
import java.io.Serializable

data class SelectTreasureInputData(
    val route: Route,
    val progress: TreasureBag,
    val currentCoordinates: Coordinates?,
    val justFoundTreasure: Treasure?
) : Serializable

data class SelectTreasureOutputData(
    val progress: TreasureBag,
)

class SelectTreasureContract : ActivityResultContract<SelectTreasureInputData, SelectTreasureOutputData?>() {
    companion object {
        private val xmlHelper = XmlHelper()
    }

    override fun createIntent(context: Context, input: SelectTreasureInputData): Intent {
        return Intent(context, TreasureSelectorActivity::class.java).apply {
            input.justFoundTreasure?.let {
                putExtra(TreasureSelectorActivity.TREASURE, input.justFoundTreasure)
            }
            putExtra(TreasureSelectorActivity.ROUTE, xmlHelper.writeToString(input.route))
            putExtra(TreasureSelectorActivity.PROGRESS, xmlHelper.writeToString(input.progress))
            input.currentCoordinates?.let {
                putExtra(TreasureSelectorActivity.LOCATION, it)
            }
        }
    }

    override fun parseResult(resultCode: Int, result: Intent?): SelectTreasureOutputData? {
        if (resultCode == Activity.RESULT_OK || resultCode == Activity.RESULT_CANCELED) {
            result?.getStringExtra(TreasureSelectorActivity.RESULT_PROGRESS)?.let {
                return SelectTreasureOutputData(xmlHelper.loadFromString<TreasureBag>(it))
            }
        }
        return null
    }
}