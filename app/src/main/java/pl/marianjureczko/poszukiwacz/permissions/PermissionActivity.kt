package pl.marianjureczko.poszukiwacz.permissions

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import pl.marianjureczko.poszukiwacz.shared.ActivityWithBackButton


abstract class PermissionActivity : ActivityWithBackButton() {

    private lateinit var permissionManager: PermissionManager
    private lateinit var permissionsSpec: PermissionsSpec
    private var isComingFromSettings = false
    private var exitOnDenied = false

    abstract fun onPermissionsGranted(permissionsSpec: PermissionsSpec)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val permissionListener = object : PermissionListener {

            override fun permissionsGranted(permissionsSpec: PermissionsSpec) {
                onPermissionsGranted(permissionsSpec)
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
                assurePermissionsAreGranted(permissionsSpec, exitOnDenied)
            }
        }
        permissionManager = PermissionManager(permissionListener)
    }

    /**
     * Checks whether user granted necessary permissions for [permissionsSpec]. If the user already
     * granted that permissions, it will call [onPermissionsGranted] method, otherwise it will make preparations to take permissions from user.
     * @param permissionsSpec It is actually an answer to the question 'What are you going to do with these permissions?'.
     * @param exitOnDenied An optional parameter. It will make your activity finish when user denies permissions.
     * Pass it **true** if your activity cannot live without permissions. Default is **false**
     */
    fun assurePermissionsAreGranted(permissionsSpec: PermissionsSpec, exitOnDenied: Boolean = false) {
        this.permissionsSpec = permissionsSpec
        this.exitOnDenied = exitOnDenied
        if (::permissionManager.isInitialized) {
            val permissionRequests = permissionsSpec.getPermissionRequestsArray()

            if (PermissionManager.areAllPermissionsGranted(this, permissionRequests)) {
                onPermissionsGranted(permissionsSpec)
            } else {
                permissionManager.requestAllPermissions(this, permissionsSpec)
            }
        }
    }

    open fun onPermissionsDenied() {
        if (exitOnDenied) {
            finish()
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (::permissionsSpec.isInitialized) {
            permissionManager.handleRequestPermissionsResult(this, permissionsSpec, permissions, grantResults)
        }
    }

    override fun onResume() {
        super.onResume()
        if (isComingFromSettings && ::permissionsSpec.isInitialized) {
            isComingFromSettings = false
            permissionManager.handleResume(this)
        }
    }
}