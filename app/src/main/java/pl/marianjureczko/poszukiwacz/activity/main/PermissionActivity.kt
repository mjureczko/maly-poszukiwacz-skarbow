package pl.marianjureczko.poszukiwacz.activity.main

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import pl.marianjureczko.poszukiwacz.permissions.ActivityRequirements
import pl.marianjureczko.poszukiwacz.permissions.PermissionListener
import pl.marianjureczko.poszukiwacz.permissions.PermissionManager
import pl.marianjureczko.poszukiwacz.permissions.PermissionsSpec
import pl.marianjureczko.poszukiwacz.permissions.RequirementsForNavigation

abstract class PermissionActivity : ComponentActivity() {

    private lateinit var permissionManager: PermissionManager
    private lateinit var activityRequirements: ActivityRequirements
    private var isComingFromSettings = false
    private var exitOnDenied = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val permissionListener = object : PermissionListener {

            override fun permissionsGranted(activityRequirements: ActivityRequirements) {
                // do nothing
            }

            override fun navigateToSettings() {
                isComingFromSettings = true
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", packageName, null))
                startActivity(intent)
            }

            override fun deny() {
                onPermissionsDenied()
            }

            override fun retry() {
                assurePermissionsAreGranted(activityRequirements, exitOnDenied)
            }
        }
        permissionManager = PermissionManager(permissionListener)
        assurePermissionsAreGranted(RequirementsForNavigation, true)
        //TODO t: exitOnDenied==true and then exitOnDenied==false leads to race condition
//        assurePermissionsAreGranted(RequirementsForDoingPhoto, false)


//        assurePermissionsAreGranted(RequirementsForExternalStorage, false)
    }

    /**
     * Checks whether user granted necessary permissions for [activityRequirements]. If the user already
     * granted that permissions, it will call [onPermissionsGranted] method, otherwise it will make preparations to take permissions from user.
     * @param activityRequirements It is actually an answer to the question 'What are you going to do with these permissions?'.
     * @param exitOnDenied An optional parameter. It will make your activity finish when user denies permissions.
     * Pass it **true** if your activity cannot live without permissions. Default is **false**
     */
    fun assurePermissionsAreGranted(activityRequirements: ActivityRequirements, exitOnDenied: Boolean = false) {
        this.activityRequirements = activityRequirements
        this.exitOnDenied = exitOnDenied
        if (::permissionManager.isInitialized) {
            //val permissions: Array<PermissionsSpec = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA)
            val permissions = arrayOf(PermissionsSpec.LOCATION, PermissionsSpec.CAMERA)
//            val permissions = activityRequirements.getSpecsArray()

            if (PermissionManager.areAllPermissionsGranted(this, permissions)) {
                // do nothing
            } else {
                permissionManager.requestAllPermissions(this, activityRequirements)
            }
        }
    }

    fun onPermissionsDenied() {
        if (exitOnDenied) {
            finish()
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (::activityRequirements.isInitialized) {
            permissionManager.handleRequestPermissionsResult(this, activityRequirements, permissions, grantResults)
        }
    }

    override fun onResume() {
        super.onResume()
        if (isComingFromSettings && ::activityRequirements.isInitialized) {
            isComingFromSettings = false
            permissionManager.handleResume(this)
        }
    }
}