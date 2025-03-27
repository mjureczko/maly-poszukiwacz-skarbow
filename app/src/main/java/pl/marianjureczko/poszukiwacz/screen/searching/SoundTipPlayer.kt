package pl.marianjureczko.poszukiwacz.screen.searching

import android.media.MediaPlayer

object SoundTipPlayer {
    fun playSound(player: MediaPlayer, tipName: String) {
        if (player.isPlaying) {
            player.stop()
        }
        player.reset()
        player.setDataSource(tipName)
        player.prepare()
        player.start()
    }
}