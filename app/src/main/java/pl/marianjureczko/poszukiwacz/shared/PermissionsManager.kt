package pl.marianjureczko.poszukiwacz.shared

import android.Manifest
import android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
import android.Manifest.permission.BLUETOOTH
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionsManager(private val activity: Activity) {
    companion object {
        private const val REQUEST_MEDIA_PERMISSIONS = 200
        private const val REQUEST_BLUETOOTH_PERMISSION = 201
        private const val REQUEST_ACCESS_BACKGROUND_LOCATION = 202
        private const val RECORD_AUDIO: String = Manifest.permission.RECORD_AUDIO
        private const val CAMERA: String = Manifest.permission.CAMERA
        private val MEDIA_PERMISSIONS = arrayOf(RECORD_AUDIO, CAMERA)
    }

    fun requestPermissions() = ActivityCompat.requestPermissions(activity, MEDIA_PERMISSIONS, REQUEST_MEDIA_PERMISSIONS)

    fun requestBluetoothPermission() {
        if (ContextCompat.checkSelfPermission(activity, BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(BLUETOOTH), REQUEST_BLUETOOTH_PERMISSION)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(activity, ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, arrayOf(ACCESS_BACKGROUND_LOCATION), REQUEST_ACCESS_BACKGROUND_LOCATION)
            }
        }
    }

    fun recordingGranted(): Boolean = checkPermission(RECORD_AUDIO)

    fun capturingPhotoGranted(): Boolean = checkPermission(CAMERA)

    fun bluetoothGranted(): Boolean = checkPermission(BLUETOOTH)

    private fun checkPermission(permission: String) =
        ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
}