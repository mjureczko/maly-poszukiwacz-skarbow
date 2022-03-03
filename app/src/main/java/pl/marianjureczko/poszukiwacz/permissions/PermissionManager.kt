package pl.marianjureczko.poszukiwacz.permissions

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import pl.marianjureczko.poszukiwacz.R

class PermissionManager(private val permissionListener: PermissionListener) {

    companion object {

        fun areAllPermissionsGranted(context: Context, permissionWithCodes: Array<out PermissionWithCode>): Boolean {
            return permissionWithCodes.all { permissionRequest ->
                permissionRequest.getPermissionTextArray().all { permission ->
                    val permissionStatus = ContextCompat.checkSelfPermission(context, permission)
                    permissionStatus == PackageManager.PERMISSION_GRANTED
                }
            }
        }

        fun isPermissionGranted(context: Context, permissionWithCode: PermissionWithCode): Boolean {
            return permissionWithCode.getPermissionTextArray().all { permission ->
                val permissionStatus = ContextCompat.checkSelfPermission(context, permission)
                permissionStatus == PackageManager.PERMISSION_GRANTED
            }
        }
    }

    fun requestAllPermissions(activity: PermissionActivity, permissionsSpec: PermissionsSpec) {
        val permissions = permissionsSpec.getPermissionRequestsArray()
            .flatMap { it.getPermissionTextArray().asSequence() }
            .toTypedArray()
        val requestCode = permissionsSpec.getPermissionRequestsArray().sumOf { it.request }
        ActivityCompat.requestPermissions(activity, permissions, requestCode)
    }

    /**
     * This method is called when [Activity.onRequestPermissionsResult] is invoked to handle the result of
     * permission. It invokes [PermissionListener.permissionsGranted] method if all permissions are
     * granted. If user denies any of permissions, it shows dialog.
     */
    fun handleRequestPermissionsResult(activity: Activity, permissionsSpec: PermissionsSpec, permissions: Array<out String>, grantResults: IntArray) {
        val deniedPermissions = ArrayList<String>()
        var areAllPermissionsGranted = true
        grantResults.indices.forEach { i ->
            val grantResult = grantResults[i]
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                areAllPermissionsGranted = false
                deniedPermissions.add(permissions[i])
            }
        }

        if (areAllPermissionsGranted) {
            permissionListener.permissionsGranted(permissionsSpec)
        } else {
            val shouldShowRationale = deniedPermissions.any { deniedPermission ->
                ActivityCompat.shouldShowRequestPermissionRationale(activity, deniedPermission)
            }

            if (shouldShowRationale) {
                showPermissionRationaleDialog(activity, permissionsSpec)
            } else {
                showPermissionPermanentDenialDialog(activity, permissionsSpec, deniedPermissions)
            }
        }
    }

    /**
     * When user comes from the settings menu of the application, it shows a dialog to try again to
     * continue process. If user taps on yes, it invokes [PermissionListener.retry]. If user taps on no, it invokes [PermissionListener.deny]
     */
    fun handleResume(activity: Activity) {
        if (!activity.isFinishing) {
            AlertDialog.Builder(activity)
                .setTitle(R.string.retry)
                .setMessage(R.string.retry_details)
                .setCancelable(false)
                .setPositiveButton(R.string.yes) { _, _ -> permissionListener.retry() }
                .setNegativeButton(R.string.no) { dialog, _ ->
                    if (!activity.isFinishing) {
                        dialog.dismiss()
                        permissionListener.deny()
                    }
                }
                .show()
        }
    }

    private fun showPermissionRationaleDialog(activity: Activity, permissionsSpec: PermissionsSpec) {
        if (!activity.isFinishing) {
            AlertDialog.Builder(activity)
                .setTitle(R.string.permission_retry_dialog_title)
                .setMessage(permissionsSpec.getMessage())
                .setCancelable(false)
                .setPositiveButton(R.string.retry) { _, _ -> permissionListener.retry() }
                .setNegativeButton(R.string.cancel) { dialog, _ ->
                    if (!activity.isFinishing) {
                        dialog.dismiss()
                        permissionListener.deny()
                    }
                }
                .show()
        }
    }

    private fun showPermissionPermanentDenialDialog(activity: Activity, permissionsSpec: PermissionsSpec, deniedPermissions: ArrayList<String>) {
        if (!activity.isFinishing) {
            val message = permissionsSpec.getMessageForPermanentDenial()

            AlertDialog.Builder(activity)
                .setTitle(R.string.permission_retry_dialog_title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(R.string.settings) { _, _ -> permissionListener.navigateToSettings() }
                .setNegativeButton(R.string.cancel) { dialog, _ ->
                    if (!activity.isFinishing) {
                        dialog.dismiss()
                        permissionListener.deny()
                    }
                }
                .show()
        }
    }
}