package pl.marianjureczko.poszukiwacz.permissions

import android.Manifest
import android.os.Build
import pl.marianjureczko.poszukiwacz.R

object RequirementsToExternalStorage : Requirements {
    override fun getPermission(): String = Manifest.permission.WRITE_EXTERNAL_STORAGE
    override fun getMessage(): Int = R.string.missing_external_storage_permission
    override fun shouldRequestOnThiDevice(): Boolean = Build.VERSION.SDK_INT < Build.VERSION_CODES.Q
}