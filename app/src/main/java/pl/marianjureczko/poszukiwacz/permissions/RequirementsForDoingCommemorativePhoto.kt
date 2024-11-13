package pl.marianjureczko.poszukiwacz.permissions

import android.Manifest
import pl.marianjureczko.poszukiwacz.R

object RequirementsForDoingCommemorativePhoto : Requirements {
    override fun getPermission(): String = Manifest.permission.CAMERA
    override fun getMessage(): Int = R.string.missing_commemorative_photo_permission
}
