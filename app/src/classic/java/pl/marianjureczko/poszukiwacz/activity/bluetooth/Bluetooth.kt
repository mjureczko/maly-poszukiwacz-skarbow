//package pl.marianjureczko.poszukiwacz.activity.main
//
//import android.annotation.SuppressLint
//import android.bluetooth.BluetoothAdapter
//import android.bluetooth.BluetoothClass
//import android.bluetooth.BluetoothDevice
//import pl.marianjureczko.poszukiwacz.R
//
//class BluetoothException(val msgId: Int) : Exception()
//
//class Bluetooth(
////    private val permissionsManager: PermissionsManager
//) {
//    companion object {
//        val NAME = "MALY_POSZUKIWACZ_SKARBOW"
//    }
//
//    val adapter: BluetoothAdapter? by lazy {
//        BluetoothAdapter.getDefaultAdapter()
//    }
//
//    @SuppressLint("MissingPermission")
//    @Throws(BluetoothException::class)
//    fun findDevices(): List<BluetoothDevice> {
//        if (adapter == null) {
//            throw BluetoothException(R.string.no_bluetooth)
//        }
//        if (!adapter!!.isEnabled) {
//            throw BluetoothException(R.string.bluetooth_disabled)
//        }
//        return adapter?.bondedDevices
//            ?.filter {
//                it.bluetoothClass.majorDeviceClass == BluetoothClass.Device.Major.PHONE
//            }
//            ?: listOf()
//    }
//
//    fun isConnected() {
//        // https://stackoverflow.com/questions/4715865/how-can-i-programmatically-tell-if-a-bluetooth-device-is-connected
//        // public void onCreate() {
//        //    ...
//        //    IntentFilter filter = new IntentFilter();
//        //    filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
//        //    filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
//        //    filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
//        //    this.registerReceiver(mReceiver, filter);
//        //}
//        //
//        ////The BroadcastReceiver that listens for bluetooth broadcasts
//        //private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
//        //    @Override
//        //    public void onReceive(Context context, Intent intent) {
//        //        String action = intent.getAction();
//        //        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//        //
//        //        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//        //           ... //Device found
//        //        }
//        //        else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
//        //           ... //Device is now connected
//        //        }
//        //        else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
//        //           ... //Done searching
//        //        }
//        //        else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
//        //           ... //Device is about to disconnect
//        //        }
//        //        else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
//        //           ... //Device has disconnected
//        //        }
//        //    }
//        //};
//    }
//}