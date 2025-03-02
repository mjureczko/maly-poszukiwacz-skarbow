package pl.marianjureczko.poszukiwacz.screen.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.marianjureczko.poszukiwacz.R
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.concurrent.atomic.AtomicBoolean

@SuppressLint("MissingPermission")
class ConnectCoroutine(
    private val selectedDevice: BluetoothDevice,
    private val route: ByteArrayOutputStream,
    private val bluetooth: Bluetooth,
    private val printer: Printer,
) {

    private val TAG = javaClass.simpleName
    private var connectJob: Job? = null
    private var isDone = AtomicBoolean(false)
    private var socket: BluetoothSocket? = null

    fun sendRoute(scope: CoroutineScope, dispatcher: CoroutineDispatcher) {
        connectJob = scope.launch(dispatcher) {
            bluetooth.adapter?.cancelDiscovery() // Cancel discovery to speed up connection

            try {
                socket = selectedDevice.createRfcommSocketToServiceRecord(MY_BLUETOOTH_UUID)
                socket?.connect()
                printer.print(R.string.bluetooth_connection_created)
                sendRoute(socket!!)
            } catch (e: IOException) {
                Log.e(TAG, "Error occurred when creating connection", e)
                printer.print(R.string.error_when_creating_connection, e.message ?: "")
            } finally {
                isDone.set(true)
            }
        }
    }

    private suspend fun sendRoute(socket: BluetoothSocket) = withContext(Dispatchers.IO) {
        try {
            val outStream: OutputStream = socket.outputStream
            printer.print(R.string.sending_over_bluetooth)
            outStream.write(route.toByteArray())
            printer.print(R.string.route_sent_over_bluetooth)
        } catch (e: IOException) {
            Log.e(TAG, "Error occurred when sending data", e)
            printer.print(R.string.error_when_sending_route, e.message ?: "")
        }
    }

    fun cancel() {
        connectJob?.cancel()
        if (!isDone.get()) {
            try {
                socket?.close()
            } catch (e: IOException) {
                Log.e(TAG, "Could not close the client socket", e)
            }
        }
    }
}