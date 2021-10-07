package pl.marianjureczko.poszukiwacz.activity.bluetooth

import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import pl.marianjureczko.poszukiwacz.R

class DevicesAdapter(
    private val activity: FragmentActivity,
    private val devices: Array<String>,
    private val memoConsole: MemoConsole,
    private val recyclerView: RecyclerView
) : RecyclerView.Adapter<AbstractDeviceHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractDeviceHolder {
        val view = activity.layoutInflater.inflate(R.layout.device_item, parent, false)
        return if (devices.size == 1) {
            ImmutableDeviceHolder(view)
        } else {
            DeviceHolder(view, activity, memoConsole, recyclerView)
        }
    }

    override fun onBindViewHolder(holder: AbstractDeviceHolder, position: Int) {
        holder.setupView(devices[position])
    }

    override fun getItemCount(): Int {
        return devices.size
    }
}