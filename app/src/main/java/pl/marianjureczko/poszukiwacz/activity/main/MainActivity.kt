package pl.marianjureczko.poszukiwacz.activity.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pl.marianjureczko.poszukiwacz.App
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.ui.theme.AppTheme
import pl.marianjureczko.poszukiwacz.shared.Settings

/**
 * Routes creation and selection activity
 */
class MainActivity : ComponentActivity() {//PermissionActivity() {

//    private val TAG = javaClass.simpleName
//    override fun getTreasureProgress(): TreasuresProgress? {
//        return null
//    }
//
//    private val storageHelper: StorageHelper by lazy { StorageHelper(this) }
//    private lateinit var routesRecyclerView: RecyclerView
//    private lateinit var binding: ActivityMainBinding
//
//    override fun onPermissionsGranted(activityRequirements: ActivityRequirements) {
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
//        val isClassic = settings.isClassicMode()

        setContent {
            AppTheme {
                HunterApp { onClickOnGuide() }
            }
        }

//        binding = ActivityMainBinding.inflate(layoutInflater)
//        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//        setContentView(R.layout.activity_main)
//        assurePermissionsAreGranted(RequirementsForNavigation, true)
//
//        routesRecyclerView = binding.routes
//        routesRecyclerView.layoutManager = LinearLayoutManager(this)
//
//        val routes = storageHelper.loadAll()
//        showRoutes(routes)
//
//        binding.newRouteButton.setOnClickListener {
//            startActivity(TreasuresEditorActivity.intent(this))
//        }
//        binding.routeFromBluetoothButton.setOnClickListener {
//            fetchRouteFromBluetooth()
//        }
//        setTitle(R.string.main_activity_title)
//        setContentView(binding.root)
//
//        setUpAds(binding.adView)
    }

    fun onClickOnGuide() {
        val url = App.getResources().getString(R.string.help_path)
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

//    override fun onResume() {
//        super.onResume()
//        val routes = storageHelper.loadAll()
//        showRoutes(routes)
//        if (routes.isNotEmpty()) {
//            binding.noRoutes.visibility = View.GONE
//        }
//    }
//
//    private fun showRoutes(routes: MutableList<Route>) {
//        val routeAdapter = RouteAdapter(this, routes, storageHelper)
//        routesRecyclerView.adapter = routeAdapter
//    }
//
//    private fun fetchRouteFromBluetooth() =
//        startActivity(BluetoothActivity.intent(this, BluetoothActivity.Mode.ACCEPTING, null))
}

@Composable
private fun HunterApp(onClickOnGuide: () -> Unit) {
    val settings = Settings(App.getAppContext().assets)
    val navController = rememberNavController()
    NavHost(navController, startDestination = "main") {
        composable(route = "main") {
            MainScreen(settings.isClassicMode(), onClickOnGuide) //{ id -> navController.navigate("restaurants/$id") }
        }
//        composable(
//            route = "restaurants/{restaurant_id}",
//            arguments = listOf(navArgument("restaurant_id") { type = NavType.IntType }),
//            deepLinks = listOf(navDeepLink { uriPattern = "www.restaurantsapp.details.com/{restaurant_id}" }),
//        ) { RestaurantDetailsScreen() }
    }
}