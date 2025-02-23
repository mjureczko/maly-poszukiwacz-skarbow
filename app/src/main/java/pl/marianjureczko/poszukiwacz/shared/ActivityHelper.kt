package pl.marianjureczko.poszukiwacz.shared

import android.media.AudioManager
import android.media.ToneGenerator

fun errorTone() {
    ToneGenerator(AudioManager.STREAM_NOTIFICATION, 90).startTone(ToneGenerator.TONE_PROP_BEEP)
}