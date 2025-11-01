package pl.marianjureczko.poszukiwacz.screen.badges

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.permissions.RequirementsToExternalStorage
import pl.marianjureczko.poszukiwacz.shared.GoToGuide
import pl.marianjureczko.poszukiwacz.ui.components.TopBar
import pl.marianjureczko.poszukiwacz.ui.handlePermission

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BadgesScreen(
    navController: NavController,
    onClickOnGuide: GoToGuide,
) {
    val viewModel: BadgesViewModel = hiltViewModel()
    handlePermission(RequirementsToExternalStorage)

    Scaffold(
        topBar = {
            TopBar(
                navController = navController,
                title = stringResource(id = R.string.achievements),
                menuConfig = badgesMenuConfig(onClickOnGuide),
            )
        },
        content = { paddingValues ->
            BadgesScreenBody(
                modifier = Modifier.padding(paddingValues),
                state = viewModel.state.value,
            )
        }
    )
}
