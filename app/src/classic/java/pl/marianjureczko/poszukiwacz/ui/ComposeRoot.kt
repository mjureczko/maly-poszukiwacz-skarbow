package pl.marianjureczko.poszukiwacz.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import pl.marianjureczko.poszukiwacz.screen.Screens
import pl.marianjureczko.poszukiwacz.screen.bluetooth.BluetoothScreen
import pl.marianjureczko.poszukiwacz.screen.bluetooth.Mode
import pl.marianjureczko.poszukiwacz.screen.commemorative.CommemorativeScreen
import pl.marianjureczko.poszukiwacz.screen.facebook.FacebookHelper
import pl.marianjureczko.poszukiwacz.screen.facebook.FacebookScreen
import pl.marianjureczko.poszukiwacz.screen.main.MainScreen
import pl.marianjureczko.poszukiwacz.screen.map.MapScreen
import pl.marianjureczko.poszukiwacz.screen.phototip.TipPhotoScreen
import pl.marianjureczko.poszukiwacz.screen.result.ResultScreen
import pl.marianjureczko.poszukiwacz.screen.result.ResultType
import pl.marianjureczko.poszukiwacz.screen.searching.SearchingScreen
import pl.marianjureczko.poszukiwacz.screen.treasureseditor.TreasureEditorScreen
import pl.marianjureczko.poszukiwacz.screen.treasureselector.SelectorScreen
import pl.marianjureczko.poszukiwacz.shared.GoToCommemorative
import pl.marianjureczko.poszukiwacz.shared.GoToFacebook
import pl.marianjureczko.poszukiwacz.shared.GoToGuide
import pl.marianjureczko.poszukiwacz.shared.GoToTreasureEditor

@Composable
fun ComposeRoot(onClickGuide: GoToGuide) {
    val navController = rememberNavController()
    val goToFacebook: GoToFacebook = FacebookHelper.createFacebookCallback(navController)
    val goToCommemorative: GoToCommemorative = { treasureId, photoPath ->
        navController.navigate(Screens.Commemorative.doRoute(treasureId, photoPath))
    }
    val goToEditor: GoToTreasureEditor =
        { routeName -> navController.navigate(Screens.TreasureEditor.doRoute(routeName)) }

    NavHost(navController, startDestination = "main") {
        composable(route = "main") {
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
        composable(
            route = Screens.Searching.ROUTE,
            arguments = listOf(navArgument(Screens.Searching.PARAMETER_ROUTE_NAME) { type = NavType.StringType }),
        ) {
            SearchingScreen(
                navController = navController,
                onClickOnGuide = onClickGuide,
                goToTipPhoto = { tipPhoto, routeName ->
                    navController.navigate(Screens.TipPhoto.doRoute(tipPhoto, routeName))
                },
                goToResult = { routeName, resultType, treasureId, amount ->
                    navController.navigate(Screens.Results.doRoute(routeName, resultType, treasureId, amount))
                },
                goToMap = { navController.navigate(Screens.Map.doRoute(it)) },
                goToTreasureSelector = { treasureDescriptionId ->
                    navController.navigate(Screens.Selector.doRoute(treasureDescriptionId))
                },
                goToFacebook = goToFacebook,
                goToCommemorative = goToCommemorative,
            )
        }
        composable(
            route = Screens.Results.ROUTE,
            arguments = listOf(
                navArgument(Screens.Results.PARAMETER_ROUTE_NAME) { type = NavType.StringType },
                navArgument(Screens.Results.PARAMETER_RESULT_TYPE) { type = NavType.EnumType(ResultType::class.java) },
                navArgument(Screens.Results.PARAMETER_TREASURE_ID) { type = NavType.IntType },
                navArgument(Screens.Results.PARAMETER_TREASURE_AMOUNT) { type = NavType.IntType },
            )
        ) { navBackStackEntry -> ResultScreen(navController, navBackStackEntry, onClickGuide, goToFacebook) }
        composable(
            route = Screens.TipPhoto.ROUTE,
            arguments = listOf(
                navArgument(Screens.TipPhoto.PARAMETER_TIP_PHOTO) { type = NavType.StringType },
                navArgument(Screens.TipPhoto.PARAMETER_ROUTE_NAME) { type = NavType.StringType },
            )
        ) {
            TipPhotoScreen(
                navController = navController,
                onClickOnGuide = onClickGuide,
                onClickOnFacebook = goToFacebook
            )
        }
        composable(
            route = Screens.Map.ROUTE,
            arguments = listOf(navArgument(Screens.Map.PARAMETER_ROUTE_NAME) { type = NavType.StringType }),
        ) {
            MapScreen(
                navController = navController,
                onClickOnGuide = onClickGuide,
                onClickOnFacebook = goToFacebook
            )
        }
        composable(
            route = Screens.Selector.ROUTE,
            arguments = listOf(navArgument(Screens.Selector.PARAMETER_JUST_FOUND_TREASURE) { type = NavType.IntType }),
        ) { navBackStackEntry ->
            SelectorScreen(
                navController,
                navBackStackEntry,
                onClickGuide,
                goToResult = { },
                goToCommemorative = goToCommemorative,
                onClickOnFacebook = goToFacebook
            )
        }
        composable(
            route = Screens.Commemorative.ROUTE,
            arguments = listOf(navArgument(Screens.Commemorative.PARAMETER_TREASURE_DESCRIPTION_ID) {
                type = NavType.IntType
            })
        ) { navBackStackEntry -> CommemorativeScreen(navController, navBackStackEntry, onClickGuide, goToFacebook) }
        composable(
            route = Screens.Facebook.ROUTE,
            arguments = listOf(navArgument(Screens.Facebook.PARAMETER_ROUTE_NAME) {
                type = NavType.StringType
            }),
        ) { navBackStackEntry -> FacebookScreen(navController, onClickGuide) }
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
}
