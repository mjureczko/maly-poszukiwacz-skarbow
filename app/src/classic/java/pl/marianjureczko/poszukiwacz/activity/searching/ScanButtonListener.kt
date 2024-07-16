//package pl.marianjureczko.poszukiwacz.activity.searching
//
//import android.view.View
//import androidx.activity.result.ActivityResultLauncher
//import com.journeyapps.barcodescanner.ScanOptions
//
//class ScanButtonListener(
//    private val scannerLauncher: ActivityResultLauncher<ScanOptions>,
//    private val message: String
//) : View.OnClickListener {
//
//    override fun onClick(v: View?) {
//        val scanOptions = ScanOptions()
//        scanOptions.setPrompt(message)
//        scannerLauncher.launch(scanOptions)
//    }
//}