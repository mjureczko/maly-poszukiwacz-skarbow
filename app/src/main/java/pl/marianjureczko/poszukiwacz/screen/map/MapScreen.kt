package pl.marianjureczko.poszukiwacz.screen.map

import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.shared.GoToFacebook
import pl.marianjureczko.poszukiwacz.shared.GoToGuide
import pl.marianjureczko.poszukiwacz.shared.MapHelper.DEFAULT_STYLE
import pl.marianjureczko.poszukiwacz.ui.components.TopBar
import pl.marianjureczko.poszukiwacz.ui.components.TreasureHunterMap

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MapScreen(
    navController: NavController,
    onClickOnGuide: GoToGuide,
    onClickOnFacebook: GoToFacebook
) {
    val viewModel: MapViewModel = hiltViewModel()
    val state = viewModel.state.value
    Scaffold(
        topBar = {
            TopBar(
                navController = navController,
                title = stringResource(R.string.map_activity_title),
                onClickOnGuide = onClickOnGuide,
                onClickOnFacebook = { onClickOnFacebook(state.route.name) },
            )
        },
        content = { MapScreenBody(state) }
    )
}

@Composable
fun MapScreenBody(state: MapState) {
    TreasureHunterMap(state.route.treasures, DEFAULT_STYLE)
}

