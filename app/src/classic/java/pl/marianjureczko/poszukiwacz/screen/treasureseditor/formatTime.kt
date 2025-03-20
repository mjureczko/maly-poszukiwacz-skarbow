package pl.marianjureczko.poszukiwacz.screen.treasureseditor

import android.annotation.SuppressLint

@SuppressLint("DefaultLocale")
fun formatTime(ms:Long?): String {
    if (ms == null) {
        return "00:00" // Handle null input
    }

    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60

    return String.format("%02d:%02d", minutes, seconds)
}