package pl.marianjureczko.poszukiwacz.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import pl.marianjureczko.poszukiwacz.activity.commemorative.n.CommemorativeScreen
import pl.marianjureczko.poszukiwacz.activity.commemorative.n.PARAMETER_TREASURE_DESCRIPTION_ID
import pl.marianjureczko.poszukiwacz.activity.main.COMMEMORATIVE_PATH
import pl.marianjureczko.poszukiwacz.activity.main.COMMEMORATIVE_ROUTE
import pl.marianjureczko.poszukiwacz.activity.main.FACEBOOK_ROUTE
import pl.marianjureczko.poszukiwacz.activity.main.RESULTS_PATH
import pl.marianjureczko.poszukiwacz.activity.main.RESULTS_ROUTE
import pl.marianjureczko.poszukiwacz.activity.main.SEARCHING_PATH
import pl.marianjureczko.poszukiwacz.activity.main.SEARCHING_ROUTE
import pl.marianjureczko.poszukiwacz.activity.main.SELECTOR_PATH
import pl.marianjureczko.poszukiwacz.activity.main.SELECTOR_ROUTE
import pl.marianjureczko.poszukiwacz.activity.main.TREASURE_EDITOR_PATH
import pl.marianjureczko.poszukiwacz.activity.main.TREASURE_EDITOR_ROUTE
import pl.marianjureczko.poszukiwacz.activity.map.n.MapScreen
import pl.marianjureczko.poszukiwacz.activity.map.n.PARAMETER_ROUTE_NAME_2
import pl.marianjureczko.poszukiwacz.activity.searching.n.PARAMETER_ROUTE_NAME
import pl.marianjureczko.poszukiwacz.activity.searching.n.SearchingScreen
import pl.marianjureczko.poszukiwacz.screen.bluetooth.BluetoothScreen
import pl.marianjureczko.poszukiwacz.screen.bluetooth.Mode
import pl.marianjureczko.poszukiwacz.screen.bluetooth.PARAMETER_MODE
import pl.marianjureczko.poszukiwacz.screen.bluetooth.PARAMETER_ROUTE_TO_SENT
import pl.marianjureczko.poszukiwacz.screen.facebook.FacebookHelper
import pl.marianjureczko.poszukiwacz.screen.facebook.FacebookScreen
import pl.marianjureczko.poszukiwacz.screen.main.MainScreen
import pl.marianjureczko.poszukiwacz.screen.phototip.PARAMETER_TIP_PHOTO
import pl.marianjureczko.poszukiwacz.screen.phototip.TipPhotoScreen
import pl.marianjureczko.poszukiwacz.screen.result.PARAMETER_RESULT_TYPE
import pl.marianjureczko.poszukiwacz.screen.result.PARAMETER_TREASURE_AMOUNT
import pl.marianjureczko.poszukiwacz.screen.result.PARAMETER_TREASURE_ID
import pl.marianjureczko.poszukiwacz.screen.result.ResultScreen
import pl.marianjureczko.poszukiwacz.screen.result.ResultType
import pl.marianjureczko.poszukiwacz.screen.treasureseditor.TreasureEditorScreen
import pl.marianjureczko.poszukiwacz.screen.treasureselector.PARAMETER_JUST_FOUND_TREASURE
import pl.marianjureczko.poszukiwacz.screen.treasureselector.SelectorScreen
import pl.marianjureczko.poszukiwacz.shared.GoToCommemorative
import pl.marianjureczko.poszukiwacz.shared.GoToFacebook
import pl.marianjureczko.poszukiwacz.shared.GoToGuide
import pl.marianjureczko.poszukiwacz.shared.GoToTreasureEditor


val BLUETOOTH_PATH = "bluetooth"
val BLUETOOTH_ROUTE = "$BLUETOOTH_PATH/{$PARAMETER_MODE}/{$PARAMETER_ROUTE_TO_SENT}"

