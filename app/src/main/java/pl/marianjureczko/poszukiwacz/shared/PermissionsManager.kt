package pl.marianjureczko.poszukiwacz.shared

import android.Manifest
import android.Manifest.permission.*
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat

class PermissionsManager(private val activity: Activity) {
    companion object {
        private const val REQUEST_BLUETOOTH_PERMISSION = 201
        private const val REQUEST_ACCESS_BACKGROUND_LOCATION = 202
        private const val REQUEST_BLUETOOTH_CONNECT_PERMISSION = 203
    }

    fun requestBluetoothPermissions() {
        if (!bluetoothGranted()) {
            ActivityCompat.requestPermissions(activity, arrayOf(BLUETOOTH), REQUEST_BLUETOOTH_PERMISSION)
        }
        if(!bluetoothConnectGranted()) {
            ActivityCompat.requestPermissions(activity, arrayOf(BLUETOOTH_CONNECT), REQUEST_BLUETOOTH_CONNECT_PERMISSION)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (!checkPermissionIsGranted(ACCESS_BACKGROUND_LOCATION)) {
                ActivityCompat.requestPermissions(activity, arrayOf(ACCESS_BACKGROUND_LOCATION), REQUEST_ACCESS_BACKGROUND_LOCATION)
            }
        }
    }

    fun bluetoothGranted(): Boolean = checkPermissionIsGranted(BLUETOOTH)

    fun bluetoothConnectGranted(): Boolean = checkPermissionIsGranted(BLUETOOTH_CONNECT)

    private fun checkPermissionIsGranted(permission: String) =
        ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
}