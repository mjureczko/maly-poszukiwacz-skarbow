package pl.marianjureczko.poszukiwacz.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import pl.marianjureczko.poszukiwacz.screen.Screens
import pl.marianjureczko.poszukiwacz.screen.searching.SharedViewModel

@Composable
inline fun <reified T : SharedViewModel> getViewModel(viewModelStoreOwner: ViewModelStoreOwner): T {
    val sharedViewModel: SharedViewModel = hiltViewModel(viewModelStoreOwner)
    return sharedViewModel as T
}

//TODO t: use in getViewModel to make the calls from screens simpler
@Composable
fun shareViewModelStoreOwner(navBackStackEntry: NavBackStackEntry, navController: NavController): NavBackStackEntry {
    //https://stackoverflow.com/a/72336036/8471555
    return remember(navBackStackEntry) { navController.getBackStackEntry(Screens.Searching.ROUTE) }
}

@Composable
fun isOnStack(navController: NavController, route: String): Boolean {
    return navController.backQueue.any { it.destination.route == route }
}