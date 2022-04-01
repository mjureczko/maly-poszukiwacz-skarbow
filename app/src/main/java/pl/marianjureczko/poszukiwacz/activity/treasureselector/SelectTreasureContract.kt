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
    val selectedTreasure: TreasureDescription?,
    val location: Location?){

}

class SelectTreasureContract : ActivityResultContract<SelectTreasureInputData, Int?>() {
    companion object {
        private val xmlHelper = XmlHelper()
    }

    override fun createIntent(context: Context, input: SelectTreasureInputData): Intent {
        return Intent(context, TreasureSelectorActivity::class.java).apply {
            putExtra(TreasureSelectorActivity.ROUTE, xmlHelper.writeToString(input.route))
            putExtra(TreasureSelectorActivity.PROGRESS, xmlHelper.writeToString(input.progress))
            val selected = input.selectedTreasure?.id ?: TreasureSelectorActivity.NON_SELECTED
            putExtra(TreasureSelectorActivity.SELECTED_TREASURE, selected)
            input.location?.let{
                putExtra(TreasureSelectorActivity.LOCATION, Coordinates(it.latitude, it.longitude))
            }
        }
    }

    override fun parseResult(resultCode: Int, result: Intent?): Int? {
        if (resultCode != Activity.RESULT_OK) {
            return null
        }
        return result?.getIntExtra(TreasureSelectorActivity.RESULT, TreasureSelectorActivity.NON_SELECTED)
    }
}