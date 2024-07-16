//package pl.marianjureczko.poszukiwacz.activity.searching
//
//import android.view.View
//import android.widget.Toast
//import androidx.activity.ComponentActivity
//import androidx.activity.result.ActivityResultLauncher
//import pl.marianjureczko.poszukiwacz.R
//import pl.marianjureczko.poszukiwacz.shared.errorTone
//
//class PhotoButtonListener(
//    private val context: ComponentActivity,
//    private val data: DataStorageWrapper
//) : View.OnClickListener {
//
//    private val photoLauncher: ActivityResultLauncher<pl.marianjureczko.poszukiwacz.activity.photo.PhotoInputData> = context.registerForActivityResult(
//        pl.marianjureczko.poszukiwacz.activity.photo.PhotoContract()
//    ) {}
//
//    override fun onClick(v: View?) {
//        if (data.getSelectedForHuntTreasure() != null && data.getSelectedForHuntTreasure()!!.hasPhoto()) {
//            photoLauncher.launch(
//                pl.marianjureczko.poszukiwacz.activity.photo.PhotoInputData(
//                    data.getSelectedForHuntTreasure()!!.photoFileName!!,
//                    data.getTreasuresProgress()
//                )
//            )
//        } else {
//            Toast.makeText(context, R.string.no_photo_to_show, Toast.LENGTH_SHORT).show()
//            errorTone()
//        }
//    }
//}