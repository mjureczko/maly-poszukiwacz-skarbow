package pl.marianjureczko.poszukiwacz.activity.treasureselector.n

import android.annotation.SuppressLint
import android.content.res.Resources
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
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.activity.searching.n.SelectorSharedState
import pl.marianjureczko.poszukiwacz.activity.searching.n.SelectorSharedViewModel
import pl.marianjureczko.poszukiwacz.activity.searching.n.SharedViewModel
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.ui.components.AdvertBanner
import pl.marianjureczko.poszukiwacz.ui.components.TopBar
import pl.marianjureczko.poszukiwacz.ui.shareViewModelStoreOwner
import pl.marianjureczko.poszukiwacz.ui.theme.FANCY_FONT

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SelectorScreen(
    navController: NavController,
    navBackStackEntry: NavBackStackEntry,
    resources: Resources,
    onClickOnGuide: () -> Unit,
    goToResult: (Int) -> Unit
) {
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopBar(navController, onClickOnGuide) },
        content = {
            SelectorScreenBody(
                navController,
                shareViewModelStoreOwner(navBackStackEntry, navController),
                resources,
                scaffoldState,
                goToResult
            )
        }
    )
}

@Composable
fun SelectorScreenBody(
    navController: NavController,
    viewModelStoreOwner: NavBackStackEntry,
    resources: Resources,
    scaffoldState: ScaffoldState,
    goToResult: (Int) -> Unit
) {
    val localViewModel: SelectorViewModel = hiltViewModel()
    val localState: SelectorState = localViewModel.state.value
    val sharedViewModel: SelectorSharedViewModel = getViewModel(viewModelStoreOwner)
    sharedViewModel.selectorPresented()
    val state: SelectorSharedState = sharedViewModel.state.value
    Column(Modifier.background(Color.White)) {
        LazyColumn(
            contentPadding = PaddingValues(vertical = 8.dp, horizontal = 8.dp),
            modifier = Modifier.weight(0.99f)
        ) {
            items(state.route.treasures) { treasure ->
                TreasureItem(navController, resources, treasure, localState, state, goToResult, sharedViewModel)
            }
        }
        Spacer(modifier = Modifier.weight(0.01f))
        AdvertBanner()
    }
    localViewModel.delayedUpdateOfJustFound()
    sharedViewModel.updateJustFoundFromSelector()
}

@Composable
fun TreasureItem(
    navController: NavController,
    resources: Resources,
    treasure: TreasureDescription,
    localState: SelectorState,
    state: SelectorSharedState,
    goToResult: (Int) -> Unit,
    sharedViewModel: SelectorSharedViewModel
) {
    Card(
        elevation = 4.dp,
        modifier = Modifier
            .padding(4.dp)
            .clickable {
                sharedViewModel.updateCurrentTreasure(treasure)
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
            TreasureCollectedCheckbox(state, localState, treasure)
            val distanceInSteps = state.distancesInSteps[treasure.id]
            if (distanceInSteps != null) {
                Text(
                    text = resources.getString(R.string.steps_to_treasure, treasure.id, distanceInSteps),
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center,
                    fontFamily = FANCY_FONT,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.3f)
                )
            } else {
                Text(
                    text = "[${treasure.id}]",
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
            ShowMovieButton(state, treasure, goToResult)
            PhotoButton(state, treasure)
        }
    }
}

@Composable
fun ShowMovieButton(state: SelectorSharedState, treasure: TreasureDescription, goToResult: (Int) -> Unit) {
    if (state.isTreasureCollected(treasure.id)) {
        Image(
            painterResource(R.drawable.movie),
            modifier = Modifier
                .padding(2.dp)
                .height(35.dp)
                .clickable { goToResult(treasure.id) },
            contentDescription = "Show treasure movie",
            contentScale = ContentScale.Inside,
        )
    }
}

@Composable
private fun PhotoButton(sharedState: SelectorSharedState, treasure: TreasureDescription) {
    if (sharedState.hasCommemorativePhoto(treasure.id)) {
        Image(
            painterResource(R.drawable.camera_show_photo),
            modifier = Modifier
                .padding(2.dp)
                .height(35.dp),
            contentDescription = "Show commemorative photo",
            contentScale = ContentScale.Inside,
        )
    } else {
        Image(
            painterResource(R.drawable.camera_do_photo),
            modifier = Modifier
                .padding(2.dp)
                .height(35.dp),
            contentDescription = "Do commemorative photo",
            contentScale = ContentScale.Inside,
        )
    }
}

@Composable
private fun TreasureCollectedCheckbox(
    state: SelectorSharedState,
    localState: SelectorState,
    treasure: TreasureDescription
) {
    val imageId: Int = if (treasure.id != localState.justFoundTreasureId && state.isTreasureCollected(treasure.id)) {
        R.drawable.checkbox_checked
    } else {
        R.drawable.checkbox_empty
    }
    Image(
        painterResource(imageId),
        modifier = Modifier
            .padding(2.dp)
            .height(40.dp),
        contentDescription = "Change treasure button",
        contentScale = ContentScale.Inside,
    )
}

@Composable
private fun getViewModel(viewModelStoreOwner: NavBackStackEntry): SelectorSharedViewModel {
    val viewModelDoNotInline: SharedViewModel = hiltViewModel(viewModelStoreOwner)
    return viewModelDoNotInline
}

//@Preview(showBackground = true, apiLevel = 31)
//@Composable
//fun SelectorDefaultPreview() {
//    AppTheme {
//        SelectorScreen(null, App.getResources(), {})
//    }
//}