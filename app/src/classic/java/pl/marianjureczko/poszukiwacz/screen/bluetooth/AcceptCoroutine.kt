package pl.marianjureczko.poszukiwacz.screen.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import pl.marianjureczko.poszukiwacz.R
import java.io.IOException
import java.util.UUID

val MY_BLUETOOTH_UUID: UUID = UUID.fromString("3c7397c5-68ea-487f-bc11-d5b113bcad71")

@SuppressLint("MissingPermission")
class AcceptCoroutine(
    private val bluetooth: Bluetooth,
    private val printer: Printer,
    private val routeReader: RouteReader,
) {
    private val TAG = javaClass.simpleName
    private val serverSocket: BluetoothServerSocket? by lazy(LazyThreadSafetyMode.PUBLICATION) {
        bluetooth.adapter?.listenUsingRfcommWithServiceRecord(Bluetooth.NAME, MY_BLUETOOTH_UUID)
    }

    private var acceptJob: Job? = null

    fun startAccepting(scope: CoroutineScope, dispatcher: CoroutineDispatcher) {
        if (serverSocket == null) {
            printer.print(R.string.no_bluetooth_to_receive_route)
            return
        }

        acceptJob = scope.launch(dispatcher) {
            try {
                printer.print(R.string.bluetooth_waiting_to_accept)

                val socket: BluetoothSocket? = serverSocket?.accept()
                if (socket != null) {
                    printer.print(R.string.bluetooth_connection_accepted)
                    routeReader.readRuteFromConnectedSocket(socket)
                }
            } catch (e: IOException) {
                printer.print(R.string.accepting_bluetooth_connection_error, e.message ?: "")
            } finally {
                try {
                    serverSocket?.close()
                } catch (e: IOException) {
                    Log.e(TAG, "Could not close the connect socket", e)
                }
            }
        }
    }

    fun cancel() {
        acceptJob?.cancel()
        try {
            serverSocket?.close()
        } catch (e: IOException) {
            Log.e(TAG, "Could not close the connect socket", e)
        }
    }
}