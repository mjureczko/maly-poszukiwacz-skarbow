package pl.marianjureczko.poszukiwacz.activity.bluetooth

import android.bluetooth.BluetoothDevice
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import pl.marianjureczko.poszukiwacz.R

abstract class AbstractDeviceHolder(view: View) : RecyclerView.ViewHolder(view) {

    val deviceName: Button = itemView.findViewById((R.id.device_name))

    abstract fun setupView(device: BluetoothDevice)
}