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
import pl.marianjureczko.poszukiwacz.ui.components.GoToBadgesScreen

//Custom
@Composable
fun ComposeRoot(onClickGuide: GoToGuide) {
    val navController = rememberNavController()
    val goToFacebook: GoToFacebook = FacebookHelper.createFacebookCallback(navController)
    val goToCommemorative: GoToCommemorative = getGoToCommemorative(navController)
    val goToResultsFromSelector = GoToResultWithTreasure { treasureId, isJustFound ->
        navController.navigate(
            Screens.Results.doRoute(
                CustomInitializerForRoute.routeName,
                ResultType.KNOWLEDGE,
                isJustFound,
                treasureId,
                1
            )
        )
    }
    val goToBadges = GoToBadgesScreen { navController.navigate(Screens.Badges.ROUTE) }

    NavHost(navController, startDestination = Screens.Main.ROUTE) {
        main(navController, onClickGuide, goToBadges)
        searching(navController, onClickGuide, goToFacebook, goToCommemorative, goToBadges)
        results(navController, onClickGuide, goToFacebook, goToBadges)
        tipPhoto(navController, onClickGuide, goToFacebook, goToBadges)
        map(navController, onClickGuide, goToFacebook, goToBadges)
        selector(navController, onClickGuide, goToCommemorative, goToFacebook, goToResultsFromSelector, goToBadges)
        commemorative(navController, onClickGuide, goToFacebook, goToBadges)
        facebook(navController, onClickGuide, goToBadges)
    }
}

private fun NavGraphBuilder.main(
    navController: NavHostController,
    onClickGuide: GoToGuide,
    onClickBadges: GoToBadgesScreen,
) {
    composable(route = Screens.Main.ROUTE) {
        MainScreen(
            navController = navController,
            onClickOnGuide = onClickGuide,
            onClickBadges = onClickBadges,
        ) { routeName ->
            navController.navigate(Screens.Searching.doRoute(routeName))
        }
    }
}