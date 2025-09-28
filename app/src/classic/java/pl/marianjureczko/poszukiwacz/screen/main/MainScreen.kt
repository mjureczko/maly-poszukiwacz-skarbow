package pl.marianjureczko.poszukiwacz.screen.main

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.permissions.RequirementsForNavigation
import pl.marianjureczko.poszukiwacz.shared.GoToBluetooth
import pl.marianjureczko.poszukiwacz.shared.GoToFacebook
import pl.marianjureczko.poszukiwacz.shared.GoToSearching
import pl.marianjureczko.poszukiwacz.shared.GoToTreasureEditor
import pl.marianjureczko.poszukiwacz.ui.components.MenuConfig
import pl.marianjureczko.poszukiwacz.ui.components.TopBar
import pl.marianjureczko.poszukiwacz.ui.handlePermissionWithExitOnDenied

@Composable
fun MainScreen(
    navController: NavController,
    onClickOnGuide: () -> Unit,
    onClickOnFacebook: GoToFacebook,
    goToBluetooth: GoToBluetooth,
    goToTreasureEditor: GoToTreasureEditor,
    goToSearching: GoToSearching
) {
    val isInPreview = LocalInspectionMode.current
    if (!isInPreview) {
        handlePermissionWithExitOnDenied(RequirementsForNavigation)
    }
    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            TopBar(
                navController = navController,
                title = stringResource(R.string.app_name),
                menuConfig = MenuConfig(onClickOnGuide),
            )
        },
        content = { paddingValues ->
            MainScreenBody(
                goToBluetooth,
                goToTreasureEditor,
                goToSearching,
                Modifier.padding(paddingValues)
            )
        }
    )
}