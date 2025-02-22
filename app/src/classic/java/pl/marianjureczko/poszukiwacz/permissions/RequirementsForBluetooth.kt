package pl.marianjureczko.poszukiwacz.permissions

import android.Manifest
import android.annotation.SuppressLint
import pl.marianjureczko.poszukiwacz.R

object RequirementsForBluetooth : Requirements {
    override fun getPermission(): String = Manifest.permission.BLUETOOTH
    override fun getMessage(): Int = R.string.missing_bluetooth_permission
}

object RequirementsForBluetoothConnect : Requirements {
    @SuppressLint("InlinedApi")
    override fun getPermission(): String = Manifest.permission.BLUETOOTH_CONNECT
    override fun getMessage(): Int = R.string.missing_bluetooth_permission
}

object RequirementsForNearbyWifiDevices : Requirements {
    @SuppressLint("InlinedApi")
    override fun getPermission(): String = Manifest.permission.NEARBY_WIFI_DEVICES
    override fun getMessage(): Int = R.string.missing_bluetooth_permission
}

object RequirementsForBluetoothScan : Requirements {
    override fun getPermission(): String = Manifest.permission.BLUETOOTH_SCAN
    override fun getMessage(): Int = R.string.missing_bluetooth_permission
}