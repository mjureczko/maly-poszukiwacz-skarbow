package pl.marianjureczko.poszukiwacz.permissions

import android.Manifest

enum class PermissionWithCode(val request: Int) {
    CAMERA(1),
    MICROPHONE(2);

    fun getPermissionsTextArray(): Array<String> {
        return when (this) {
            CAMERA -> arrayOf(Manifest.permission.CAMERA)
            MICROPHONE -> arrayOf(Manifest.permission.RECORD_AUDIO)
        }
    }
}