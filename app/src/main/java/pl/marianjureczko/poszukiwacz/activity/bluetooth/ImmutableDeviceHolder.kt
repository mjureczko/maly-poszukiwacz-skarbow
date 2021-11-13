package pl.marianjureczko.poszukiwacz.activity.bluetooth

import android.bluetooth.BluetoothDevice
import android.view.View

class ImmutableDeviceHolder(view: View) : AbstractDeviceHolder(view) {

    override fun setupView(device: BluetoothDevice) {
        this.deviceName.text = device.name
        this.deviceName.isEnabled = false
    }
}