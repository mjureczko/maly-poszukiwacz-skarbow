package pl.marianjureczko.poszukiwacz.activity.searching

import android.view.View
import androidx.activity.result.ActivityResultLauncher
import pl.marianjureczko.poszukiwacz.activity.map.MapInputData

class ShowMapButtonListener(
    private val activityLauncher: ActivityResultLauncher<MapInputData>,
    private val model: SearchingActivityViewModel
) : View.OnClickListener {
    override fun onClick(v: View?) {
        activityLauncher.launch(MapInputData(model.getRoute(), model.getTreasuresProgress()))
    }
}