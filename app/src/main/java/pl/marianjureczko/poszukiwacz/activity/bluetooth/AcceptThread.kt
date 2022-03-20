package pl.marianjureczko.poszukiwacz.activity.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.util.Log
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.activity.main.Bluetooth
import java.io.IOException

@SuppressLint("MissingPermission")
class AcceptThread(
    memoConsole: MemoConsole,
    private val bluetooth: Bluetooth,
    private val bluetoothConnectionManager: BluetoothConnectionManager,
    private val context: Context
) : CancellableThread(memoConsole) {

    private val TAG = javaClass.simpleName
    private val serverSocket: BluetoothServerSocket? by lazy(LazyThreadSafetyMode.PUBLICATION) {
        bluetooth.adapter?.listenUsingRfcommWithServiceRecord(Bluetooth.NAME, MY_BLUETOOTH_UUID)
    }

    override fun run() {
        if (serverSocket == null) {
            printInConsole(context.getString(R.string.no_bluetooth_to_receive_route))
            return
        }
        // Keep listening until exception occurs or a socket is returned.
        var shouldLoop = true
        while (shouldLoop) {
            val socket: BluetoothSocket? = try {
                printInConsole(context.getString(R.string.bluetooth_waiting_to_accept))
                serverSocket?.accept()
            } catch (e: IOException) {
                printInConsole(context.getString(R.string.accepting_bluetooth_connection_error) + e.message)
                shouldLoop = false
                null
            }
            socket?.also {
                printInConsole(context.getString(R.string.bluetooth_connection_accepted))
                bluetoothConnectionManager.readRuteFromConnectedSocket(it)
                serverSocket?.close()
                shouldLoop = false
            }
        }
    }

    override fun cancel() {
        super.cancel()
        try {
            serverSocket?.close()
        } catch (e: IOException) {
            Log.e(TAG, "Could not close the connect socket", e)
        }
    }
}