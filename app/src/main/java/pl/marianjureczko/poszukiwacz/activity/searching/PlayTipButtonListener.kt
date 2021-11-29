package pl.marianjureczko.poszukiwacz.activity.searching

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import android.view.View
import android.widget.Toast
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.shared.errorTone

interface TipNameProvider {
    fun tipName(): String?
}

class PlayTipButtonListener(
    private val tipNameProvider: TipNameProvider,
    private val context: Context
) : View.OnClickListener {
    private val TAG = javaClass.simpleName

    override fun onClick(v: View?) {
        MediaPlayer().apply {
            try {
                tipNameProvider.tipName()?.let {
                    setDataSource(it)
                    prepare()
                    start()
                    Thread.sleep(3000)
                } ?: notifyAboutLackOfTip()
            } catch (e: Exception) {
                Log.e(TAG, "Cannot play the treasure tip.")
            }
        }
    }

    private fun notifyAboutLackOfTip() {
        Toast.makeText(context, R.string.no_tip_to_play, Toast.LENGTH_SHORT).show()
        errorTone()
    }
}