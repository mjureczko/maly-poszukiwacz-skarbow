package pl.marianjureczko.poszukiwacz.activity.map

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import pl.marianjureczko.poszukiwacz.model.Route

class MapActivityContract : ActivityResultContract<Route, Void>() {
    override fun createIntent(context: Context, input: Route): Intent {
        return Intent(context, MapActivity::class.java).apply {
            putExtra(MapActivity.MAP, input)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Void? {
        return null
    }
}