package pl.marianjureczko.poszukiwacz.activity.treasureselector

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Location
import androidx.activity.result.contract.ActivityResultContract
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureBag
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.shared.XmlHelper

data class SelectTreasureInputData(
    val route: Route,
    val progress: TreasureBag,
    val location: Location?
)

data class SelectTreasureOutputData(
    val progress: TreasureBag,
)

class SelectTreasureContract : ActivityResultContract<SelectTreasureInputData, SelectTreasureOutputData?>() {
    companion object {
        private val xmlHelper = XmlHelper()
    }

    override fun createIntent(context: Context, input: SelectTreasureInputData): Intent {
        return Intent(context, TreasureSelectorActivity::class.java).apply {
            putExtra(TreasureSelectorActivity.ROUTE, xmlHelper.writeToString(input.route))
            putExtra(TreasureSelectorActivity.PROGRESS, xmlHelper.writeToString(input.progress))
            input.location?.let {
                putExtra(TreasureSelectorActivity.LOCATION, Coordinates(it.latitude, it.longitude))
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