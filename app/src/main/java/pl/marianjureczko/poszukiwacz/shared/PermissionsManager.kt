package pl.marianjureczko.poszukiwacz.shared

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat

class PermissionsManager(private val activity: Activity) {
    companion object {
        private const val REQUEST_ALL_PERMISSIONS = 200
        private const val REQUEST_BLUETOOTH_PERMISSION = 201
        private const val RECORD_AUDIO: String = Manifest.permission.RECORD_AUDIO
        private const val BLUETOOTH = Manifest.permission.BLUETOOTH
        private const val CAMERA: String = Manifest.permission.CAMERA
        private val ALL_PERMISSIONS = arrayOf(RECORD_AUDIO, CAMERA)
    }

    fun requestPermissions() = ActivityCompat.requestPermissions(activity, ALL_PERMISSIONS, REQUEST_ALL_PERMISSIONS)

    fun requestBluetoothPermission() {
//        if (activity.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)) {
        Toast.makeText(activity, "Requesting Bluetooth permission", Toast.LENGTH_SHORT).show()
        ActivityCompat.requestPermissions(activity, arrayOf(BLUETOOTH), REQUEST_BLUETOOTH_PERMISSION)
//        }
    }

    fun recordingGranted(): Boolean = checkPermission(RECORD_AUDIO)

    fun capturingPhotoGranted(): Boolean = checkPermission(CAMERA)

    fun bluetoothGranted(): Boolean = checkPermission(BLUETOOTH)

    private fun checkPermission(permission: String) =
        ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
}