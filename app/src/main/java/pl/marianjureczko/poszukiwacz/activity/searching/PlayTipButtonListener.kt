package pl.marianjureczko.poszukiwacz.activity.searching

import android.media.MediaPlayer
import android.util.Log
import android.view.View

interface TipNameProvider {
    fun tipName(): String?
}

class PlayTipButtonListener(private val tipNameProvider: TipNameProvider) : View.OnClickListener {
    private val TAG = javaClass.simpleName

    override fun onClick(v: View?) {
        MediaPlayer().apply {
            try {
                tipNameProvider.tipName()?.let {
                    setDataSource(it)
                    prepare()
                    start()
                    Thread.sleep(3000)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Cannot play the treasure tip.")
            }
        }
    }
}