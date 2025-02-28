package pl.marianjureczko.poszukiwacz.permissions

import android.Manifest
import pl.marianjureczko.poszukiwacz.R

object RequirementsForDoingTipPhoto : Requirements {
    override fun getPermission(): String = Manifest.permission.CAMERA
    override fun getMessage(): Int = R.string.missing_tip_photo_permission
    override fun shouldRequestOnThiDevice(): Boolean = true
}