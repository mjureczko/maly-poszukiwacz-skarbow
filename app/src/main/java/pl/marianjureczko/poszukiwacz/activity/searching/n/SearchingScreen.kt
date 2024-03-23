package pl.marianjureczko.poszukiwacz.activity.searching.n

import android.annotation.SuppressLint
import android.content.res.Resources
import android.media.MediaPlayer
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.activity.main.RESULTS_ROUTE
import pl.marianjureczko.poszukiwacz.activity.main.SELECTOR_ROUTE
import pl.marianjureczko.poszukiwacz.activity.result.n.NOTHING_FOUND_TREASURE_ID
import pl.marianjureczko.poszukiwacz.activity.result.n.ResultType
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.shared.errorTone
import pl.marianjureczko.poszukiwacz.ui.Screen.dh
import pl.marianjureczko.poszukiwacz.ui.Screen.dw
import pl.marianjureczko.poszukiwacz.ui.components.AdvertBanner
import pl.marianjureczko.poszukiwacz.ui.components.TopBar
import pl.marianjureczko.poszukiwacz.ui.isOnStack
import pl.marianjureczko.poszukiwacz.ui.theme.SecondaryBackground
import java.net.URLEncoder

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SearchingScreen(
    navController: NavController,
    isClassicMode: Boolean,
    resources: Resources,
    onClickOnGuide: () -> Unit,
    goToTipPhoto: (String) -> Unit,
    goToResult: (ResultType, Int) -> Unit,
    goToMap: (String) -> Unit,
    goToTreasureSelector: (Int) -> Unit
) {
    val scaffoldState: ScaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopBar(navController, onClickOnGuide) },
        content = {
            SearchingScreenBody(
                navController,
                isClassicMode,
                resources,
                scaffoldState,
                goToTipPhoto,
                goToResult,
                goToMap,
                goToTreasureSelector
            )
        }
    )
}


@Composable
private fun SearchingScreenBody(
    navController: NavController,
    isClassicMode: Boolean,
    resources: Resources,
    scaffoldState: ScaffoldState,
    goToTipPhoto: (String) -> Unit,
    goToResult: (ResultType, Int) -> Unit,
    goToMap: (String) -> Unit,
    goToTreasureSelector: (Int) -> Unit
) {
    val viewModel: SearchingViewModel = getViewModel()
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
    val scanQrCallback: () -> Unit = {
        val scanOptions = ScanOptions()
        scanOptions.setPrompt(resources.getString(R.string.qr_scanner_msg))
        scanQrLauncher.launch(scanOptions)
    }
    Column(Modifier.background(SecondaryBackground)) {
        Scores(isClassicMode, state.treasuresProgress.knowledge)
        Compass(state.needleRotation)
        Steps(state.stepsToTreasure)
        Buttons(
            scanQrCallback,
            state.currentTreasure,
            state.route,
            scaffoldState,
            resources,
            state.mediaPlayer,
            goToTipPhoto,
            goToMap,
            {goToTreasureSelector(NOTHING_FOUND_TREASURE_ID)}
        )
        Spacer(
            modifier = Modifier
                .weight(0.01f)
                .background(Color.Transparent)
        )
        AdvertBanner()
    }
}

