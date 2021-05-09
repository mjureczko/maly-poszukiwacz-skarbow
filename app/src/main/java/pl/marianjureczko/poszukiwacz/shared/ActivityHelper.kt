package pl.marianjureczko.poszukiwacz.shared

import android.media.AudioManager
import android.media.ToneGenerator
import androidx.appcompat.app.ActionBar
import pl.marianjureczko.poszukiwacz.R

fun addIconToActionBar(actionBar: ActionBar?) {
    actionBar?.setDisplayShowHomeEnabled(true)
    actionBar?.setIcon(R.drawable.chest_very_small)
}

fun errorTone() {
    ToneGenerator(AudioManager.STREAM_NOTIFICATION, 50).startTone(ToneGenerator.TONE_PROP_BEEP)
}