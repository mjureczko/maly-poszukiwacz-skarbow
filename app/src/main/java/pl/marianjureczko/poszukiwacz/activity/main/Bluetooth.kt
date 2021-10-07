package pl.marianjureczko.poszukiwacz.activity.main

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import android.widget.Toast
import pl.marianjureczko.poszukiwacz.activity.bluetooth.MemoConsole

class Bluetooth(private val memo: MemoConsole) {
    companion object {
        val NAME = "MALY_POSZUKIWACZ_SKARBOW"
    }

    val adapter: BluetoothAdapter? by lazy {
        BluetoothAdapter.getDefaultAdapter()
    }

    fun findDevice(): BluetoothDevice? {
        if (adapter == null) {
            memo.print("Bluetooth not available")
            return null
        }
        if (!adapter!!.isEnabled) {
            memo.print("Bluetooth is not enabled")
            return null
        }
        if (!memo.permissionsManager.bluetoothGranted()) {
            Toast.makeText(memo, "Bluetooth permission not granted", Toast.LENGTH_SHORT).show()
        }
        val pairedDevices: Set<BluetoothDevice>? = adapter?.bondedDevices
        pairedDevices?.forEach { device ->
            val deviceClass = device.bluetoothClass.majorDeviceClass
            Toast.makeText(
                memo,
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