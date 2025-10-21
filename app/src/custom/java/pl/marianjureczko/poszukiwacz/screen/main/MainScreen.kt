package pl.marianjureczko.poszukiwacz.screen.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.permissions.RequirementsForNavigation
import pl.marianjureczko.poszukiwacz.shared.GoToSearching
import pl.marianjureczko.poszukiwacz.ui.components.GoToBadgesScreen
import pl.marianjureczko.poszukiwacz.ui.components.TopBar
import pl.marianjureczko.poszukiwacz.ui.handlePermissionWithExitOnDenied

@Composable
fun MainScreen(
    navController: NavController,
    onClickOnGuide: () -> Unit,
    onClickBadges: GoToBadgesScreen,
    goToSearching: GoToSearching,
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
                menuConfig = mainMenuConfig(onClickOnGuide, onClickBadges),
            )
        },
        content = { paddingValues -> MainScreenBody(Modifier.padding(paddingValues), goToSearching) }
    )
}
