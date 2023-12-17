package pl.marianjureczko.poszukiwacz.activity.commemorative

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
import java.io.Serializable

data class CommemorativeInputData(
    val photoAbsolutePath: String,
    val progress: TreasuresProgress
) : Serializable


class CommemorativeContract : ActivityResultContract<CommemorativeInputData, Void?>() {


    override fun createIntent(context: Context, input: CommemorativeInputData): Intent {
        return Intent(context, CommemorativeActivity::class.java).apply {
            putExtra(CommemorativeActivity.INPUT, input)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Void? = null
}