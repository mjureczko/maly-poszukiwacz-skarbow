package pl.marianjureczko.poszukiwacz.activity.treasureseditor

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

class PermissionsManager(private val activity: Activity) {
    companion object {
        private const val REQUEST_ALL_PERMISSIONS = 200
        private const val RECORD_AUDIO: String = Manifest.permission.RECORD_AUDIO
        private const val CAMERA: String = Manifest.permission.CAMERA
        private val ALL_PERMISSIONS = arrayOf(
            RECORD_AUDIO,
            CAMERA
        )
    }

    fun requestPermissions() = ActivityCompat.requestPermissions(activity, ALL_PERMISSIONS, REQUEST_ALL_PERMISSIONS)

    fun recordingGranted(): Boolean = checkPermission(RECORD_AUDIO)

    fun capturingPhotoGranted(): Boolean = checkPermission(CAMERA)

    private fun checkPermission(permission: String) =
        ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
}