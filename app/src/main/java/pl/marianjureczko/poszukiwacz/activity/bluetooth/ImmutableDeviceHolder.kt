package pl.marianjureczko.poszukiwacz.activity.bluetooth

import android.view.View

class ImmutableDeviceHolder(view: View) : AbstractDeviceHolder(view) {

    override fun setupView(deviceName: String) {
        this.deviceName.text = deviceName
        this.deviceName.isEnabled = false
    }
}