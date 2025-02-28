package pl.marianjureczko.poszukiwacz.ui

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import pl.marianjureczko.poszukiwacz.permissions.Requirements

@Composable
@OptIn(ExperimentalPermissionsApi::class)
fun handlePermissionWithExitOnDenied(requirements: Requirements) {
    val activity = LocalContext.current as? Activity
    handlePermissionGenerically(requirements, { activity?.finish() }) { permission ->
        !permission.status.isGranted
    }
}

/**
 * Android allows only one permission request to be presented to the user at a time.
 * Therefore, before requesting a new permission, the system must verify that the user has already responded
 * to the previous request.
 *
 * @param userResponsePostprocessingCallback Use to ensures that permission requests are handled sequentially,
 *          i.e. in callback for previous permission arrange requesting the next one.
 */
@Composable
@OptIn(ExperimentalPermissionsApi::class)
fun handlePermission(
    requirements: Requirements,
    userResponsePostprocessingCallback: () -> Unit = {},
): PermissionState {
    return if (requirements.shouldRequestOnThiDevice()) {
        handlePermissionGenerically(
            requirements = requirements, userResponsePostprocessingCallback = userResponsePostprocessingCallback
        ) { permission ->
            permission.status.isGranted || !permission.status.shouldShowRationale
        }
    } else {
        object : PermissionState {
            override val permission = requirements.getPermission()!!
            override val status = PermissionStatus.Granted
            override fun launchPermissionRequest() {}
        }
    }
}

fun isPermissionGranted(requirements: Requirements, context: Context): Boolean {
    if (requirements.shouldRequestOnThiDevice()) {
        val checkResult = ContextCompat.checkSelfPermission(context, requirements.getPermission()!!)
        return checkResult == android.content.pm.PackageManager.PERMISSION_GRANTED
    } else {
        return true
    }
}

@Composable
@OptIn(ExperimentalPermissionsApi::class)
private fun handlePermissionGenerically(
    requirements: Requirements,
    deniedCleanUp: () -> Unit = {},
    userResponsePostprocessingCallback: () -> Unit = {},
    launchingCondition: (PermissionState) -> Boolean
): PermissionState {
    val context = LocalContext.current
    val permissionDeniedMessage = stringResource(requirements.getMessage())
    val requestPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (!granted) {
                Toast.makeText(context, permissionDeniedMessage, Toast.LENGTH_LONG).show()
                deniedCleanUp()
            }
            userResponsePostprocessingCallback()
        }
    val permissionState = rememberPermissionState(requirements.getPermission()!!)
    LaunchedEffect(permissionState) {
        if (launchingCondition(permissionState)) {
            requestPermissionLauncher.launch(requirements.getPermission()!!)
        }
    }
    return permissionState
}
