package pl.marianjureczko.poszukiwacz.activity.main

import android.app.AlertDialog
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import pl.marianjureczko.poszukiwacz.App
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.activity.searching.SearchingActivity
import pl.marianjureczko.poszukiwacz.activity.treasureseditor.TreasuresEditorActivity
import pl.marianjureczko.poszukiwacz.model.Route
import java.io.IOException
import java.io.OutputStream


class RouteHolder(
    view: View,
    private val activity: MainActivity,
    private val routesRemover: RoutesRemover
) : RecyclerView.ViewHolder(view) {
    private val TAG = javaClass.simpleName
    private val selectBtn: Button = itemView.findViewById(R.id.select_route)
    private val editBtn: ImageButton = itemView.findViewById(R.id.edit_route)
    private val shareBtn: ImageButton = itemView.findViewById(R.id.share_route)
    private val deleteBtn: ImageButton = itemView.findViewById(R.id.delete_route)
    private val bluetooth: Bluetooth = Bluetooth(activity)
//    private val handler: Handler = NotificationHandler(activity)

    fun setupRoute(route: Route) {
        selectBtn.text = route.name
        selectBtn.setOnClickListener { activity.startActivity(SearchingActivity.intent(activity, route)) }
        shareBtn.setOnClickListener {
//            App.executorService.submit {
//                val handler = Handler(Looper.getMainLooper())
//                handler.post {
//                    Toast.makeText(activity, "Showing from thread", Toast.LENGTH_SHORT).show()
//                }
//            }
            val device: BluetoothDevice? = bluetooth.findDevice()
            //TODO: not sure if that is the right device...
            if (device != null) {
                Toast.makeText(activity, "Bluetooth device found, starting connect thread", Toast.LENGTH_SHORT).show()
                App.executorService.submit(ConnectThread(device))
            } else {
                Toast.makeText(activity, "Bluetooth device not found.", Toast.LENGTH_SHORT).show()
                Toast.makeText(activity, "2nd message", Toast.LENGTH_SHORT).show()
            }
        }
        editBtn.setOnClickListener { activity.startActivity(TreasuresEditorActivity.intent(activity, route)) }

        deleteBtn.setOnClickListener {
            AlertDialog.Builder(activity)
                .setMessage(activity.getString(R.string.route_remove_prompt, route.name))
                .setPositiveButton(R.string.no) { _, _ -> Log.d(TAG, "####no") }
                .setNegativeButton(R.string.yes) { _, _ -> routesRemover.remove(route) }
                .show()
        }
    }

    private inner class ConnectThread(device: BluetoothDevice) : Thread() {

        private val socket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
            try {
                sendMessageToMain("ConnectThread is trying to get the socket")
                val s = device.createRfcommSocketToServiceRecord(MY_BLUETOOTH_UUID)
                sendMessageToMain("ConnectThread got the socket")
                s
            } catch (e: Exception) {
                sendMessageToMain("ConnectThread got exception when obtaining the socket ${e.message}")
                null
            }
        }

        override fun run() {
            // Cancel discovery because it otherwise slows down the connection.
            bluetooth.adapter?.cancelDiscovery()

            sendMessageToMain("Socket is null ${socket == null}.")
            socket?.let { socket ->
                sendMessageToMain("Trying to connect to the remote device through the socket")
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                socket.connect()
                sendMessageToMain("Connected to the remote device through the socket")

                // The connection attempt succeeded. Perform work associated with
                // the connection in a separate thread.
                manageMyConnectedSocket(socket)
            }
        }

        private fun manageMyConnectedSocket(socket: BluetoothSocket) {
            try {
                val outStream: OutputStream = socket.outputStream
                sendMessageToMain("Will write to socket ${(outStream != null).toString()}")
                outStream.write("test message".toByteArray(Charsets.UTF_8))
                sendMessageToMain("Data written to the socket")
            } catch (e: IOException) {
                Log.e(TAG, "Error occurred when sending data", e)
                sendMessageToMain("Error occurred when sending data")
                return
            }

        }

        private fun sendMessageToMain(msg: String) {
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
            }
        }

        // Closes the client socket and causes the thread to finish.
        fun cancel() {
            try {
                socket?.close()
            } catch (e: IOException) {
                Log.e(TAG, "Could not close the client socket", e)
            }
        }
    }
}
