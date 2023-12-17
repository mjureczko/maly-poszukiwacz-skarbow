package pl.marianjureczko.poszukiwacz.activity.map

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
import java.io.Serializable

data class MapInputData(
    val route: Route,
    val progress: TreasuresProgress
) : Serializable

class MapActivityContract : ActivityResultContract<MapInputData, Void>() {
    override fun createIntent(context: Context, input: MapInputData): Intent {
        return Intent(context, MapActivity::class.java).apply {
            putExtra(MapActivity.MAP, input)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Void? {
        return null
    }
}