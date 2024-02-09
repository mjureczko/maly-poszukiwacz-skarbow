package pl.marianjureczko.poszukiwacz.activity.result

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class ResultActivityContract : ActivityResultContract<ResultActivityInput, ResultActivityOutput>() {
    override fun createIntent(context: Context, input: ResultActivityInput): Intent {
        return Intent(context, ResultActivity::class.java).apply {
            putExtra(ResultActivity.RESULT_IN, input)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): ResultActivityOutput {
        return intent?.getSerializableExtra(ResultActivity.RESULT_OUT) as ResultActivityOutput
    }
}