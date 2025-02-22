@file:OptIn(ExperimentalPermissionsApi::class)

package pl.marianjureczko.poszukiwacz.screen.bluetooth

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.permissions.RequirementsForBluetooth
import pl.marianjureczko.poszukiwacz.permissions.RequirementsForBluetoothConnect
import pl.marianjureczko.poszukiwacz.permissions.RequirementsForBluetoothScan
import pl.marianjureczko.poszukiwacz.permissions.RequirementsForNearbyWifiDevices
import pl.marianjureczko.poszukiwacz.shared.GoToFacebook
import pl.marianjureczko.poszukiwacz.shared.GoToGuide
import pl.marianjureczko.poszukiwacz.ui.components.TopBar
import pl.marianjureczko.poszukiwacz.ui.handlePermission

// Manifest.permission.BLUETOOTH
// if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)) {
//     permissions.add(Manifest.permission.BLUETOOTH_CONNECT)
// }
// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//     permissions.add(Manifest.permission.NEARBY_WIFI_DEVICES)
// }

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BluetoothScreen(
    navController: NavController,
    onClickOnGuide: GoToGuide,
    onClickOnFacebook: GoToFacebook,
) {
    val viewModel: BluetoothViewModel = hiltViewModel()
    val state: State<BluetoothState> = viewModel.state

    val bluetoothPermission: PermissionState = handlePermission(RequirementsForBluetooth)
    viewModel.addBluetoothPermission(bluetoothPermission)
    if (state.value.permissions.canAskForBluetoothScanPermission()) {
        viewModel.addBluetoothScanPermission(handlePermission(RequirementsForBluetoothScan))
    }
    if (state.value.permissions.canAskForBluetoothConnectPermission()) {
        viewModel.addBluetoothConnectPermission(handlePermission(RequirementsForBluetoothConnect))
    }
    if (state.value.permissions.canAskForNearbyWifiDevicesPermission()) {
        viewModel.addNearbyWifiDevicesPermission(handlePermission(RequirementsForNearbyWifiDevices))
    }

    if (state.value.permissions.canInitViewModel()) {
        LaunchedEffect(Unit) {
            viewModel.init()
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                navController = navController,
                title = stringResource(id = R.string.sending_route),
                onClickOnGuide = onClickOnGuide,
                onClickOnFacebook = { onClickOnFacebook("") },
            )
        },
        content = { _ ->
            BluetoothScreenBody(state.value, viewModel)
        }
    )
}

