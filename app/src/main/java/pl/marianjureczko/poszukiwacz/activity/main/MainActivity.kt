package pl.marianjureczko.poszukiwacz.activity.main

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.window.layout.WindowMetricsCalculator
import pl.marianjureczko.poszukiwacz.App
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.activity.searching.SearchingScreen
import pl.marianjureczko.poszukiwacz.ui.theme.AppTheme
import pl.marianjureczko.poszukiwacz.shared.Settings
import pl.marianjureczko.poszukiwacz.ui.Screen

/**
 * Routes creation and selection activity
 */
class MainActivity : ComponentActivity() {

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        Screen.init(this)

        setContent {
            AppTheme {
                HunterApp { onClickOnGuide() }
            }
        }
    }

    fun onClickOnGuide() {
        val url = App.getResources().getString(R.string.help_path)
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}

@Composable
private fun HunterApp(onClickOnGuide: () -> Unit) {
    val settings = Settings(App.getAppContext().assets)
    val navController = rememberNavController()
    NavHost(navController, startDestination = "main") {
        composable(route = "main") {
            MainScreen(settings.isClassicMode(), onClickOnGuide) { routeName ->
                navController.navigate("searching/$routeName")
            }
        }
        composable(
            route = "searching/{route_name}",
            arguments = listOf(navArgument("route_name") { type = NavType.StringType }),
//            deepLinks = listOf(navDeepLink { uriPattern = "www.restaurantsapp.details.com/{restaurant_id}" }),
        ) { SearchingScreen(onClickOnGuide) }
    }
}