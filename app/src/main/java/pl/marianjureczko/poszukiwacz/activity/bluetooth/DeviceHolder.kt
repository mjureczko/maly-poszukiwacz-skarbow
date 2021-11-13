package pl.marianjureczko.poszukiwacz.activity.bluetooth

import android.bluetooth.BluetoothDevice
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import pl.marianjureczko.poszukiwacz.R

class DeviceHolder(
    view: View,
    private val activity: FragmentActivity,
    private val memoConsole: MemoConsole,
    private val manager: BluetoothConnectionManager,
    private val recyclerView: RecyclerView
) : AbstractDeviceHolder(view) {

    override fun setupView(device: BluetoothDevice) {
        this.deviceName.text = device.name
        this.deviceName.setOnClickListener {
            memoConsole.print(activity.getString(R.string.device_was_selected, device.name))
            recyclerView.adapter = DevicesAdapter(activity, listOf(device), memoConsole, manager, recyclerView)
        }
    }
}