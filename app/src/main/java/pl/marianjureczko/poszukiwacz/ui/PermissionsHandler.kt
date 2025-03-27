package pl.marianjureczko.poszukiwacz.ui

import android.content.Context
import pl.marianjureczko.poszukiwacz.permissions.Requirements

data class PermissionsHandler(private val permissionsToRequest: List<Requirements>) {

    fun requestNextPermission(previous: Requirements): Int {
        return permissionsToRequest.indexOf(previous) + 1
    }

    fun getPermissionRequirements(permissionToRequestIndex: Int): Requirements? {
        return if (permissionToRequestIndex < permissionsToRequest.size) {
            permissionsToRequest[permissionToRequestIndex]
        } else {
            null
        }
    }

    fun allPermissionsGranted(context: Context): Boolean {
        return permissionsToRequest.all { isPermissionGranted(it, context) }
    }
}