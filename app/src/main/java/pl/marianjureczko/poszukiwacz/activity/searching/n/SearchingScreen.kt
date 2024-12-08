package pl.marianjureczko.poszukiwacz.activity.searching.n

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.activity.main.RESULTS_ROUTE
import pl.marianjureczko.poszukiwacz.activity.main.SELECTOR_ROUTE
import pl.marianjureczko.poszukiwacz.activity.result.n.NOTHING_FOUND_TREASURE_ID
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.screen.searching.Scores
import pl.marianjureczko.poszukiwacz.shared.GoToCommemorative
import pl.marianjureczko.poszukiwacz.shared.GoToFacebook
import pl.marianjureczko.poszukiwacz.shared.GoToGuide
import pl.marianjureczko.poszukiwacz.shared.GoToMap
import pl.marianjureczko.poszukiwacz.shared.GoToQrScanner
import pl.marianjureczko.poszukiwacz.shared.GoToResults
import pl.marianjureczko.poszukiwacz.shared.GoToTipPhoto
import pl.marianjureczko.poszukiwacz.shared.GoToTreasureSelector
import pl.marianjureczko.poszukiwacz.shared.errorTone
import pl.marianjureczko.poszukiwacz.ui.Screen.dh
import pl.marianjureczko.poszukiwacz.ui.Screen.dw
import pl.marianjureczko.poszukiwacz.ui.components.AdvertBanner
import pl.marianjureczko.poszukiwacz.ui.components.CommemorativePhotoButton
import pl.marianjureczko.poszukiwacz.ui.components.TopBar
import pl.marianjureczko.poszukiwacz.ui.handlePermission
import pl.marianjureczko.poszukiwacz.ui.isOnStack
import java.net.URLEncoder

const val COMPASS = "Compass"
const val STEPS_TO_TREASURE = "Steps to treasure"
const val SCAN_TREASURE_BUTTON = "Scan treasure"

@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SearchingScreen(
    navController: NavController,
    onClickOnGuide: GoToGuide,
    goToTipPhoto: GoToTipPhoto,
    goToResult: GoToResults,
    goToMap: GoToMap,
    goToTreasureSelector: GoToTreasureSelector,
    goToFacebook: GoToFacebook,
    goToCommemorative: GoToCommemorative
) {
    val cameraPermission: PermissionState = handlePermission(pl.marianjureczko.poszukiwacz.permissions.RequirementsForDoingCommemorativePhoto)
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val viewModel: SearchingViewModel = getViewModel()
    val title = "${stringResource(R.string.treasure)} ${viewModel.state.value.treasuresProgress.selectedTreasure.id}"
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopBar(navController, title, onClickOnGuide, goToFacebook) },
        content = {
            SearchingScreenBody(
                navController,
                viewModel,
                goToTipPhoto,
                goToResult,
                goToMap,
                goToTreasureSelector,
                cameraPermission,
                goToCommemorative
            )
        }
    )
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun SearchingScreenBody(
    navController: NavController,
    viewModel: SearchingViewModel,
    goToTipPhoto: GoToTipPhoto,
    goToResult: GoToResults,
    goToMap: GoToMap,
    goToTreasureSelector: GoToTreasureSelector,
    cameraPermissionState: PermissionState,
    goToCommemorative: GoToCommemorative
) {
    val state: SearchingSharedState = viewModel.state.value
    if (!isOnStack(navController, SELECTOR_ROUTE)
        && !isOnStack(navController, RESULTS_ROUTE)
        && state.treasureFoundAndResultAlreadyPresented()
    ) {
        goToTreasureSelector(state.treasuresProgress.justFoundTreasureId!!)
    }
    val scanQrLauncher = rememberLauncherForActivityResult(
        contract = ScanContract(),
        onResult = viewModel.scannedTreasureCallback { resultType, treasureId ->
            goToResult(resultType, treasureId)
        }
    )
    val prompt = stringResource(R.string.qr_scanner_msg)
    val scanQrCallback: GoToQrScanner = {
        val scanOptions = ScanOptions()
        scanOptions.setPrompt(prompt)
        scanQrLauncher.launch(scanOptions)
    }
    Column {
        Box {
            Scores(state.treasuresProgress.knowledge, Modifier.align(Alignment.TopStart))
            Compass(state.needleRotation, Modifier.align(Alignment.Center))
            CommemorativePhotoButton(
                cameraPermissionState.status.isGranted,
                state,
                state.treasuresProgress.selectedTreasure,
                goToCommemorative,
                viewModel,
                Modifier.align(Alignment.TopEnd).padding(15.dp)
            )
        }
        Steps(state.stepsToTreasure)
        Buttons(
            scanQrCallback,
            state.treasuresProgress.selectedTreasure,
            state.route,
            state.mediaPlayer,
            goToTipPhoto,
            goToMap
        ) { goToTreasureSelector(NOTHING_FOUND_TREASURE_ID) }
        Spacer(
            modifier = Modifier
                .weight(0.01f)
                .background(Color.Transparent)
        )
        AdvertBanner()
    }
}

