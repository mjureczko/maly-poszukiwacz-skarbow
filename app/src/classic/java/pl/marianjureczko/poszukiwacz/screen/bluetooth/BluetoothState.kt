package pl.marianjureczko.poszukiwacz.screen.bluetooth

import pl.marianjureczko.poszukiwacz.model.Route

data class BluetoothState(
    val route: Route? = null,
    val mode: Mode,
    val messages: List<String> = emptyList(),
    val devices: List<String> = emptyList(),
    val deviceIsSelected: Boolean = false,
    val permissionToRequestIndex: Int = 0,
) {
    fun addMessage(msg: String): BluetoothState = copy(messages = messages + msg)
    fun shouldShowDeviceSelection(): Boolean {
        return devices.isNotEmpty() && !deviceIsSelected
    }
}
