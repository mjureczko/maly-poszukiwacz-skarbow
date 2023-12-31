package pl.marianjureczko.poszukiwacz.permissions

import android.Manifest
import android.os.Build
import pl.marianjureczko.poszukiwacz.activity.facebook.FacebookActivity

enum class PermissionsSpec(val requestCode: Int) {
    CAMERA(1),
    MICROPHONE(2),
    LOCATION(4),
    BLUETOOTH(8),
    EXTERNAL_STORAGE(16);

    fun getPermissionsTextArray(): Array<String> {
        return when (this) {
            CAMERA -> arrayOf(Manifest.permission.CAMERA)
            MICROPHONE -> arrayOf(Manifest.permission.RECORD_AUDIO)
            LOCATION -> arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
            BLUETOOTH -> {
                val permissions = mutableListOf(Manifest.permission.BLUETOOTH)
                if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)) {
                    permissions.add(Manifest.permission.BLUETOOTH_CONNECT)
                }
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                    permissions.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
//                }
                permissions.toTypedArray()
            }
            EXTERNAL_STORAGE -> {
                val permissions = mutableListOf(Manifest.permission.BLUETOOTH)
                if (Build.VERSION.SDK_INT < FacebookActivity.STORAGE_DO_NOT_REQUIRE_PERMISSONS) {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                }
                permissions.toTypedArray()
            }
        }
    }
}