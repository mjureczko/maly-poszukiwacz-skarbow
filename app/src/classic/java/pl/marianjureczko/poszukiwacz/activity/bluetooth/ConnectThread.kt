//package pl.marianjureczko.poszukiwacz.activity.bluetooth
//
//import android.annotation.SuppressLint
//import android.bluetooth.BluetoothDevice
//import android.bluetooth.BluetoothSocket
//import android.content.Context
//import android.util.Log
//import pl.marianjureczko.poszukiwacz.R
//import pl.marianjureczko.poszukiwacz.activity.main.Bluetooth
//import pl.marianjureczko.poszukiwacz.screen.bluetooth.Printer
//import java.io.ByteArrayOutputStream
//import java.io.IOException
//import java.io.OutputStream
//import java.util.concurrent.atomic.AtomicReference
//
//@SuppressLint("MissingPermission")
//class ConnectThread(
//    selectedDevice: BluetoothDevice,
//    private val route: ByteArrayOutputStream,
//    private val bluetooth: Bluetooth,
//    private val printer: Printer,
//    private val context: Context
//) : CancellableThread(printer) {
//
//    private val TAG = javaClass.simpleName
//
//    private var done: AtomicReference<Boolean> = AtomicReference(false)
//    private val socket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
//        try {
//            selectedDevice.createRfcommSocketToServiceRecord(MY_BLUETOOTH_UUID)
//        } catch (e: Exception) {
//            reportException(printer, e)
//        }
//    }
//
//    private fun reportException(printer: Printer, e: Exception): Nothing? {
//        printer.print(R.string.bluetooth_connecting_error, e.message ?: "")
//        return null
//    }
//
//    @SuppressLint("MissingPermission")
//    override fun run() {
//        // Cancel discovery because it slows down the connection, requires android.permission.BLUETOOTH_SCAN.
//        bluetooth.adapter?.cancelDiscovery()
//
//        socket?.let { socket ->
//            try {
//                socket.connect()
//                printer.print(R.string.bluetooth_connection_created)
//                writeRouteToSocket(socket)
//            } catch (e: Exception) {
//                Log.e(TAG, "Error occurred when creating connection", e)
//                printer.print(R.string.error_when_creating_connection, e.message ?: "")
//            }
//        }
//        done.set(true)
//    }
//
//    private fun writeRouteToSocket(socket: BluetoothSocket) {
//        try {
//            val outStream: OutputStream = socket.outputStream
//            printer.print(R.string.sending_over_bluetooth)
//            outStream.write(route.toByteArray())
//            printer.print(R.string.route_sent_over_bluetooth)
//        } catch (e: IOException) {
//            Log.e(TAG, "Error occurred when sending data", e)
//            printer.print(R.string.error_when_sending_route, e.message ?: "")
//            return
//        }
//
//    }
//
//    override fun cancel() {
//        super.cancel()
//        if (!done.get()) {
//            try {
//                socket?.close()
//            } catch (e: IOException) {
//                Log.e(TAG, "Could not close the client socket", e)
//            }
//        }
//    }
//}
