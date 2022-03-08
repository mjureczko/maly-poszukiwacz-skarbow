package pl.marianjureczko.poszukiwacz.permissions

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import pl.marianjureczko.poszukiwacz.R

class PermissionManager(private val permissionListener: PermissionListener) {

    companion object {

        fun areAllPermissionsGranted(context: Context, permissions: Array<out PermissionsSpec>): Boolean =
            permissions.all { permissions -> isPermissionGranted(context, permissions) }

        fun isPermissionGranted(context: Context, permissions: PermissionsSpec): Boolean {
            return permissions.getPermissionsTextArray().all { permission ->
                val permissionStatus = ContextCompat.checkSelfPermission(context, permission)
                permissionStatus == PackageManager.PERMISSION_GRANTED
            }
        }
    }

    fun requestAllPermissions(activity: PermissionActivity, activityRequirements: ActivityRequirements) {
        val permissions = activityRequirements.getPermissionsArray()
            .flatMap { it.getPermissionsTextArray().asSequence() }
            .toTypedArray()
        val requestCode = activityRequirements.getPermissionsArray().sumOf { it.request }
        ActivityCompat.requestPermissions(activity, permissions, requestCode)
    }

    /**
     * This method is called when [Activity.onRequestPermissionsResult] is invoked to handle the result of
     * permission. It invokes [PermissionListener.permissionsGranted] method if all permissions are
     * granted. If user denies any of permissions, it shows dialog.
     */
    fun handleRequestPermissionsResult(activity: Activity, activityRequirements: ActivityRequirements, permissions: Array<out String>, grantResults: IntArray) {
        val deniedPermissions = getDeniedPermissions(grantResults, permissions)
        if (deniedPermissions.isEmpty()) {
            permissionListener.permissionsGranted(activityRequirements)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && areAllPermissionsGranted(activity, activityRequirements.getPermissionsArray())) {
                permissionListener.permissionsGranted(activityRequirements)
            } else {
                if (shouldShowRationale(activity, deniedPermissions)) {
                    showPermissionRationaleDialog(activity, activityRequirements)
                } else {
                    showPermissionPermanentDenialDialog(activity, activityRequirements, deniedPermissions)
                }
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

    private fun shouldShowRationale(activity: Activity, deniedPermissions: ArrayList<String>): Boolean =
        deniedPermissions.any { deniedPermission ->
            ActivityCompat.shouldShowRequestPermissionRationale(activity, deniedPermission)
        }

    private fun getDeniedPermissions(grantResults: IntArray, permissions: Array<out String>): ArrayList<String> {
        val deniedPermissions = ArrayList<String>()
        grantResults.indices.forEach { i ->
            val grantResult = grantResults[i]
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                deniedPermissions.add(permissions[i])
            }
        }
        return deniedPermissions
    }

    private fun showPermissionRationaleDialog(activity: Activity, activityRequirements: ActivityRequirements) {
        if (!activity.isFinishing) {
            AlertDialog.Builder(activity)
                .setTitle(R.string.permission_retry_dialog_title)
                .setMessage(activityRequirements.getMessage())
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

    private fun showPermissionPermanentDenialDialog(activity: Activity, activityRequirements: ActivityRequirements, deniedPermissions: ArrayList<String>) {
        if (!activity.isFinishing) {
            val message = activityRequirements.getMessageForPermanentDenial()

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
