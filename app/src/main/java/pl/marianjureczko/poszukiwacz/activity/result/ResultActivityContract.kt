package pl.marianjureczko.poszukiwacz.activity.result

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class ResultActivityContract : ActivityResultContract<ResultActivityData, ResultActivityData?>() {
    override fun createIntent(context: Context, input: ResultActivityData?): Intent {
        return Intent(context, ResultActivity::class.java).apply {
            putExtra(ResultActivity.RESULT, input)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): ResultActivityData? {
        return intent?.getSerializableExtra(ResultActivity.RESULT) as ResultActivityData?
    }
}