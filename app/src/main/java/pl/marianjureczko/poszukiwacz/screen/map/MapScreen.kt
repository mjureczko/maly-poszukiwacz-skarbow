package pl.marianjureczko.poszukiwacz.screen.map

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.screen.searching.RestarterSharedViewModel
import pl.marianjureczko.poszukiwacz.shared.GoToFacebook
import pl.marianjureczko.poszukiwacz.shared.GoToGuide
import pl.marianjureczko.poszukiwacz.shared.MapHelper.DEFAULT_STYLE
import pl.marianjureczko.poszukiwacz.ui.components.AdvertBanner
import pl.marianjureczko.poszukiwacz.ui.components.GoToBadgesScreen
import pl.marianjureczko.poszukiwacz.ui.components.TopBar
import pl.marianjureczko.poszukiwacz.ui.components.TreasureHunterMap
import pl.marianjureczko.poszukiwacz.ui.components.ViewModelProgressRestarter
import pl.marianjureczko.poszukiwacz.ui.getSharedViewModel

@Composable
fun MapScreen(
    navController: NavController,
    navBackStackEntry: NavBackStackEntry,
    onClickOnGuide: GoToGuide,
    onClickOnFacebook: GoToFacebook,
    onClickBadges: GoToBadgesScreen,
) {
    val viewModel: MapViewModel = hiltViewModel()
    val state = viewModel.state.value
    val shared: RestarterSharedViewModel = getSharedViewModel(navBackStackEntry, navController)
    val restarter = ViewModelProgressRestarter { shared.restartProgress() }
    Scaffold(
        topBar = {
            TopBar(
                navController = navController,
                title = stringResource(R.string.map_activity_title),
                menuConfig = mapMenuConfig(onClickOnGuide, onClickOnFacebook, state, restarter, onClickBadges),
            )
        },
        content = { paddingValues -> MapScreenBody(Modifier.padding(paddingValues), state) }
    )
}

@Composable
fun MapScreenBody(
    modifier: Modifier,
    state: MapState
) {
    Column(modifier = modifier) {
        TreasureHunterMap(Modifier.weight(1f), state.route.treasures, DEFAULT_STYLE)
        AdvertBanner()
    }
}
