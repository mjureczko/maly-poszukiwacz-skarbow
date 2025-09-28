package pl.marianjureczko.poszukiwacz.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pl.marianjureczko.poszukiwacz.screen.Screens
import pl.marianjureczko.poszukiwacz.screen.facebook.FacebookHelper
import pl.marianjureczko.poszukiwacz.screen.main.CustomInitializerForRoute
import pl.marianjureczko.poszukiwacz.screen.main.MainScreen
import pl.marianjureczko.poszukiwacz.screen.result.ResultType
import pl.marianjureczko.poszukiwacz.shared.GoToCommemorative
import pl.marianjureczko.poszukiwacz.shared.GoToFacebook
import pl.marianjureczko.poszukiwacz.shared.GoToGuide
import pl.marianjureczko.poszukiwacz.shared.GoToResultWithTreasure

//Custom
@Composable
fun ComposeRoot(onClickGuide: GoToGuide) {
    val navController = rememberNavController()
    val goToFacebook: GoToFacebook = FacebookHelper.createFacebookCallback(navController)
    val goToCommemorative: GoToCommemorative = getGoToCommemorative(navController)
    val goToResultsFromSelector: GoToResultWithTreasure = { treasureId ->
        navController.navigate(
            Screens.Results.doRoute(CustomInitializerForRoute.routeName, ResultType.KNOWLEDGE, treasureId, 1)
        )
    }

    NavHost(navController, startDestination = Screens.Main.ROUTE) {
        main(navController, onClickGuide)
        searching(navController, onClickGuide, goToFacebook, goToCommemorative)
        results(navController, onClickGuide, goToFacebook)
        tipPhoto(navController, onClickGuide, goToFacebook)
        map(navController, onClickGuide, goToFacebook)
        selector(navController, onClickGuide, goToCommemorative, goToFacebook, goToResultsFromSelector)
        commemorative(navController, onClickGuide, goToFacebook)
        facebook(navController, onClickGuide)
    }
}

private fun NavGraphBuilder.main(
    navController: NavHostController,
    onClickGuide: GoToGuide,
) {
    composable(route = Screens.Main.ROUTE) {
        MainScreen(
            navController = navController,
            onClickOnGuide = onClickGuide,
        ) { routeName ->
            navController.navigate(Screens.Searching.doRoute(routeName))
        }
    }
}