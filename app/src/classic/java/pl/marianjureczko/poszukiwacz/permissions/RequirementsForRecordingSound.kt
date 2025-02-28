package pl.marianjureczko.poszukiwacz.permissions

import android.Manifest
import pl.marianjureczko.poszukiwacz.R

object RequirementsForRecordingSound: Requirements {
    override fun getPermission(): String = Manifest.permission.RECORD_AUDIO
    override fun getMessage(): Int = R.string.missing_photo_and_audio_permission
    override fun shouldRequestOnThiDevice(): Boolean = true
}