@Composable
fun ComposeRoot(onClickGuide: GoToGuide) {
    val navController = rememberNavController()
    val goToFacebook: GoToFacebook = FacebookHelper.createFacebookCallback(navController)
    val goToCommemorative: GoToCommemorative =
        { treasureId, photoPath -> navController.navigate("$COMMEMORATIVE_PATH/$treasureId/$photoPath") }
    val goToEditor: GoToTreasureEditor = { routeName -> navController.navigate("$TREASURE_EDITOR_PATH/$routeName") }

    NavHost(navController, startDestination = "main") {
        composable(route = "main") {
            MainScreen(
                navController = navController,
                onClickOnGuide = onClickGuide,
                onClickOnFacebook = goToFacebook,
                goToBluetooth = { mode: Mode, route: String -> navController.navigate("$BLUETOOTH_PATH/$mode/$route") },
                goToTreasureEditor = goToEditor,
            ) { routeName ->
                navController.navigate("$SEARCHING_PATH/$routeName")
            }
        }
        composable(
            route = TREASURE_EDITOR_ROUTE,
            arguments = listOf(navArgument(PARAMETER_ROUTE_NAME) { type = NavType.StringType })
        ) {
            TreasureEditorScreen(
                navController = navController,
                onClickOnGuide = onClickGuide,
                onClickOnFacebook = goToFacebook
            )
        }
        composable(
            route = SEARCHING_ROUTE,
            arguments = listOf(navArgument(PARAMETER_ROUTE_NAME) { type = NavType.StringType }),
        ) {
            SearchingScreen(
                navController = navController,
                onClickOnGuide = onClickGuide,
                goToTipPhoto = { tipPhoto, routeName -> navController.navigate("tipPhoto/$tipPhoto/$routeName") },
                goToResult = { routeName, resultType, treasureId, amount -> navController.navigate("$RESULTS_PATH/$routeName/$resultType/$treasureId/$amount") },
                goToMap = { navController.navigate("map/$it") },
                goToTreasureSelector = { treasureDescriptionId -> navController.navigate("$SELECTOR_PATH/$treasureDescriptionId") },
                goToFacebook = goToFacebook,
                goToCommemorative = goToCommemorative,
            )
        }
        composable(
            route = RESULTS_ROUTE,
            arguments = listOf(
                navArgument(pl.marianjureczko.poszukiwacz.screen.result.PARAMETER_ROUTE_NAME) {
                    type = NavType.StringType
                },
                navArgument(PARAMETER_RESULT_TYPE) { type = NavType.EnumType(ResultType::class.java) },
                navArgument(PARAMETER_TREASURE_ID) { type = NavType.IntType },
                navArgument(PARAMETER_TREASURE_AMOUNT) { type = NavType.IntType },
            )
        ) { navBackStackEntry -> ResultScreen(navController, navBackStackEntry, onClickGuide, goToFacebook) }
        composable(
            route = "tipPhoto/{$PARAMETER_TIP_PHOTO}/{${pl.marianjureczko.poszukiwacz.screen.phototip.PARAMETER_ROUTE_NAME}}",
            arguments = listOf(
                navArgument(PARAMETER_TIP_PHOTO) { type = NavType.StringType },
                navArgument(pl.marianjureczko.poszukiwacz.screen.phototip.PARAMETER_ROUTE_NAME) {
                    type = NavType.StringType
                },
            )
        ) {
            TipPhotoScreen(
                navController = navController,
                onClickOnGuide = onClickGuide,
                onClickOnFacebook = goToFacebook
            )
        }
        composable(
            route = "map/{$PARAMETER_ROUTE_NAME_2}",
            arguments = listOf(navArgument(PARAMETER_ROUTE_NAME) { type = NavType.StringType }),
        ) {
            MapScreen(
                navController = navController,
                onClickOnGuide = onClickGuide,
                onClickOnFacebook = goToFacebook
            )
        }
        composable(
            route = SELECTOR_ROUTE,
            arguments = listOf(navArgument(PARAMETER_JUST_FOUND_TREASURE) { type = NavType.IntType }),
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
            route = COMMEMORATIVE_ROUTE,
            arguments = listOf(navArgument(PARAMETER_TREASURE_DESCRIPTION_ID) { type = NavType.IntType })
        ) { navBackStackEntry -> CommemorativeScreen(navController, navBackStackEntry, onClickGuide, goToFacebook) }
        composable(
            route = FACEBOOK_ROUTE,
            arguments = listOf(navArgument(pl.marianjureczko.poszukiwacz.screen.facebook.PARAMETER_ROUTE_NAME) {
                type = NavType.StringType
            }),
        ) { navBackStackEntry -> FacebookScreen(navController, onClickGuide) }
        composable(
            route = BLUETOOTH_ROUTE,
            arguments = listOf(
                navArgument(PARAMETER_MODE) { type = NavType.EnumType(Mode::class.java) },
                navArgument(PARAMETER_ROUTE_TO_SENT) { type = NavType.StringType },
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
