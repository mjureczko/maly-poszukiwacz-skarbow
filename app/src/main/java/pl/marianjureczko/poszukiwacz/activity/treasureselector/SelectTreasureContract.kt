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

data class SelectTreasureOutputData(
    val progress: TreasureBag,
    val selectedTreasureId: Int?
)

class SelectTreasureContract : ActivityResultContract<SelectTreasureInputData, SelectTreasureOutputData>() {
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

    override fun parseResult(resultCode: Int, result: Intent?): SelectTreasureOutputData? {
        if (resultCode != Activity.RESULT_OK) {
            return null
        }
        val id = result?.getIntExtra(TreasureSelectorActivity.RESULT_SELECTED, TreasureSelectorActivity.NON_SELECTED)
        val progressAsXml = result?.getStringExtra(TreasureSelectorActivity.RESULT_PROGRESS)!!
        val progress = xmlHelper.loadFromString<TreasureBag>(progressAsXml)
        return SelectTreasureOutputData(progress, id)
    }
}