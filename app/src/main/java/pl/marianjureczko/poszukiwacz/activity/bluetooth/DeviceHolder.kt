package pl.marianjureczko.poszukiwacz.activity.bluetooth

import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView

class DeviceHolder(
    view: View,
    private val activity: FragmentActivity,
    private val memoConsole: MemoConsole,
    private val recyclerView: RecyclerView
) : AbstractDeviceHolder(view) {

    override fun setupView(deviceName: String) {
        this.deviceName.text = deviceName
        this.deviceName.setOnClickListener {
            memoConsole.print("Wybrano urządzenie $deviceName\n")
            recyclerView.adapter = DevicesAdapter(activity, arrayOf(deviceName), memoConsole, recyclerView)
//            Toast.makeText(activity, "Wybrano urządzenie $deviceName", Toast.LENGTH_SHORT).show()
        }
    }
}