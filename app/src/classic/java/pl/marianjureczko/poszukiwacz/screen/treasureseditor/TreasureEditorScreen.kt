package pl.marianjureczko.poszukiwacz.screen.treasureseditor

import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.permissions.RequirementsForDoingTipPhoto
import pl.marianjureczko.poszukiwacz.permissions.RequirementsForRecordingSound
import pl.marianjureczko.poszukiwacz.shared.GoToFacebook
import pl.marianjureczko.poszukiwacz.shared.GoToGuide
import pl.marianjureczko.poszukiwacz.ui.canAskForNextPermission
import pl.marianjureczko.poszukiwacz.ui.components.TopBar
import pl.marianjureczko.poszukiwacz.ui.handlePermission

@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TreasureEditorScreen(
    navController: NavController,
    onClickOnGuide: GoToGuide,
    onClickOnFacebook: GoToFacebook
) {
    val viewModel: TreasureEditorViewModel = hiltViewModel()
    val state = viewModel.state.value
    val cameraPermission: PermissionState = handlePermission(RequirementsForDoingTipPhoto)
    var microphonePermission: PermissionState? = null
    if (canAskForNextPermission(cameraPermission)) {
        microphonePermission = handlePermission(RequirementsForRecordingSound)
    }
    Scaffold(
        topBar = {
            TopBar(
                navController,
                "${stringResource(id = R.string.route)} ${state.route.name}",
                onClickOnGuide,
                onClickOnFacebook
            )
        },
        content = { _ ->
            TreasureEditorScreenBody(
                state,
                cameraPermission.status.isGranted,
                microphonePermission?.status?.isGranted ?: false,
                { viewModel.hideOverridePhotoDialog() },
                { viewModel.showOverridePhotoDialog() },
                { viewModel.hideOverrideSoundTipDialog() },
                { viewModel.showOverrideSoundTipDialog() },
                { td -> viewModel.showSoundRecordingDialog(td) },
                { viewModel.hideSoundRecordingDialog() },
                { viewModel.addTreasure() },
                { id -> viewModel.removeTreasure(id) },
                viewModel
            )
        }
    )
}

