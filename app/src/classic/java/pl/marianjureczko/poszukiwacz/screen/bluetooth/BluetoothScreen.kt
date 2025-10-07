@file:OptIn(ExperimentalPermissionsApi::class)

package pl.marianjureczko.poszukiwacz.screen.bluetooth

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.shared.GoToGuide
import pl.marianjureczko.poszukiwacz.ui.components.GoToBadgesScreen
import pl.marianjureczko.poszukiwacz.ui.components.MenuConfig
import pl.marianjureczko.poszukiwacz.ui.components.TopBar
import pl.marianjureczko.poszukiwacz.ui.handlePermission

@Composable
fun BluetoothScreen(
    navController: NavController,
    onClickOnGuide: GoToGuide,
    goToBadges: GoToBadgesScreen,
) {
    val viewModel: BluetoothViewModel = hiltViewModel()
    val state: State<BluetoothState> = viewModel.state

    val toRequest = viewModel.getPermissionRequirements()
    if (toRequest != null) {
        if (toRequest.shouldRequestOnThiDevice()) {
            handlePermission(toRequest) {
                viewModel.goToNextPermission(toRequest)
            }
        } else {
            viewModel.goToNextPermission(toRequest)
        }
    } else {
        val context = LocalContext.current
        if (viewModel.permissionsHandler.allPermissionsGranted(context)) {
            LaunchedEffect(Unit) {
                viewModel.init()
            }
        } else {
            Toast.makeText(context, R.string.bluetooth_permission_not_given, Toast.LENGTH_LONG).show()
            navController.navigateUp()
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                navController = navController,
                title = stringResource(id = R.string.sending_route),
                menuConfig = MenuConfig(onClickOnGuide, onClickBadges = goToBadges),
            )
        },
        content = { paddingValues ->
            BluetoothScreenBody(Modifier.padding(paddingValues), state.value, viewModel)
        }
    )
}

