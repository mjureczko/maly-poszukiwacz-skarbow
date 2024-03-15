package pl.marianjureczko.poszukiwacz.activity.main

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
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
import pl.marianjureczko.poszukiwacz.activity.result.n.PARAMETER_RESULT_TYPE
import pl.marianjureczko.poszukiwacz.activity.result.n.ResultScreen
import pl.marianjureczko.poszukiwacz.activity.result.n.ResultType
import pl.marianjureczko.poszukiwacz.activity.searching.n.PARAMETER_ROUTE_NAME
import pl.marianjureczko.poszukiwacz.activity.searching.n.SearchingScreen
import pl.marianjureczko.poszukiwacz.shared.Settings
import pl.marianjureczko.poszukiwacz.ui.Screen
import pl.marianjureczko.poszukiwacz.ui.theme.AppTheme
import javax.inject.Inject

/**
 * Routes creation and selection activity
 */
//TODO: check https://developer.android.com/build/build-variants and Product Flavours
@AndroidEntryPoint
class MainActivity : PermissionActivity() {

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

    fun onClickOnGuide() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(resources.getString(R.string.help_path))))
    }

}

@Composable
private fun ComposeRoot(settings: Settings, resources: Resources, onClickOnGuide: () -> Unit) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "main") {
        composable(route = "main") {
            MainScreen(navController, settings.isClassicMode(), resources, onClickOnGuide) { routeName ->
                navController.navigate("searching/$routeName")
            }
        }
        composable(
            route = "searching/{$PARAMETER_ROUTE_NAME}",
            arguments = listOf(navArgument(PARAMETER_ROUTE_NAME) { type = NavType.StringType }),
//            deepLinks = listOf(navDeepLink { uriPattern = "www.restaurantsapp.details.com/{restaurant_id}" }),
        ) { SearchingScreen(navController, settings.isClassicMode(), resources, onClickOnGuide) {
            navController.navigate("result/$it")
        } }
        composable(
            route = "result/{$PARAMETER_RESULT_TYPE}",
            arguments = listOf(navArgument(PARAMETER_RESULT_TYPE) { type = NavType.EnumType(ResultType::class.java) })
        ) { ResultScreen(navController, resources, onClickOnGuide) }

    }
}