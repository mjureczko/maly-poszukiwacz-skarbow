package pl.marianjureczko.poszukiwacz.activity.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import pl.marianjureczko.poszukiwacz.R

class DevicesAdapter(
    private val activity: FragmentActivity,
    private val devices: List<BluetoothDevice>,
    private val memoConsole: MemoConsole,
    private val manager: BluetoothConnectionManager,
    private val recyclerView: RecyclerView
) : RecyclerView.Adapter<AbstractDeviceHolder>() {

    @SuppressLint("MissingPermission")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractDeviceHolder {
        val view = activity.layoutInflater.inflate(R.layout.device_item, parent, false)
        return if (devices.size == 1) {
            memoConsole.print(activity.getString(R.string.connect_to_device, devices[0].name))
            manager.tryToConnect(devices[0])
            ImmutableDeviceHolder(view)
        } else {
            DeviceHolder(view, activity, memoConsole, manager, recyclerView)
        }
    }

    override fun onBindViewHolder(holder: AbstractDeviceHolder, position: Int) {
        holder.setupView(devices[position])
    }

    override fun getItemCount(): Int = devices.size
}