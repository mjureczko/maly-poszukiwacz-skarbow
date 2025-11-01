package pl.marianjureczko.poszukiwacz.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import pl.marianjureczko.poszukiwacz.screen.Screens
import pl.marianjureczko.poszukiwacz.screen.badges.BadgesScreen
import pl.marianjureczko.poszukiwacz.screen.bluetooth.BluetoothScreen
import pl.marianjureczko.poszukiwacz.screen.bluetooth.Mode
import pl.marianjureczko.poszukiwacz.screen.facebook.FacebookHelper
import pl.marianjureczko.poszukiwacz.screen.main.MainScreen
import pl.marianjureczko.poszukiwacz.screen.treasureseditor.TreasureEditorScreen
import pl.marianjureczko.poszukiwacz.shared.GoToCommemorative
import pl.marianjureczko.poszukiwacz.shared.GoToFacebook
import pl.marianjureczko.poszukiwacz.shared.GoToGuide
import pl.marianjureczko.poszukiwacz.shared.GoToResultWithTreasure
import pl.marianjureczko.poszukiwacz.shared.GoToTreasureEditor
import pl.marianjureczko.poszukiwacz.ui.components.GoToBadgesScreen

//Classic
@Composable
fun ComposeRoot(onClickGuide: GoToGuide) {
    val navController = rememberNavController()
    val goToFacebook: GoToFacebook = FacebookHelper.createFacebookCallback(navController)
    val goToCommemorative: GoToCommemorative = getGoToCommemorative(navController)
    val goToEditor: GoToTreasureEditor =
        { routeName -> navController.navigate(Screens.TreasureEditor.doRoute(routeName)) }
    val goToResultsFromSelector = GoToResultWithTreasure { _, _ -> /*do nothing*/ }
    val goToBadges = GoToBadgesScreen { navController.navigate(Screens.Badges.ROUTE) }

    NavHost(navController, startDestination = Screens.Main.ROUTE) {
        main(navController, onClickGuide, goToFacebook, goToEditor, goToBadges)
        treasureEditor(navController, onClickGuide, goToBadges)
        searching(navController, onClickGuide, goToFacebook, goToCommemorative, goToBadges)
        results(navController, onClickGuide, goToFacebook, goToBadges)
        tipPhoto(navController, onClickGuide, goToFacebook, goToBadges)
        map(navController, onClickGuide, goToFacebook, goToBadges)
        selector(navController, onClickGuide, goToCommemorative, goToFacebook, goToResultsFromSelector, goToBadges)
        commemorative(navController, onClickGuide, goToFacebook, goToBadges)
        facebook(navController, onClickGuide, goToBadges)
        bluetooth(navController, onClickGuide, goToBadges)
        badges(navController, onClickGuide)
    }
}

private fun NavGraphBuilder.badges(
    navController: NavHostController,
    onClickGuide: GoToGuide,
) {
    composable(route = Screens.Badges.ROUTE) { _ ->
        BadgesScreen(navController = navController, onClickOnGuide = onClickGuide)
    }
}

private fun NavGraphBuilder.bluetooth(
    navController: NavHostController,
    onClickGuide: GoToGuide,
    goToBadges: GoToBadgesScreen,
) {
    composable(
        route = Screens.Bluetooth.ROUTE,
        arguments = listOf(
            navArgument(Screens.Bluetooth.PARAMETER_MODE) { type = NavType.EnumType(Mode::class.java) },
            navArgument(Screens.Bluetooth.PARAMETER_ROUTE_TO_SENT) { type = NavType.StringType },
        ),
    ) { _ -> BluetoothScreen(navController = navController, onClickOnGuide = onClickGuide, goToBadges = goToBadges) }
}

private fun NavGraphBuilder.treasureEditor(
    navController: NavHostController,
    onClickGuide: GoToGuide,
    goToBadges: GoToBadgesScreen,
) {
    composable(
        route = Screens.TreasureEditor.ROUTE,
        arguments = listOf(navArgument(Screens.TreasureEditor.PARAMETER_ROUTE_NAME) { type = NavType.StringType })
    ) {
        TreasureEditorScreen(
            navController = navController,
            onClickOnGuide = onClickGuide,
            goToBadges = goToBadges
        )
    }
}

private fun NavGraphBuilder.main(
    navController: NavHostController,
    onClickGuide: GoToGuide,
    goToFacebook: GoToFacebook,
    goToEditor: GoToTreasureEditor,
    goToBadges: GoToBadgesScreen,
) {
    composable(route = Screens.Main.ROUTE) {
        MainScreen(
            navController = navController,
            onClickOnGuide = onClickGuide,
            goToBluetooth = { mode: Mode, route: String ->
                navController.navigate(Screens.Bluetooth.doRoute(mode, route))
            },
            goToTreasureEditor = goToEditor,
            goToSearching = { routeName -> navController.navigate(Screens.Searching.doRoute(routeName)) },
            goToBadges = goToBadges,
        )
    }
}
