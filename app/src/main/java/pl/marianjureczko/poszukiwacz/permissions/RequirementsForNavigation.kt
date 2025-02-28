package pl.marianjureczko.poszukiwacz.permissions

import android.Manifest
import pl.marianjureczko.poszukiwacz.R

object RequirementsForNavigation : Requirements {
    override fun getPermission(): String = Manifest.permission.ACCESS_FINE_LOCATION
    override fun getMessage(): Int = R.string.missing_location_permission
    override fun shouldRequestOnThiDevice(): Boolean = true
}