@Composable
fun Scores(isClassicMode: Boolean, score: Int) {
    Row(
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .height(0.05.dh),
    ) {
        if (isClassicMode) {
            Image(
                painterResource(R.drawable.gold),
                contentDescription = "gold image",
                contentScale = ContentScale.Inside,
            )
            Text(
                color = Color.Gray,
                text = "0",
                fontSize = pl.marianjureczko.poszukiwacz.ui.theme.Typography.h5.fontSize
            )
            Image(
                painterResource(R.drawable.ruby),
                contentDescription = "ruby image",
                contentScale = ContentScale.Inside,
                modifier = Modifier.padding(start = 20.dp)
            )
            Text(
                color = Color.Gray,
                text = "0",
                fontSize = pl.marianjureczko.poszukiwacz.ui.theme.Typography.h5.fontSize
            )
            Image(
                painterResource(R.drawable.diamond),
                contentDescription = "diamond image",
                contentScale = ContentScale.Inside,
                modifier = Modifier.padding(start = 20.dp)
            )
            Text(
                color = Color.Gray,
                text = "0",
                fontSize = pl.marianjureczko.poszukiwacz.ui.theme.Typography.h5.fontSize
            )
        } else {
            Image(
                painterResource(R.drawable.chest_small),
                contentDescription = "tourist treasure image",
                contentScale = ContentScale.Inside,
                modifier = Modifier.padding(end = 5.dp)
            )
            Text(
                color = Color.Gray,
                text = score.toString(),
                fontSize = pl.marianjureczko.poszukiwacz.ui.theme.Typography.h5.fontSize,
                modifier = Modifier.padding(end = 5.dp)
            )
        }
    }
}

@Composable
fun Compass(arcRotation: Float) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(0.35.dh),
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
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (stepsToTreasure != null) {
            Text(
                modifier = Modifier.padding(start = 50.dp),
                fontSize = 56.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                text = stepsToTreasure.toString()
            )
        } else {
            CircularProgressIndicator(Modifier.semantics { this.contentDescription = "Waiting for GPS" })
        }
        Image(
            painterResource(R.drawable.steps),
            modifier = Modifier.padding(start = 50.dp),
            contentDescription = null,
            contentScale = ContentScale.Inside,
        )
    }
}

@Composable
fun Buttons(
    scanQrCallback: () -> Unit,
    currentTreasure: TreasureDescription,
    route: Route,
    scaffoldState: ScaffoldState,
    resources: Resources,
    mediaPlayer: MediaPlayer,
    goToTipPhoto: (String) -> Unit,
    goToMap: (String) -> Unit,
    goToTreasureSelector: () -> Unit
) {
    val snackbarCoroutineScope: CoroutineScope = rememberCoroutineScope()
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
            PhotoTipButton(currentTreasure, scaffoldState, snackbarCoroutineScope, resources, goToTipPhoto)
            SoundTipButton(currentTreasure, scaffoldState, snackbarCoroutineScope, resources, mediaPlayer)
        }
    }
}

@Composable
private fun ShowMapButton(route: Route, goToMap: (String) -> Unit) {
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
private fun ScanTreasureButton(scanQrCallback: () -> Unit) {
    Image(
        painterResource(R.drawable.chest),
        modifier = Modifier
            .padding(start = 20.dp)
            .clickable { scanQrCallback() },
        contentDescription = null,
        contentScale = ContentScale.Inside,
    )
}

@Composable
private fun PhotoTipButton(
    currentTreasure: TreasureDescription,
    scaffoldState: ScaffoldState,
    snackbarCoroutineScope: CoroutineScope,
    resources: Resources,
    goToTipPhoto: (String) -> Unit
) {
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
                    snackbarCoroutineScope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(resources.getString(R.string.no_photo_to_show))
                    }
                }
            },
        contentDescription = null,
        contentScale = ContentScale.Inside,
    )
}

@Composable
private fun SoundTipButton(
    currentTreasure: TreasureDescription,
    scaffoldState: ScaffoldState,
    snackbarCoroutineScope: CoroutineScope,
    resources: Resources,
    mediaPlayer: MediaPlayer
) {
    Image(
        painterResource(R.drawable.megaphone),
        modifier = Modifier
            .padding(10.dp)
            .clickable {
                if (currentTreasure.tipFileName != null) {
                    SoundTipPlayer.playSound(mediaPlayer, currentTreasure.tipFileName!!)
                } else {
                    errorTone()
                    snackbarCoroutineScope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(resources.getString(R.string.no_tip_to_play))
                    }
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

//@Preview(showBackground = true, apiLevel = 31)
//@Composable
//fun SearchingDefaultPreview() {
//    AppTheme {
//        SearchingScreen(null, false, App.getResources(), {}, {}, { _, _ -> }, {}, { s: String, i: Int? -> })
//    }
//}