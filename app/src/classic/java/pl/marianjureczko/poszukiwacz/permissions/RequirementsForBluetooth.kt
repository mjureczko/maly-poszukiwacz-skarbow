package pl.marianjureczko.poszukiwacz.permissions

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import pl.marianjureczko.poszukiwacz.R

object RequirementsForBluetooth : Requirements {
    override fun getPermission(): String = Manifest.permission.BLUETOOTH
    override fun getMessage(): Int = R.string.missing_bluetooth_permission
    override fun shouldRequestOnThiDevice(): Boolean = true
}

object RequirementsForBluetoothConnect : Requirements {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun getPermission(): String = Manifest.permission.BLUETOOTH_CONNECT
    override fun getMessage(): Int = R.string.missing_bluetooth_permission
    override fun shouldRequestOnThiDevice(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
}

object RequirementsForNearbyWifiDevices : Requirements {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun getPermission(): String = Manifest.permission.NEARBY_WIFI_DEVICES
    override fun getMessage(): Int = R.string.missing_bluetooth_permission
    override fun shouldRequestOnThiDevice(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
}

object RequirementsForBluetoothScan : Requirements {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun getPermission(): String = Manifest.permission.BLUETOOTH_SCAN
    override fun getMessage(): Int = R.string.missing_bluetooth_permission
    override fun shouldRequestOnThiDevice(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
}