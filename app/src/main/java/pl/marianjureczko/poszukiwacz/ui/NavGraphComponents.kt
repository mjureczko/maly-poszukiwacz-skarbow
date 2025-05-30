package pl.marianjureczko.poszukiwacz.ui

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import pl.marianjureczko.poszukiwacz.screen.Screens
import pl.marianjureczko.poszukiwacz.screen.commemorative.CommemorativeScreen
import pl.marianjureczko.poszukiwacz.screen.facebook.FacebookScreen
import pl.marianjureczko.poszukiwacz.screen.map.MapScreen
import pl.marianjureczko.poszukiwacz.screen.phototip.TipPhotoScreen
import pl.marianjureczko.poszukiwacz.screen.result.ResultScreen
import pl.marianjureczko.poszukiwacz.screen.result.ResultType
import pl.marianjureczko.poszukiwacz.screen.searching.SearchingScreen
import pl.marianjureczko.poszukiwacz.screen.treasureselector.SelectorScreen
import pl.marianjureczko.poszukiwacz.shared.GoToCommemorative
import pl.marianjureczko.poszukiwacz.shared.GoToFacebook
import pl.marianjureczko.poszukiwacz.shared.GoToGuide
import pl.marianjureczko.poszukiwacz.shared.GoToResultWithTreasure
import pl.marianjureczko.poszukiwacz.shared.PhotoHelper

fun getGoToCommemorative(navController: NavHostController): GoToCommemorative = { treasureId, photoPath ->
    navController.navigate(Screens.Commemorative.doRoute(treasureId, PhotoHelper.encodePhotoPath(photoPath)))
}

fun NavGraphBuilder.searching(
    navController: NavHostController,
    onClickGuide: GoToGuide,
    goToFacebook: GoToFacebook,
    goToCommemorative: GoToCommemorative
) {
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
}

fun NavGraphBuilder.selector(
    navController: NavHostController,
    onClickGuide: GoToGuide,
    goToCommemorative: GoToCommemorative,
    goToFacebook: GoToFacebook,
    goToResultsFromSelector: GoToResultWithTreasure,
) {
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
}

fun NavGraphBuilder.results(
    navController: NavHostController,
    onClickGuide: GoToGuide,
    goToFacebook: GoToFacebook
) {
    composable(
        route = Screens.Results.ROUTE,
        arguments = listOf(
            navArgument(Screens.Results.PARAMETER_ROUTE_NAME) { type = NavType.StringType },
            navArgument(Screens.Results.PARAMETER_RESULT_TYPE) { type = NavType.EnumType(ResultType::class.java) },
            navArgument(Screens.Results.PARAMETER_TREASURE_ID) { type = NavType.IntType },
            navArgument(Screens.Results.PARAMETER_TREASURE_AMOUNT) { type = NavType.IntType },
        )
    ) { navBackStackEntry -> ResultScreen(navController, navBackStackEntry, onClickGuide, goToFacebook) }
}

fun NavGraphBuilder.tipPhoto(
    navController: NavHostController,
    onClickGuide: GoToGuide,
    goToFacebook: GoToFacebook
) {
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
}

fun NavGraphBuilder.map(
    navController: NavHostController,
    onClickGuide: GoToGuide,
    goToFacebook: GoToFacebook
) {
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
}

fun NavGraphBuilder.commemorative(
    navController: NavHostController,
    onClickGuide: GoToGuide,
    goToFacebook: GoToFacebook
) {
    composable(
        route = Screens.Commemorative.ROUTE,
        arguments = listOf(
            navArgument(Screens.Commemorative.PARAMETER_TREASURE_DESCRIPTION_ID) { type = NavType.IntType },
            navArgument(Screens.Commemorative.PARAMETER_PHOTO_PATH) { type = NavType.StringType }
        )
    ) { navBackStackEntry -> CommemorativeScreen(navController, navBackStackEntry, onClickGuide, goToFacebook) }
}

fun NavGraphBuilder.facebook(
    navController: NavHostController,
    onClickGuide: GoToGuide
) {
    composable(
        route = Screens.Facebook.ROUTE,
        arguments = listOf(navArgument(Screens.Facebook.PARAMETER_ROUTE_NAME) { type = NavType.StringType }),
    ) { navBackStackEntry -> FacebookScreen(navController, onClickGuide) }
}
