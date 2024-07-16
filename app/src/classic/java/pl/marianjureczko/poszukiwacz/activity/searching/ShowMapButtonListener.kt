//package pl.marianjureczko.poszukiwacz.activity.searching
//
//import android.view.View
//import androidx.activity.result.ActivityResultLauncher
//
//class ShowMapButtonListener(
//    private val activityLauncher: ActivityResultLauncher<pl.marianjureczko.poszukiwacz.activity.map.MapInputData>,
//    private val model: SearchingActivityViewModel
//) : View.OnClickListener {
//    override fun onClick(v: View?) {
//        activityLauncher.launch(
//            pl.marianjureczko.poszukiwacz.activity.map.MapInputData(
//                model.getRoute(),
//                model.getTreasuresProgress()
//            )
//        )
//    }
//}