//package pl.marianjureczko.poszukiwacz.activity.searching
//
//import android.content.Context
//import android.media.MediaPlayer
//import android.view.View
//import android.widget.Toast
//import pl.marianjureczko.poszukiwacz.R
//import pl.marianjureczko.poszukiwacz.shared.errorTone
//
//interface TipResourcesProvider {
//    fun tipName(): String?
//    fun mediaPlayer(): MediaPlayer
//}
//
//class PlayTipButtonListener(
//    private val tipResourcesProvider: TipResourcesProvider,
//    private val context: Context
//) : View.OnClickListener {
//    private val TAG = javaClass.simpleName
//
//    override fun onClick(v: View?) {
//        val player = tipResourcesProvider.mediaPlayer()
//        if (player.isPlaying) {
//            player.stop()
//        }
//        tipResourcesProvider.tipName()?.let {
//            player.reset()
//            player.setDataSource(it)
//            player.prepare()
//            player.start()
//        } ?: notifyAboutLackOfTip()
//
//    }
//
//    private fun notifyAboutLackOfTip() {
//        Toast.makeText(context, R.string.no_tip_to_play, Toast.LENGTH_SHORT).show()
//        errorTone()
//    }
//}