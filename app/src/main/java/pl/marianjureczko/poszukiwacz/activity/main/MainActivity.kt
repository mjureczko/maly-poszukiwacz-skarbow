package pl.marianjureczko.poszukiwacz.activity.main

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.activity.commemorative.n.CommemorativeScreen
import pl.marianjureczko.poszukiwacz.activity.commemorative.n.PARAMETER_TREASURE_DESCRIPTION_ID
import pl.marianjureczko.poszukiwacz.activity.map.n.MapScreen
import pl.marianjureczko.poszukiwacz.activity.map.n.PARAMETER_ROUTE_NAME_2
import pl.marianjureczko.poszukiwacz.activity.photo.n.PARAMETER_TIP_PHOTO
import pl.marianjureczko.poszukiwacz.activity.photo.n.TipPhotoScreen
import pl.marianjureczko.poszukiwacz.activity.result.n.PARAMETER_RESULT_TYPE
import pl.marianjureczko.poszukiwacz.activity.result.n.PARAMETER_TREASURE_ID
import pl.marianjureczko.poszukiwacz.activity.result.n.ResultScreen
import pl.marianjureczko.poszukiwacz.activity.result.n.ResultType
import pl.marianjureczko.poszukiwacz.activity.searching.n.PARAMETER_ROUTE_NAME
import pl.marianjureczko.poszukiwacz.activity.searching.n.SearchingScreen
import pl.marianjureczko.poszukiwacz.activity.treasureselector.n.PARAMETER_JUST_FOUND_TREASURE
import pl.marianjureczko.poszukiwacz.activity.treasureselector.n.SelectorScreen
import pl.marianjureczko.poszukiwacz.shared.Settings
import pl.marianjureczko.poszukiwacz.ui.Screen
import pl.marianjureczko.poszukiwacz.ui.theme.AppTheme
import javax.inject.Inject

val SEARCHING_PATH = "searching"
val SEARCHING_ROUTE = "$SEARCHING_PATH/{$PARAMETER_ROUTE_NAME}"
val RESULTS_PATH = "result"
val RESULTS_ROUTE = "$RESULTS_PATH/{$PARAMETER_RESULT_TYPE}/{$PARAMETER_TREASURE_ID}"
val SELECTOR_PATH = "selector"
val SELECTOR_ROUTE = "$SELECTOR_PATH/{$PARAMETER_JUST_FOUND_TREASURE}"
val COMMEMORATIVE_PATH = "commemorative"
val COMMEMORATIVE_ROUTE = "$COMMEMORATIVE_PATH/{$PARAMETER_TREASURE_DESCRIPTION_ID}"

/**
 * Routes creation and selection activity
 */
//TODO: check https://developer.android.com/build/build-variants and Product Flavours
@AndroidEntryPoint
class MainActivity : PermissionActivity() {
    private val TAG = javaClass.simpleName
    @Inject
    lateinit var settings: Settings

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        Screen.init(this)

        setContent {
            AppTheme {
                ComposeRoot(settings, resources) { onClickOnGuide() }
            }
        }
    }

    override fun onPause() {
        Log.i(TAG, "onPause")
        super.onPause()
    }

    override fun onResume() {
        Log.i(TAG, "onResume")
        super.onResume()
    }

    override fun onStop() {
        Log.i(TAG, "onStop")
        super.onStop()
    }

    override fun onDestroy() {
        Log.i(TAG, "onDestroy")
        super.onDestroy()
    }

    fun onClickOnGuide() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(resources.getString(R.string.help_path))))
    }

}

@Composable
private fun ComposeRoot(settings: Settings, resources: Resources, onClickGuide: () -> Unit) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "main") {
        composable(route = "main") {
            MainScreen(navController, settings.isClassicMode(), resources, onClickGuide) { routeName ->
                navController.navigate("$SEARCHING_PATH/$routeName")
            }
        }
        composable(
            route = SEARCHING_ROUTE,
            arguments = listOf(navArgument(PARAMETER_ROUTE_NAME) { type = NavType.StringType }),
//            deepLinks = listOf(navDeepLink { uriPattern = "www.restaurantsapp.details.com/{restaurant_id}" }),
        ) {
            SearchingScreen(
                navController = navController,
                isClassicMode = settings.isClassicMode(),
                resources = resources,
                onClickOnGuide = onClickGuide,
                goToTipPhoto = { navController.navigate("tipPhoto/$it") },
                goToResult = { resultType, treasureId -> navController.navigate("$RESULTS_PATH/$resultType/$treasureId") },
                goToMap = { navController.navigate("map/$it") },
                goToTreasureSelector = { navController.navigate("$SELECTOR_PATH/$it") }
            )
        }
        composable(
            route = RESULTS_ROUTE,
            arguments = listOf(
                navArgument(PARAMETER_RESULT_TYPE) { type = NavType.EnumType(ResultType::class.java) },
                navArgument(PARAMETER_TREASURE_ID) { type = NavType.IntType }
            )
        ) { navBackStackEntry -> ResultScreen(navController, navBackStackEntry, onClickGuide) }
        composable(
            route = "tipPhoto/{$PARAMETER_TIP_PHOTO}",
            arguments = listOf(navArgument(PARAMETER_TIP_PHOTO) { type = NavType.StringType })
        ) {
            TipPhotoScreen(navController = navController, onClickOnGuide = onClickGuide)
        }
        composable(
            route = "map/{$PARAMETER_ROUTE_NAME_2}",
            arguments = listOf(navArgument(PARAMETER_ROUTE_NAME) { type = NavType.StringType }),
        ) {
            MapScreen(navController = navController, onClickOnGuide = onClickGuide, resources = resources)
        }
        composable(
            route = SELECTOR_ROUTE,
            arguments = listOf(navArgument(PARAMETER_JUST_FOUND_TREASURE) { type = NavType.IntType }),
        ) { navBackStackEntry -> SelectorScreen(
            navController,
            navBackStackEntry,
            resources,
            onClickGuide,
            goToResult = { treasureId -> navController.navigate("$RESULTS_PATH/${ResultType.TREASURE}/$treasureId") },
            goToCommemorative = {treasureId -> navController.navigate("$COMMEMORATIVE_PATH/$treasureId")}
        ) }
        composable(
            route = COMMEMORATIVE_ROUTE,
            arguments = listOf(navArgument(PARAMETER_TREASURE_DESCRIPTION_ID) { type = NavType.IntType })
        ) { navBackStackEntry -> CommemorativeScreen(navController, navBackStackEntry, onClickGuide) }
    }
}