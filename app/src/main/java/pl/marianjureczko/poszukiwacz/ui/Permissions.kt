package pl.marianjureczko.poszukiwacz.ui

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import pl.marianjureczko.poszukiwacz.permissions.Requirements

@Composable
@OptIn(ExperimentalPermissionsApi::class)
fun handlePermission(requirements: Requirements): PermissionState {
    return handlePermissionGenerically(requirements) { permission ->
        permission.status.isGranted || !permission.status.shouldShowRationale
    }
}

@Composable
@OptIn(ExperimentalPermissionsApi::class)
fun handlePermissionWithExitOnDenied(requirements: Requirements) {
    val activity = LocalContext.current as? Activity
    handlePermissionGenerically(requirements, { activity?.finish() }) { permission ->
        !permission.status.isGranted
    }
}

@Composable
@OptIn(ExperimentalPermissionsApi::class)
private fun handlePermissionGenerically(
    requirements: Requirements,
    deniedCleanUp: () -> Unit = {},
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
        }
    val permissionState = rememberPermissionState(requirements.getPermission()!!)
    LaunchedEffect(permissionState) {
        if (launchingCondition(permissionState)) {
            requestPermissionLauncher.launch(requirements.getPermission()!!)
        }
    }
    return permissionState
}