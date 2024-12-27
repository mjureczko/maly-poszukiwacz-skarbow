package pl.marianjureczko.poszukiwacz.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import pl.marianjureczko.poszukiwacz.activity.commemorative.n.CommemorativeScreen
import pl.marianjureczko.poszukiwacz.activity.commemorative.n.PARAMETER_TREASURE_DESCRIPTION_ID
import pl.marianjureczko.poszukiwacz.activity.facebook.n.FacebookHelper
import pl.marianjureczko.poszukiwacz.activity.facebook.n.FacebookScreen
import pl.marianjureczko.poszukiwacz.activity.main.COMMEMORATIVE_PATH
import pl.marianjureczko.poszukiwacz.activity.main.COMMEMORATIVE_ROUTE
import pl.marianjureczko.poszukiwacz.activity.main.FACEBOOK_ROUTE
import pl.marianjureczko.poszukiwacz.activity.main.RESULTS_PATH
import pl.marianjureczko.poszukiwacz.activity.main.RESULTS_ROUTE
import pl.marianjureczko.poszukiwacz.activity.main.SEARCHING_PATH
import pl.marianjureczko.poszukiwacz.activity.main.SEARCHING_ROUTE
import pl.marianjureczko.poszukiwacz.activity.main.SELECTOR_PATH
import pl.marianjureczko.poszukiwacz.activity.main.SELECTOR_ROUTE
import pl.marianjureczko.poszukiwacz.activity.map.n.MapScreen
import pl.marianjureczko.poszukiwacz.activity.map.n.PARAMETER_ROUTE_NAME_2
import pl.marianjureczko.poszukiwacz.activity.photo.n.PARAMETER_TIP_PHOTO
import pl.marianjureczko.poszukiwacz.activity.photo.n.TipPhotoScreen
import pl.marianjureczko.poszukiwacz.screen.result.PARAMETER_RESULT_TYPE
import pl.marianjureczko.poszukiwacz.screen.result.PARAMETER_TREASURE_ID
import pl.marianjureczko.poszukiwacz.screen.result.PARAMETER_TREASURE_QUANTITY
import pl.marianjureczko.poszukiwacz.screen.result.ResultScreen
import pl.marianjureczko.poszukiwacz.screen.result.ResultType
import pl.marianjureczko.poszukiwacz.activity.searching.n.PARAMETER_ROUTE_NAME
import pl.marianjureczko.poszukiwacz.activity.searching.n.SearchingScreen
import pl.marianjureczko.poszukiwacz.activity.treasureselector.n.PARAMETER_JUST_FOUND_TREASURE
import pl.marianjureczko.poszukiwacz.activity.treasureselector.n.SelectorScreen
import pl.marianjureczko.poszukiwacz.screen.main.MainScreen
import pl.marianjureczko.poszukiwacz.shared.GoToCommemorative
import pl.marianjureczko.poszukiwacz.shared.GoToFacebook
import pl.marianjureczko.poszukiwacz.shared.GoToGuide

@Composable
fun ComposeRoot(onClickGuide: GoToGuide) {
    val navController = rememberNavController()
    val goToFacebook: GoToFacebook = FacebookHelper.createFacebookCallback(navController)
    val goToCommemorative: GoToCommemorative = { treasureId -> navController.navigate("$COMMEMORATIVE_PATH/$treasureId") }

    NavHost(navController, startDestination = "main") {
        composable(route = "main") {
            MainScreen(navController, onClickGuide, goToFacebook) { routeName ->
                navController.navigate("$SEARCHING_PATH/$routeName")
            }
        }
        composable(
            route = SEARCHING_ROUTE,
            arguments = listOf(navArgument(PARAMETER_ROUTE_NAME) { type = NavType.StringType }),
        ) {
            SearchingScreen(
                navController = navController,
                onClickOnGuide = onClickGuide,
                goToTipPhoto = { navController.navigate("tipPhoto/$it") },
                goToResult = { resultType, treasureId, amount -> navController.navigate("$RESULTS_PATH/$resultType/$treasureId/$amount") },
                goToMap = { navController.navigate("map/$it") },
                goToTreasureSelector = { navController.navigate("$SELECTOR_PATH/$it") },
                goToFacebook = goToFacebook,
                goToCommemorative = goToCommemorative,
            )
        }
        composable(
            route = RESULTS_ROUTE,
            arguments = listOf(
                navArgument(PARAMETER_RESULT_TYPE) { type = NavType.EnumType(ResultType::class.java) },
                navArgument(PARAMETER_TREASURE_ID) { type = NavType.IntType },
                navArgument(PARAMETER_TREASURE_AMOUNT) { type = NavType.IntType },
            )
        ) { navBackStackEntry -> ResultScreen(navController, navBackStackEntry, onClickGuide, goToFacebook) }
        composable(
            route = "tipPhoto/{$PARAMETER_TIP_PHOTO}",
            arguments = listOf(navArgument(PARAMETER_TIP_PHOTO) { type = NavType.StringType })
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
                //TODO t: doesn't support classic
                goToResult = { treasureId -> navController.navigate("$RESULTS_PATH/${ResultType.TREASURE}/$treasureId") },
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
        ) { navBackStackEntry -> FacebookScreen(navController, onClickGuide) }
    }
}