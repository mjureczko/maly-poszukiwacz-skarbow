package pl.marianjureczko.poszukiwacz.shared

import android.media.AudioManager
import android.media.ToneGenerator
import android.util.Log

private var toneGenerator: ToneGenerator? = null

fun errorTone() {
    try {
        // Create if not yet initialized
        if (toneGenerator == null) {
            toneGenerator = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 90)
        }

        // Start tone for 200ms (you can adjust)
        toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 200)
    } catch (e: RuntimeException) {
        Log.w("ErrorTone", "Failed to initialize ToneGenerator: ${e.message}")
    }
}