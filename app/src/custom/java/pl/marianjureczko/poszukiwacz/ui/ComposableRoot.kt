package pl.marianjureczko.poszukiwacz.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import pl.marianjureczko.poszukiwacz.screen.Screens
import pl.marianjureczko.poszukiwacz.screen.commemorative.CommemorativeScreen
import pl.marianjureczko.poszukiwacz.screen.facebook.FacebookHelper
import pl.marianjureczko.poszukiwacz.screen.facebook.FacebookScreen
import pl.marianjureczko.poszukiwacz.screen.main.CustomInitializerForRoute
import pl.marianjureczko.poszukiwacz.screen.main.MainScreen
import pl.marianjureczko.poszukiwacz.screen.map.MapScreen
import pl.marianjureczko.poszukiwacz.screen.phototip.TipPhotoScreen
import pl.marianjureczko.poszukiwacz.screen.result.ResultScreen
import pl.marianjureczko.poszukiwacz.screen.result.ResultType
import pl.marianjureczko.poszukiwacz.screen.searching.SearchingScreen
import pl.marianjureczko.poszukiwacz.screen.treasureselector.SelectorScreen
import pl.marianjureczko.poszukiwacz.shared.GoToCommemorative
import pl.marianjureczko.poszukiwacz.shared.GoToFacebook
import pl.marianjureczko.poszukiwacz.shared.GoToGuide
import pl.marianjureczko.poszukiwacz.shared.PhotoHelper

@Composable
fun ComposeRoot(onClickGuide: GoToGuide) {
    val navController = rememberNavController()
    val goToFacebook: GoToFacebook = FacebookHelper.createFacebookCallback(navController)
    val goToCommemorative: GoToCommemorative =
        { treasureId, photoPath ->
            navController.navigate(
                Screens.Commemorative.doRoute(treasureId, PhotoHelper.encodePhotoPath(photoPath))
            )
        }

    NavHost(navController, startDestination = "main") {
        composable(route = "main") {
            MainScreen(navController, onClickGuide, goToFacebook) { routeName ->
                navController.navigate(Screens.Searching.doRoute(routeName))
            }
        }
        composable(
            route = Screens.Searching.ROUTE,
            arguments = listOf(navArgument(Screens.Searching.PARAMETER_ROUTE_NAME) { type = NavType.StringType }),
        ) {
            SearchingScreen(
                navController = navController,
                onClickOnGuide = onClickGuide,
                goToTipPhoto = { tipPhoto, _ ->
                    navController.navigate(Screens.TipPhoto.doRoute(tipPhoto, CustomInitializerForRoute.routeName))
                },
                goToResult = { routeName, resultType, treasureId, amount ->
                    navController.navigate(Screens.Results.doRoute(routeName, resultType, treasureId, amount))
                },
                goToMap = { navController.navigate(Screens.Map.doRoute(it)) },
                goToTreasureSelector = { navController.navigate(Screens.Selector.doRoute(it)) },
                goToFacebook = goToFacebook,
                goToCommemorative = goToCommemorative,
            )
        }
        composable(
            route = Screens.Results.ROUTE,
            arguments = listOf(
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
                goToResult = { treasureId ->
                    navController.navigate(
                        Screens.Results.doRoute(
                            CustomInitializerForRoute.routeName,
                            ResultType.KNOWLEDGE,
                            treasureId,
                            1
                        )
                    )
                },
                goToCommemorative = goToCommemorative,
                onClickOnFacebook = goToFacebook
            )
        }
        composable(
            route = Screens.Commemorative.ROUTE,
            arguments = listOf(
                navArgument(Screens.Commemorative.PARAMETER_TREASURE_DESCRIPTION_ID) { type = NavType.IntType },
                navArgument(Screens.Commemorative.PARAMETER_PHOTO_PATH) { type = NavType.StringType },
            )
        ) { navBackStackEntry -> CommemorativeScreen(navController, navBackStackEntry, onClickGuide, goToFacebook) }
        composable(
            route = Screens.Facebook.ROUTE,
        ) { _ -> FacebookScreen(navController, onClickGuide) }
    }
}