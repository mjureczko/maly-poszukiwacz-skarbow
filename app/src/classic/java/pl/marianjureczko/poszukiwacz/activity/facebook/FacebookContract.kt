//package pl.marianjureczko.poszukiwacz.activity.facebook
//
//import android.app.Activity
//import android.content.Context
//import android.content.Intent
//import androidx.activity.result.contract.ActivityResultContract
//import pl.marianjureczko.poszukiwacz.model.HunterPath
//import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
//import java.io.Serializable
//
//data class FacebookInputData(
//    val hunterPath: HunterPath?,
//    val progress: TreasuresProgress
//) : Serializable
//
//data class FacebookOutputData(
//    val result: Boolean,
//)
//
//class FacebookContract : ActivityResultContract<FacebookInputData, FacebookOutputData?>() {
//
//    override fun createIntent(context: Context, input: FacebookInputData): Intent {
//        return Intent(context, FacebookActivity::class.java).apply {
//            putExtra(FacebookActivity.INPUT, input)
//        }
//    }
//
//    override fun parseResult(resultCode: Int, result: Intent?): FacebookOutputData? =
//        if (resultCode == Activity.RESULT_OK) {
//            FacebookOutputData(true)
//        } else {
//            FacebookOutputData(false)
//        }
//
//
//}