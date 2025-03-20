package pl.marianjureczko.poszukiwacz.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import pl.marianjureczko.poszukiwacz.screen.Screens

@Composable
fun shareViewModelStoreOwner(navBackStackEntry: NavBackStackEntry, navController: NavController): NavBackStackEntry {
    //https://stackoverflow.com/a/72336036/8471555
    return remember(navBackStackEntry) { navController.getBackStackEntry(Screens.Searching.ROUTE) }
}

@Composable
fun isOnStack( navController: NavController, route: String): Boolean {
    return navController.backQueue.any { it.destination.route == route }
}