@Composable
fun Compass(arcRotation: Float, modifier: Modifier) {
    BoxWithConstraints(
        modifier = modifier
            .padding(15.dp)
            .fillMaxWidth()
            .height(0.40.dh)
            .semantics { contentDescription = COMPASS },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painterResource(R.drawable.compass),
            contentDescription = "compass face",
            contentScale = ContentScale.Inside,
        )
        Image(
            painter = painterResource(R.drawable.arrow),
            contentDescription = "compass needle",
            contentScale = ContentScale.Inside,
            modifier = Modifier.rotate(arcRotation)
        )
    }
}

@Composable
fun Steps(stepsToTreasure: Int?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .height(0.15.dh),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Top,
    ) {
        if (stepsToTreasure != null) {
            Text(
                modifier = Modifier
                    .padding(start = 40.dp)
                    .semantics { contentDescription = STEPS_TO_TREASURE },
                style = MaterialTheme.typography.h2,
                color = Color.Gray,
                text = stepsToTreasure.toString()
            )
        } else {
            CircularProgressIndicator(Modifier.semantics { this.contentDescription = "Waiting for GPS" })
        }
        Image(
            painterResource(R.drawable.steps),
            modifier = Modifier.padding(start = 45.dp),
            contentDescription = null,
            contentScale = ContentScale.Inside,
        )
    }
}

@Composable
fun Buttons(
    scanQrCallback: GoToQrScanner,
    currentTreasure: TreasureDescription,
    route: Route,
    mediaPlayer: MediaPlayer,
    goToTipPhoto: GoToTipPhoto,
    goToMap: GoToMap,
    goToTreasureSelector: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .height(0.20.dh),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.width(0.2.dw)) {
            ShowMapButton(route, goToMap)
            ChangeTreasureButton(goToTreasureSelector)
        }
        Column(modifier = Modifier.width(0.6.dw)) {
            ScanTreasureButton(scanQrCallback)
        }
        Column(modifier = Modifier.width(0.2.dw)) {
            PhotoTipButton(currentTreasure, goToTipPhoto)
            SoundTipButton(currentTreasure, mediaPlayer)
        }
    }
}

@Composable
private fun ShowMapButton(route: Route, goToMap: GoToMap) {
    Image(
        painterResource(R.drawable.map),
        modifier = Modifier
            .padding(10.dp)
            .clickable { goToMap(route.name) },
        contentDescription = null,
        contentScale = ContentScale.Inside,
    )
}

@Composable
private fun ChangeTreasureButton(goToTreasureSelector: () -> Unit) {
    Image(
        painterResource(R.drawable.change_chest),
        modifier = Modifier
            .padding(10.dp)
            .clickable { goToTreasureSelector() },
        contentDescription = "Change treasure button",
        contentScale = ContentScale.Inside,
    )
}

@Composable
private fun ScanTreasureButton(scanQrCallback: GoToQrScanner) {
    Image(
        painterResource(R.drawable.chest),
        modifier = Modifier
            .padding(start = 20.dp)
            .clickable { scanQrCallback() },
        contentDescription = SCAN_TREASURE_BUTTON,
        contentScale = ContentScale.Inside,
    )
}

@Composable
private fun PhotoTipButton(currentTreasure: TreasureDescription, goToTipPhoto: GoToTipPhoto) {
    val noPhotoToShowMsg = stringResource(R.string.no_photo_to_show)
    val context = LocalContext.current
    Image(
        painterResource(R.drawable.show_photo),
        modifier = Modifier
            .padding(10.dp)
            .clickable {
                if (currentTreasure.hasPhoto()) {
                    val encodedFilePath = URLEncoder.encode(currentTreasure.photoFileName!!, Charsets.UTF_8.name())
                    goToTipPhoto(encodedFilePath)
                } else {
                    errorTone()
                    Toast.makeText(context, noPhotoToShowMsg, Toast.LENGTH_LONG).show()
                }
            },
        contentDescription = null,
        contentScale = ContentScale.Inside,
    )
}

@Composable
private fun SoundTipButton(currentTreasure: TreasureDescription, mediaPlayer: MediaPlayer) {
    val noTipToPlayMsg = stringResource(R.string.no_tip_to_play)
    val context = LocalContext.current
    Image(
        painterResource(R.drawable.megaphone),
        modifier = Modifier
            .padding(10.dp)
            .clickable {
                if (currentTreasure.tipFileName != null) {
                    SoundTipPlayer.playSound(mediaPlayer, currentTreasure.tipFileName!!)
                } else {
                    errorTone()
                    Toast.makeText(context, noTipToPlayMsg, Toast.LENGTH_LONG).show()
                }
            },
        contentDescription = null,
        contentScale = ContentScale.Inside,
    )
}

@Composable
private fun getViewModel(): SearchingViewModel {
    val viewModelDoNotInline: SharedViewModel = hiltViewModel()
    return viewModelDoNotInline
}
