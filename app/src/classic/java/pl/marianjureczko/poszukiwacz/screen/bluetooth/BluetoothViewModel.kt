package pl.marianjureczko.poszukiwacz.screen.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.res.Resources
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.permissions.Requirements
import pl.marianjureczko.poszukiwacz.permissions.RequirementsForBluetooth
import pl.marianjureczko.poszukiwacz.permissions.RequirementsForBluetoothConnect
import pl.marianjureczko.poszukiwacz.permissions.RequirementsForBluetoothScan
import pl.marianjureczko.poszukiwacz.permissions.RequirementsForNearbyWifiDevices
import pl.marianjureczko.poszukiwacz.screen.Screens
import pl.marianjureczko.poszukiwacz.shared.di.IoDispatcher
import pl.marianjureczko.poszukiwacz.shared.port.StorageHelper
import pl.marianjureczko.poszukiwacz.ui.PermissionsHandler
import javax.inject.Inject

interface OnDeviceSelected {
    fun sentRouteToDevice(deviceName: String)
}

interface Printer {
    fun print(msgId: Int)
    fun print(msgId: Int, vararg formatArgs: Any)
}

interface RouteReader {
    fun readRuteFromConnectedSocket(socket: BluetoothSocket)
}

@SuppressLint("MissingPermission")
@HiltViewModel
class BluetoothViewModel @Inject constructor(
    private val stateHandle: SavedStateHandle,
    private val storage: StorageHelper,
    private val resources: Resources,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel(), OnDeviceSelected, Printer, RouteReader {
    private val TAG = javaClass.simpleName
    private var _state: MutableState<BluetoothState> = mutableStateOf(createState())
    private val bluetooth: Bluetooth = Bluetooth()
    private var devices: List<BluetoothDevice>? = null
    val permissionsHandler: PermissionsHandler = PermissionsHandler(
        listOf(
            RequirementsForBluetooth,
            RequirementsForBluetoothScan,
            RequirementsForBluetoothConnect,
            RequirementsForNearbyWifiDevices,
        )
    )

    val state: State<BluetoothState>
        get() = _state

    fun init() {
        try {
            if (state.value.mode == Mode.SENDING) {
                devices = bluetooth.findDevices()
                if (devices!!.isEmpty()) {
                    print(R.string.no_bluetooth_device)
                } else {
                    if (devices!!.size == 1) {
                        sentRouteToDevice(devices!![0].name)
                    } else {
                        _state.value = state.value.copy(devices = devices!!.map { it.name })
                    }
                }
            } else {
                AcceptCoroutine(bluetooth, this, this).startAccepting(viewModelScope, ioDispatcher)
            }
        } catch (e: BluetoothException) {
            val msg = resources.getString(e.msgId)
            Log.w(TAG, msg, e)
            _state.value = state.value.addMessage(msg)
        }
    }

    override fun sentRouteToDevice(deviceName: String) {
        print(R.string.device_was_selected, deviceName)
        _state.value = state.value.copy(deviceIsSelected = true)
        val selectedDevice: BluetoothDevice = devices!!.first { it.name == deviceName }
        print(R.string.connect_to_device, selectedDevice.name)
        storage.routeToZipOutputStream(state.value.route!!).use { routeStream ->
            ConnectCoroutine(selectedDevice, routeStream, bluetooth, this)
                .sendRoute(viewModelScope, ioDispatcher)
        }
    }

    override fun print(msgId: Int) {
        viewModelScope.launch(Dispatchers.Main) {
            _state.value = state.value.addMessage(resources.getString(msgId))
        }
    }

    override fun print(msgId: Int, vararg formatArgs: Any) {
        viewModelScope.launch(Dispatchers.Main) {
            _state.value = state.value.addMessage(resources.getString(msgId, *formatArgs))
        }
    }

    fun goToNextPermission(previous: Requirements) {
        val nextPermissionIndex = permissionsHandler.requestNextPermission(previous)
        _state.value = _state.value.copy(permissionToRequestIndex = nextPermissionIndex)
    }

    fun getPermissionRequirements(): Requirements? =
        permissionsHandler.getPermissionRequirements(state.value.permissionToRequestIndex)

    override fun readRuteFromConnectedSocket(socket: BluetoothSocket) {
        storage.extractZipStream(socket.inputStream, ProgressPrinter(this))
        print(R.string.extracted_everything)
    }

    private fun createState(): BluetoothState {
        val mode = stateHandle.get<Mode>(Screens.Bluetooth.PARAMETER_MODE)!!
        var route: Route? = null
        if (mode == Mode.SENDING) {
            route = storage.loadRoute(stateHandle.get<String>(Screens.Bluetooth.PARAMETER_ROUTE_TO_SENT)!!)
        }
        return BluetoothState(
            route = route,
            mode = mode,
        )
    }
}