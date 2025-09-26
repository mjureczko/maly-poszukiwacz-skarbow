package pl.marianjureczko.poszukiwacz.screen.main

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.permissions.RequirementsForNavigation
import pl.marianjureczko.poszukiwacz.shared.GoToFacebook
import pl.marianjureczko.poszukiwacz.shared.GoToSearching
import pl.marianjureczko.poszukiwacz.ui.components.MenuConfig
import pl.marianjureczko.poszukiwacz.ui.components.TopBar
import pl.marianjureczko.poszukiwacz.ui.handlePermissionWithExitOnDenied

@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(
    navController: NavController,
    onClickOnGuide: () -> Unit,
    onClickOnFacebook: GoToFacebook,
    goToSearching: GoToSearching
) {
    val isInPreview = LocalInspectionMode.current
    if (!isInPreview) {
        handlePermissionWithExitOnDenied(RequirementsForNavigation)
    }
    Scaffold(
        topBar = {
            TopBar(
                navController = navController,
                title = stringResource(R.string.app_name),
                menuConfig = MenuConfig(onClickOnGuide),
            )
        },
        content = { _ -> MainScreenBody(goToSearching) }
    )
}
