package pl.marianjureczko.poszukiwacz.screen.treasureselector

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import pl.marianjureczko.poszukiwacz.ui.components.GoToBadgesScreen
import pl.marianjureczko.poszukiwacz.ui.components.OkDialog
import pl.marianjureczko.poszukiwacz.ui.components.TopBar
import pl.marianjureczko.poszukiwacz.ui.components.ViewModelProgressRestarter
import pl.marianjureczko.poszukiwacz.ui.getSharedViewModel
import pl.marianjureczko.poszukiwacz.ui.handlePermission
import pl.marianjureczko.poszukiwacz.ui.theme.FANCY_FONT
import pl.marianjureczko.poszukiwacz.ui.theme.Shapes

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SelectorScreen(
    navController: NavController,
    navBackStackEntry: NavBackStackEntry,
    onClickOnGuide: GoToGuide,
    goToResult: GoToResultWithTreasure,
    goToCommemorative: GoToCommemorative,
    onClickOnFacebook: GoToFacebook,
    onClickBadges: GoToBadgesScreen,
) {
    val cameraPermission: PermissionState =
        handlePermission(pl.marianjureczko.poszukiwacz.permissions.RequirementsForDoingCommemorativePhoto)
    val sharedViewModel: SelectorSharedViewModel = getSharedViewModel(navBackStackEntry, navController)
    val sharedState: SelectorSharedState = sharedViewModel.state.value
    val restarter = ViewModelProgressRestarter { sharedViewModel.restartProgress() }
    Scaffold(
        topBar = {
            TopBar(
                navController = navController,
                title = stringResource(R.string.select_treasure_dialog_title),
                menuConfig = selectorMenuConfig(
                    onClickOnGuide,
                    onClickOnFacebook,
                    sharedState,
                    restarter,
                    onClickBadges
                )
            )
        },
        content = { paddingValues ->
            SelectorScreenBody(
                Modifier.padding(paddingValues),
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
    modifier: Modifier,
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
    Column(modifier = modifier.background(Color.White)) {
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
            WellDoneOkDialogContent(onClickOnFacebook)
        }
    }
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
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
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
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                    fontFamily = FANCY_FONT,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.3f)
                )
            } else {
                Text(
                    text = "[${treasureDescription.id}]",
                    style = MaterialTheme.typography.headlineMedium,
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
                updateImageRefresh = {},
            )
        }
    }
}

