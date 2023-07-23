package pl.marianjureczko.poszukiwacz.activity.facebook

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
import pl.marianjureczko.poszukiwacz.shared.XmlHelper
import java.io.Serializable

data class FacebookInputData(
    val progress: TreasuresProgress
) : Serializable

data class FacebookOutputData(
    val result: Boolean,
)

class FacebookContract : ActivityResultContract<FacebookInputData, FacebookOutputData?>() {

    companion object {
        private val xmlHelper = XmlHelper()
    }

    override fun createIntent(context: Context, input: FacebookInputData): Intent {
        return Intent(context, FacebookActivity::class.java).apply {
            putExtra(FacebookActivity.TREASURE_PROGRESS, xmlHelper.writeToString(input.progress))
        }
    }

    override fun parseResult(resultCode: Int, result: Intent?): FacebookOutputData? =
        if (resultCode == Activity.RESULT_OK) {
            FacebookOutputData(true)
        } else {
            FacebookOutputData(false)
        }


}