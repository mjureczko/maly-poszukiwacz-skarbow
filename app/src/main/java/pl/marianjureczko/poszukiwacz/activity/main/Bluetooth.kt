package pl.marianjureczko.poszukiwacz.activity.main

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import android.widget.Toast

class Bluetooth(private val activity: MainActivity) {
    companion object {
        val NAME = "MALY_POSZUKIWACZ_SKARBOW"
    }

    val adapter: BluetoothAdapter? by lazy {
        BluetoothAdapter.getDefaultAdapter()
    }

    fun findDevice(): BluetoothDevice? {
        if (adapter == null) {
            Toast.makeText(activity, "Bluetooth not available", Toast.LENGTH_SHORT).show()
            return null
        }
        if (!adapter!!.isEnabled) {
            Toast.makeText(activity, "Bluetooth is not enabled", Toast.LENGTH_SHORT).show()
//            Toast.makeText(activity, "Enabling Bluetooth", Toast.LENGTH_SHORT).show()
//            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//            ActivityCompat.startActivityForResult(activity, enableBtIntent, REQUEST_ENABLE_BT, null)
//            //TODO: there is no corresponding `public void onActivityResult(int requestCode, int resultCode, Intent data)`
//            return null
        }
        if (!activity.permissionsManager.bluetoothGranted()) {
            Toast.makeText(activity, "Bluetooth permission not granted", Toast.LENGTH_SHORT).show()
        }
        val pairedDevices: Set<BluetoothDevice>? = adapter?.bondedDevices
        pairedDevices?.forEach { device ->
            val deviceClass = device.bluetoothClass.majorDeviceClass
            Toast.makeText(
                activity,
                "Found ${device.name} ($deviceClass) which is phone ${deviceClass == BluetoothClass.Device.Major.PHONE}",
                Toast.LENGTH_SHORT
            ).show()
            if (deviceClass == BluetoothClass.Device.Major.PHONE) {
                return device
            }
        }
        //TODO: message no phone is connected through bluetooth
        return null
    }
}