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

//Classic
@Composable
fun ComposeRoot(onClickGuide: GoToGuide) {
    val navController = rememberNavController()
    val goToFacebook: GoToFacebook = FacebookHelper.createFacebookCallback(navController)
    val goToCommemorative: GoToCommemorative = getGoToCommemorative(navController)
    val goToEditor: GoToTreasureEditor =
        { routeName -> navController.navigate(Screens.TreasureEditor.doRoute(routeName)) }
    val goToResultsFromSelector: GoToResultWithTreasure = {}

    NavHost(navController, startDestination = Screens.Main.ROUTE) {
        main(navController, onClickGuide, goToFacebook, goToEditor)
        treasureEditor(navController, onClickGuide, goToFacebook)
        searching(navController, onClickGuide, goToFacebook, goToCommemorative)
        results(navController, onClickGuide, goToFacebook)
        tipPhoto(navController, onClickGuide, goToFacebook)
        map(navController, onClickGuide, goToFacebook)
        selector(navController, onClickGuide, goToCommemorative, goToFacebook, goToResultsFromSelector)
        commemorative(navController, onClickGuide, goToFacebook)
        facebook(navController, onClickGuide)
        bluetooth(navController, onClickGuide, goToFacebook)
    }
}

private fun NavGraphBuilder.bluetooth(
    navController: NavHostController,
    onClickGuide: GoToGuide,
    goToFacebook: GoToFacebook
) {
    composable(
        route = Screens.Bluetooth.ROUTE,
        arguments = listOf(
            navArgument(Screens.Bluetooth.PARAMETER_MODE) { type = NavType.EnumType(Mode::class.java) },
            navArgument(Screens.Bluetooth.PARAMETER_ROUTE_TO_SENT) { type = NavType.StringType },
        ),
    ) { navBackStackEntry ->
        BluetoothScreen(
            navController = navController,
            onClickOnGuide = onClickGuide,
            onClickOnFacebook = goToFacebook
        )
    }
}

private fun NavGraphBuilder.treasureEditor(
    navController: NavHostController,
    onClickGuide: GoToGuide,
    goToFacebook: GoToFacebook
) {
    composable(
        route = Screens.TreasureEditor.ROUTE,
        arguments = listOf(navArgument(Screens.TreasureEditor.PARAMETER_ROUTE_NAME) { type = NavType.StringType })
    ) {
        TreasureEditorScreen(
            navController = navController,
            onClickOnGuide = onClickGuide,
            onClickOnFacebook = goToFacebook
        )
    }
}

private fun NavGraphBuilder.main(
    navController: NavHostController,
    onClickGuide: GoToGuide,
    goToFacebook: GoToFacebook,
    goToEditor: GoToTreasureEditor
) {
    composable(route = Screens.Main.ROUTE) {
        MainScreen(
            navController = navController,
            onClickOnGuide = onClickGuide,
            onClickOnFacebook = goToFacebook,
            goToBluetooth = { mode: Mode, route: String ->
                navController.navigate(Screens.Bluetooth.doRoute(mode, route))
            },
            goToTreasureEditor = goToEditor,
        ) { routeName ->
            navController.navigate(Screens.Searching.doRoute(routeName))
        }
    }
}
