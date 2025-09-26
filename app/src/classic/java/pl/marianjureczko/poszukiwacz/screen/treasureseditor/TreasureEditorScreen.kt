package pl.marianjureczko.poszukiwacz.screen.treasureseditor

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.permissions.RequirementsForDoingTipPhoto
import pl.marianjureczko.poszukiwacz.permissions.RequirementsForRecordingSound
import pl.marianjureczko.poszukiwacz.shared.GoToFacebook
import pl.marianjureczko.poszukiwacz.shared.GoToGuide
import pl.marianjureczko.poszukiwacz.ui.components.MenuConfig
import pl.marianjureczko.poszukiwacz.ui.components.TopBar
import pl.marianjureczko.poszukiwacz.ui.handlePermission
import pl.marianjureczko.poszukiwacz.ui.isPermissionGranted

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

    val toRequest = viewModel.getNextPermissionRequest()
    if (toRequest != null) {
        handlePermission(toRequest) {
            viewModel.requestNextPermission(toRequest)
        }
    }
    val cameraPermission = isPermissionGranted(RequirementsForDoingTipPhoto, LocalContext.current)
    val microphonePermission = isPermissionGranted(RequirementsForRecordingSound, LocalContext.current)
    Scaffold(
        topBar = {
            TopBar(
                navController = navController,
                title = "${stringResource(id = R.string.route)} ${state.route.name}",
                menuConfig = MenuConfig(onClickOnGuide),
            )
        },
        content = { paddingValues ->
            TreasureEditorScreenBody(
                state = state,
                cameraPermissionGranted = cameraPermission,
                recordingPermissionGranted = microphonePermission,
                hideOverrideSoundTipDialog = { viewModel.hideOverrideSoundTipDialog() },
                hideOverridePhotoDialog = { viewModel.hideOverridePhotoDialog() },
                showOverridePhotoDialog = { td: TreasureDescription -> viewModel.showOverridePhotoDialog(td) },
                showOverrideSoundTipDialog = { td: TreasureDescription -> viewModel.showOverrideSoundTipDialog(td) },
                showSoundRecordingDialog = { td: TreasureDescription -> viewModel.showSoundRecordingDialog(td) },
                hideSoundRecordingDialog = { viewModel.hideSoundRecordingDialog() },
                addTreasure = { viewModel.addTreasure() },
                removeTreasure = { id -> viewModel.removeTreasure(id) },
                getDoTipPhoto = viewModel,
                Modifier.padding(paddingValues),
            )
        }
    )
}

