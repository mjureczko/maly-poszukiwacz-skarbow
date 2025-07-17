package pl.marianjureczko.poszukiwacz.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import pl.marianjureczko.poszukiwacz.screen.Screens
import pl.marianjureczko.poszukiwacz.screen.searching.SharedViewModel

/**
 * Retrieves the `SharedViewModel` instance originally created for the searching screen.
 *
 * This function traverses the navigation stack to find the `NavBackStackEntry`
 * associated with the searching screen (`Screens.Searching.ROUTE`), so that
 * the shared ViewModel is always tied to the searching screen, regardless of
 * the current screen in the navigation flow.
 */
@Composable
inline fun <reified T : SharedViewModel> getSharedViewModel(
    navBackStackEntry: NavBackStackEntry,
    navController: NavController
): T {
    val viewModelStoreOwner: ViewModelStoreOwner = shareViewModelStoreOwner(navBackStackEntry, navController)
    val sharedViewModel: SharedViewModel = hiltViewModel(viewModelStoreOwner)
    return sharedViewModel as T
}

@Composable
fun shareViewModelStoreOwner(navBackStackEntry: NavBackStackEntry, navController: NavController): NavBackStackEntry {
    //https://stackoverflow.com/a/72336036/8471555
    return remember(navBackStackEntry) { navController.getBackStackEntry(Screens.Searching.ROUTE) }
}

@Composable
fun isOnStack(navController: NavController, route: String): Boolean {
    return navController.backQueue.any { it.destination.route == route }
}