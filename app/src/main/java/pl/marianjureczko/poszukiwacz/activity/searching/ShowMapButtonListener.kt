package pl.marianjureczko.poszukiwacz.activity.searching

import android.view.View
import androidx.activity.result.ActivityResultLauncher
import pl.marianjureczko.poszukiwacz.model.Route

class ShowMapButtonListener(
    private val activityLauncher: ActivityResultLauncher<Route>,
    private val model: SearchingActivityViewModel
) : View.OnClickListener {
    override fun onClick(v: View?) {
        activityLauncher.launch(model.getRoute())
    }
}