package pl.marianjureczko.poszukiwacz.screen.treasureselector

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.screen.searching.SelectorSharedState
import pl.marianjureczko.poszukiwacz.screen.searching.SelectorSharedViewModel
import pl.marianjureczko.poszukiwacz.shared.GoToCommemorative
import pl.marianjureczko.poszukiwacz.shared.GoToFacebook
import pl.marianjureczko.poszukiwacz.shared.GoToGuide
import pl.marianjureczko.poszukiwacz.shared.GoToResultWithTreasure
import pl.marianjureczko.poszukiwacz.ui.components.AdvertBanner
import pl.marianjureczko.poszukiwacz.ui.components.CommemorativePhotoButton
import pl.marianjureczko.poszukiwacz.ui.components.MenuConfig
import pl.marianjureczko.poszukiwacz.ui.components.OkDialog
import pl.marianjureczko.poszukiwacz.ui.components.TopBar
import pl.marianjureczko.poszukiwacz.ui.components.ViewModelProgressRestarter
import pl.marianjureczko.poszukiwacz.ui.getSharedViewModel
import pl.marianjureczko.poszukiwacz.ui.handlePermission
import pl.marianjureczko.poszukiwacz.ui.theme.FANCY_FONT
import pl.marianjureczko.poszukiwacz.ui.theme.Shapes
import pl.marianjureczko.poszukiwacz.ui.theme.Typography

@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SelectorScreen(
    navController: NavController,
    navBackStackEntry: NavBackStackEntry,
    onClickOnGuide: GoToGuide,
    goToResult: GoToResultWithTreasure,
    goToCommemorative: GoToCommemorative,
    onClickOnFacebook: GoToFacebook,
) {
    val cameraPermission: PermissionState =
        handlePermission(pl.marianjureczko.poszukiwacz.permissions.RequirementsForDoingCommemorativePhoto)
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val sharedViewModel: SelectorSharedViewModel = getSharedViewModel(navBackStackEntry, navController)
    val sharedState: SelectorSharedState = sharedViewModel.state.value
    val restarter = ViewModelProgressRestarter { sharedViewModel.restartProgress() }
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopBar(
                navController = navController,
                title = stringResource(R.string.select_treasure_dialog_title),
                menuConfig = MenuConfig(onClickOnGuide, { onClickOnFacebook(sharedState.route.name) }, restarter)
            )
        },
        content = {
            SelectorScreenBody(
                cameraPermission,
                navController,
                sharedViewModel,
                sharedState,
                goToResult,
                goToCommemorative,
                { onClickOnFacebook(sharedState.route.name) },
            )
        }
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SelectorScreenBody(
    cameraPermission: PermissionState,
    navController: NavController,
    sharedViewModel: SelectorSharedViewModel,
    state: SelectorSharedState,
    goToResult: GoToResultWithTreasure,
    goToCommemorative: GoToCommemorative,
    onClickOnFacebook: () -> Unit,
) {
    val localViewModel: SelectorViewModel = hiltViewModel()
    val localState: SelectorState = localViewModel.state.value
    sharedViewModel.selectorPresented()
    Column(Modifier.background(Color.White)) {
        LazyColumn(
            contentPadding = PaddingValues(vertical = 8.dp, horizontal = 8.dp),
            modifier = Modifier.weight(0.99f)
        ) {
            items(state.route.treasures) { treasure ->
                TreasureItem(
                    cameraPermission,
                    navController,
                    treasure,
                    localState,
                    state,
                    goToResult,
                    sharedViewModel,
                    goToCommemorative
                )
            }
        }
        Spacer(modifier = Modifier.weight(0.01f))
        AdvertBanner()
    }
    localViewModel.delayedUpdateOfJustFound()
    sharedViewModel.updateJustFoundFromSelector()
    if (!localState.wellDoneShown && sharedViewModel.state.value.allTreasuresCollected()) {
        OkDialog(true, localViewModel::wellDoneShown) {
            OkDialogContent(onClickOnFacebook)
        }
    }
}

@Composable
private fun OkDialogContent(onClickOnFacebook: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row {
            Image(
                painterResource(R.drawable.tada),
                contentDescription = "tada icon",
                modifier = Modifier
                    .padding(end = 5.dp, top = 20.dp)
                    .height(60.dp)
            )
            Text(
                text = stringResource(R.string.well_done),
                fontSize = Typography.h5.fontSize,
                textAlign = TextAlign.Center,
                fontFamily = FANCY_FONT,
            )
        }
        OkDialogText(R.string.well_done_facebook)
        OutlinedButton(
            onClick = { onClickOnFacebook() },
            shape = Shapes.small,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.White,
                contentColor = Color.Black
            ),
            border = BorderStroke(2.dp, Color.LightGray),
            elevation = ButtonDefaults.elevation(4.dp),
        ) {
            Text("Facebook")
            Image(
                painterResource(R.drawable.facebook),
                contentDescription = "Facebook icon",
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        OkDialogText(R.string.well_done_more)
        MoreApps()
    }
}

@Composable
fun OkDialogText(textResourceId: Int) {
    Text(
        text = stringResource(textResourceId),
        fontSize = Typography.h6.fontSize,
        textAlign = TextAlign.Center,
        fontFamily = FANCY_FONT,
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun TreasureItem(
    cameraPermission: PermissionState,
    navController: NavController,
    treasureDescription: TreasureDescription,
    localState: SelectorState,
    state: SelectorSharedState,
    goToResult: GoToResultWithTreasure,
    sharedViewModel: SelectorSharedViewModel,
    goToCommemorative: GoToCommemorative
) {
    Card(
        elevation = 4.dp,
        shape = Shapes.large,
        modifier = Modifier
            .padding(4.dp)
            .clickable {
                sharedViewModel.updateSelectedTreasure(treasureDescription)
                navController.navigateUp()
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            TreasureCollectedCheckbox(state, localState, treasureDescription, sharedViewModel)
            val distanceInSteps = state.distancesInSteps[treasureDescription.id]
            if (distanceInSteps != null) {
                Text(
                    text = stringResource(R.string.steps_to_treasure, treasureDescription.id, distanceInSteps),
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center,
                    fontFamily = FANCY_FONT,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.3f)
                )
            } else {
                Text(
                    text = "[${treasureDescription.id}]",
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center,
                    fontFamily = FANCY_FONT,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.3f)
                )
                CircularProgressIndicator(
                    Modifier
                        .padding(end = 50.dp)
                        .semantics { this.contentDescription = "Waiting for GPS" })
            }
            ShowMovieButton(state, treasureDescription, goToResult)
            val photo = state.treasuresProgress.commemorativePhotosByTreasuresDescriptionIds[treasureDescription.id]
            CommemorativePhotoButton(
                cameraPermission.status.isGranted,
                state,
                { treasureDescriptionId -> goToCommemorative(treasureDescriptionId, photo) },
                sharedViewModel,
                treasureDescriptionId = treasureDescription.id,
                //TODO t: check if refresh works
                updateImageRefresh = {},
            )
        }
    }
}

//@Preview(showBackground = true, apiLevel = 31)
//@Composable
//fun SelectorDefaultPreview() {
//    AppTheme {
//        SelectorScreen(null, App.getResources(), {})
//    }
//}