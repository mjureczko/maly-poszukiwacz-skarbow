package pl.marianjureczko.poszukiwacz.screen.bluetooth

import android.os.Build
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale
import pl.marianjureczko.poszukiwacz.model.Route

@OptIn(ExperimentalPermissionsApi::class)
data class BluetoothState(
    val permissions: Permissions = Permissions(),
    val route: Route? = null,
    val mode: Mode,
    val messages: List<String> = emptyList(),
    val devices: List<String> = emptyList(),
    val deviceIsSelected: Boolean = false,
) {
    fun addMessage(msg: String): BluetoothState = copy(messages = messages + msg)
    fun shouldShowDeviceSelection(): Boolean {
        return devices.isNotEmpty() && !deviceIsSelected
    }
}

//TODO t: what will happen when permission not granted
@OptIn(ExperimentalPermissionsApi::class)
data class Permissions(
    val bluetoothPermission: PermissionState? = null,
    val bluetoothScanPermission: PermissionState? = null,
    val bluetoothConnectPermission: PermissionState? = null,
    val nearbyWifiDevicesPermission: PermissionState? = null,
) {
    //TODO t: remove duplication, use canAskForNextPermission fun
    //TODO check SDK version
    @Composable
    fun canAskForBluetoothScanPermission(): Boolean {
        return bluetoothPermissionGranted()
                && !scanPermissionGranted()
    }

    @Composable
    fun canAskForBluetoothConnectPermission(): Boolean {
        return sdkRequiringScanPermission()
                && scanPermissionGranted()
                && !connectPermissionGranted()
    }

    @Composable
    fun canAskForNearbyWifiDevicesPermission(): Boolean {
        return sdkRequiringCanAskForNearbyWifiDevices()
                && connectPermissionGranted()
                && !nearbyWifiDevicesPermissionGranted()
    }

    @Composable
    fun canInitViewModel(): Boolean {
        return scanPermissionGranted()
                && (!sdkRequiringScanPermission() || connectPermissionGranted())
                && (!sdkRequiringScanPermission() || nearbyWifiDevicesPermissionGranted())
    }

    @Composable
    private fun bluetoothPermissionGranted() =
        bluetoothPermission?.status?.isGranted == true || bluetoothPermission?.status?.shouldShowRationale == true

    @Composable
    private fun scanPermissionGranted() =
        bluetoothScanPermission?.status?.isGranted == true || bluetoothScanPermission?.status?.shouldShowRationale == true

    @Composable
    private fun sdkRequiringScanPermission() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    @Composable
    private fun sdkRequiringCanAskForNearbyWifiDevices() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

    @Composable
    private fun connectPermissionGranted() =
        bluetoothConnectPermission?.status?.isGranted == true || bluetoothConnectPermission?.status?.shouldShowRationale == true

    @Composable
    fun nearbyWifiDevicesPermissionGranted() =
        nearbyWifiDevicesPermission?.status?.isGranted == true || nearbyWifiDevicesPermission?.status?.shouldShowRationale == true
}
