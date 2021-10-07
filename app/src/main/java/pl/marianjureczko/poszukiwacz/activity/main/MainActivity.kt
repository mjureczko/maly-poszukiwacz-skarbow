package pl.marianjureczko.poszukiwacz.activity.main

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import pl.marianjureczko.poszukiwacz.App
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.activity.bluetooth.BluetoothActivity
import pl.marianjureczko.poszukiwacz.activity.treasureseditor.TreasuresEditorActivity
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.shared.PermissionsManager
import pl.marianjureczko.poszukiwacz.shared.StorageHelper
import pl.marianjureczko.poszukiwacz.shared.addIconToActionBar
import java.io.IOException
import java.io.InputStream
import java.util.*

//const val MESSAGE_READ: Int = 0
//const val MESSAGE_ERROR: Int = 1
val MY_BLUETOOTH_UUID: UUID = UUID.fromString("3c7397c5-68ea-487f-bc11-d5b113bcad71")

/**
 * Routes creation and selection activity
 */
class MainActivity : AppCompatActivity() {

    private val TAG = javaClass.simpleName
    private val MY_PERMISSION_ACCESS_FINE_LOCATION = 12
    private val MY_PERMISSION_ACCESS_BACKGROUND_LOCATION = 13
    private val storageHelper: StorageHelper by lazy { StorageHelper(this) }
    private val bluetooth: Bluetooth = Bluetooth(this)
    val permissionsManager = PermissionsManager(this)
    private lateinit var routesRecyclerView: RecyclerView
//    private val handler: Handler = NotificationHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "########> onCreate")
        super.onCreate(savedInstanceState)
        addIconToActionBar(supportActionBar)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_main)

        routesRecyclerView = findViewById(R.id.routes)
        routesRecyclerView.layoutManager = LinearLayoutManager(this)

        showRoutes(storageHelper.loadAll())

        findViewById<Button>(R.id.new_route_button).setOnClickListener {
            startActivity(TreasuresEditorActivity.intent(this))
        }
        findViewById<Button>(R.id.route_from_bluetooth_button).setOnClickListener {
            fetchRouteFromBluetooth()
        }
        setTitle(R.string.main_activity_title)
        requestAccessLocationPermission()
        requestAccessBluetoothDrivenLocationPermission()
        permissionsManager.requestBluetoothPermission()
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "########> onResume")
        val routes = storageHelper.loadAll()
        showRoutes(routes)
        if (routes.isNotEmpty()) {
            no_routes.visibility = View.GONE
        }
    }

    private fun showMessageInMain(msg: String) {
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showRoutes(routes: MutableList<Route>) {
        val routeAdapter = RouteAdapter(this, routes, storageHelper)
        routesRecyclerView.adapter = routeAdapter
    }

    /**
     * Exit application when permission to access location was not granted.
     */
    override fun onRequestPermissionsResult(code: Int, perms: Array<String>, results: IntArray) {
        super.onRequestPermissionsResult(code, perms, results)
        showMessageInMain("Obtained permission $code")
        if (code == MY_PERMISSION_ACCESS_FINE_LOCATION && results[0] != PackageManager.PERMISSION_GRANTED) {
            finish()
        }
    }

    private fun requestAccessBluetoothDrivenLocationPermission() {
        showMessageInMain("Checking ACCESS_BACKGROUND_LOCATION permission")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(
                    baseContext,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                showMessageInMain("Requesting ACCESS_BACKGROUND_LOCATION permission")
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                    MY_PERMISSION_ACCESS_BACKGROUND_LOCATION
                )
            }
        }
    }

    private fun requestAccessLocationPermission() {
        showMessageInMain("Checking ACCESS_FINE_LOCATION permission")
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            showMessageInMain("Requesting ACCESS_FINE_LOCATION permission")
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), MY_PERMISSION_ACCESS_FINE_LOCATION)
        }
    }

    private fun fetchRouteFromBluetooth() {
        startActivity(
            BluetoothActivity.intent(
                this,
                arrayOf(
                    "one",
                    "two",
                    "three",
                    "four",
                    "five",
                    "six",
                    "Samsung",
                    "LG",
                    "Telefunkel brand new model copletely orginal unique deluxe"
                )
            )
        )
//        startActivity(BluetoothActivity.intent(this, arrayOf("Samsung", "LG", "Telefunkel brand new model copletely orginal unique deluxe blah blah blah blah blah blah b.....")))
//        val device = bluetooth.findDevice()
//        if (device != null) {
//            Toast.makeText(this, "Bluetooth device found, starting accept thread", Toast.LENGTH_SHORT).show()
//            App.executorService.submit(AcceptThread(this))
//        } else {
//            Toast.makeText(this, "Bluetooth device not found", Toast.LENGTH_SHORT).show()
//        }
    }

    private fun manageMyConnectedSocket(socket: BluetoothSocket) {
        App.executorService.submit(ReaderThread(socket, this))
    }

    private inner class AcceptThread(private val activity: Activity) : Thread() {
        private val serverSocket: BluetoothServerSocket? by lazy(LazyThreadSafetyMode.PUBLICATION) {
            bluetooth.adapter?.listenUsingRfcommWithServiceRecord(Bluetooth.NAME, MY_BLUETOOTH_UUID)
        }

        override fun run() {
            // Keep listening until exception occurs or a socket is returned.
            var shouldLoop = true
            while (shouldLoop) {
                val handler = Handler(Looper.getMainLooper())
                val socket: BluetoothSocket? = try {
                    handler.post {
                        Toast.makeText(
                            activity,
                            "Accepting connections... the serverSocket is null ${serverSocket == null}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    serverSocket?.accept()
                } catch (e: IOException) {
                    handler.post { Toast.makeText(activity, "Socket's accept() method failed", Toast.LENGTH_SHORT).show() }
                    shouldLoop = false
                    null
                }
                handler.post { Toast.makeText(activity, "Accepted", Toast.LENGTH_SHORT).show() }
                socket?.also {
                    handler.post { Toast.makeText(activity, "Bluetooth socket obtained.", Toast.LENGTH_SHORT).show() }
                    manageMyConnectedSocket(it)
                    serverSocket?.close()
                    shouldLoop = false
                }
            }
        }

        // Closes the connect socket and causes the thread to finish.
        fun cancel() {
            try {
                serverSocket?.close()
            } catch (e: IOException) {
                Log.e(TAG, "Could not close the connect socket", e)
            }
        }
    }

    private inner class ReaderThread(
        private val socket: BluetoothSocket,
        private val activity: Activity
    ) : Thread() {

        private val inStream: InputStream = socket.inputStream
        private val buffer: ByteArray = ByteArray(1024) // mmBuffer store for the stream

        override fun run() {
            var numBytes: Int // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs.
            while (true) {
                // Read from the InputStream.
                numBytes = try {
                    inStream.read(buffer)
                } catch (e: IOException) {
                    Log.d(TAG, "Input stream was disconnected", e)
                    break
                }

                val handler = Handler(Looper.getMainLooper())
                var msg = ">>unread<<"
                handler.post {
                    try {
                        msg = String(buffer)
                    } catch (e: Exception) {

                    }
                    Toast.makeText(activity, "Data received through Bluetooth: $msg.", Toast.LENGTH_SHORT).show()
                }
                // Send the obtained bytes to the UI activity.
//                val readMsg = handler.obtainMessage(MESSAGE_READ, numBytes, -1, buffer)
//                readMsg.sendToTarget()
            }
        }
    }
}

//class NotificationHandler(private val activity: Activity) : Handler() {
//    override fun handleMessage(msg: Message) {
//        when (msg.what) {
//            MESSAGE_READ -> {
//                val readMessage = String(msg.obj as ByteArray, 0, msg.arg1)
//                Toast.makeText(activity, "A message was received over Bluetooth: $readMessage.", Toast.LENGTH_SHORT).show()
//            }
//            else -> {
//                Toast.makeText(activity, "Handler delivered unexpected message.", Toast.LENGTH_SHORT).show()
//                val readMessage = String(msg.obj as ByteArray, 0, msg.arg1)
//                Toast.makeText(activity, "The unexpected message: $readMessage.", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//}