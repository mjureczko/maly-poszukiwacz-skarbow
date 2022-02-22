package pl.marianjureczko.poszukiwacz.activity.bluetooth

import android.bluetooth.BluetoothDevice
import android.view.View

class ImmutableDeviceHolder(view: View) : AbstractDeviceHolder(view) {

    override fun setupView(device: BluetoothDevice) {
        try {
            this.deviceName.text = device.name
        } catch (e: SecurityException) {
        }
        this.deviceName.isEnabled = false
    }
}