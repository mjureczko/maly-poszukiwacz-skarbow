//package pl.marianjureczko.poszukiwacz.activity.photo
//
//import android.content.Context
//import android.content.Intent
//import androidx.activity.result.contract.ActivityResultContract
//import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
//import java.io.Serializable
//
//data class PhotoInputData(
//    val photo: String,
//    val progress: TreasuresProgress
//) : Serializable
//
//
//class PhotoContract : ActivityResultContract<PhotoInputData, Void?>() {
//
//    override fun createIntent(context: Context, input: PhotoInputData): Intent {
//        return Intent(context, PhotoActivity::class.java).apply {
//            putExtra(PhotoActivity.INPUT, input)
//        }
//    }
//
//    override fun parseResult(resultCode: Int, intent: Intent?): Void? = null
//}