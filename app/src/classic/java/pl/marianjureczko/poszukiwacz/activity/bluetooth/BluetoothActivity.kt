//package pl.marianjureczko.poszukiwacz.activity.bluetooth
//
//import android.bluetooth.BluetoothDevice
//import android.bluetooth.BluetoothSocket
//import android.content.Context
//import android.content.Intent
//import android.content.pm.ActivityInfo
//import android.os.Bundle
//import android.os.Handler
//import android.os.Looper
//import android.widget.EditText
//import androidx.activity.viewModels
//import androidx.core.content.ContextCompat
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import pl.marianjureczko.poszukiwacz.App
//import pl.marianjureczko.poszukiwacz.R
//import pl.marianjureczko.poszukiwacz.activity.facebook.FacebookInputData
//import pl.marianjureczko.poszukiwacz.activity.main.Bluetooth
//import pl.marianjureczko.poszukiwacz.activity.main.BluetoothException
//import pl.marianjureczko.poszukiwacz.model.Route
//import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
//import pl.marianjureczko.poszukiwacz.permissions.ActivityRequirements
//import pl.marianjureczko.poszukiwacz.permissions.PermissionActivity
//import pl.marianjureczko.poszukiwacz.permissions.RequirementsForBluetooth
//import pl.marianjureczko.poszukiwacz.shared.StorageHelper
//import pl.marianjureczko.poszukiwacz.shared.XmlHelper
//import java.util.UUID
//
//interface MemoConsole {
//    fun print(msg: String)
//}
//
//interface BluetoothConnectionManager {
//    fun tryToAcceptConnection()
//    fun readRuteFromConnectedSocket(socket: BluetoothSocket)
//    fun tryToConnect(selectedDevice: BluetoothDevice)
//}
//
////TODO: handle full activity lifecycle
////TODO: extend bluetooth capabilities: is Connected & look for Bluetooth devices (https://developer.android.com/guide/topics/connectivity/bluetooth/permissions)
//class BluetoothActivity : PermissionActivity(), MemoConsole, BluetoothConnectionManager {
//
//    enum class Mode {
//        SENDING, ACCEPTING
//    }
//
//    companion object {
//        private const val MODE = "pl.marianjureczko.poszukiwacz.activity.bluetooth_mode";
//        private const val ROUTE = "pl.marianjureczko.poszukiwacz.activity.route";
//
//        private val xmlHelper = XmlHelper()
//
//        fun intent(packageContext: Context, mode: Mode, route: Route?) = Intent(packageContext, BluetoothActivity::class.java).apply {
//            putExtra(MODE, mode.toString())
//            route?.let {
//                putExtra(ROUTE, xmlHelper.writeToString(it))
//            }
//        }
//    }
//
//    private lateinit var devicesRecyclerView: RecyclerView
//    private val bluetooth: Bluetooth = Bluetooth()
//    private val storageHelper: StorageHelper by lazy { StorageHelper(this) }
//
//    private val model: BluetoothViewModel by viewModels()
//
//    override fun onPermissionsGranted(activityRequirements: ActivityRequirements) {
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//        setContentView(R.layout.activity_bluetooth)
//        assurePermissionsAreGranted(RequirementsForBluetooth, true)
//
//        setTitle(R.string.sending_route)
//        val memo: EditText = configureMemo()
//        val mode = intent.getStringExtra(MODE)
//        if (Mode.SENDING.toString() == mode) {
//            model.setup(intent.getStringExtra(ROUTE)!!)
//            devicesRecyclerView = findViewById(R.id.devices)
//            devicesRecyclerView.layoutManager = LinearLayoutManager(this)
//            try {
//                val devices: List<BluetoothDevice> = bluetooth.findDevices()
//                if (devices.isEmpty()) {
//                    memo.setText(R.string.no_bluetooth_device)
//                } else {
//                    devicesRecyclerView.adapter = DevicesAdapter(this, devices, this, this, devicesRecyclerView)
//                    if (devices.size > 1) {
//                        memo.setText(R.string.select_bluetooth_device)
//                    } else {
//                        memo.setText(R.string.one_bluetooth_device)
//                    }
//                }
//            } catch (e: BluetoothException) {
//                memo.setText(e.msgId)
//            }
//        } else {
//            tryToAcceptConnection()
//        }
//        setUpAds(findViewById(R.id.adView))
//    }
//
//    override fun getTreasureProgress(): TreasuresProgress? {
//        return null
//    }
//
//    override fun onBackPressed() {
//        if (model.thread != null) {
//            model.thread!!.cancel()
//        }
//        super.onBackPressed()
//    }
//
//    override fun tryToAcceptConnection() {
//        model.thread = AcceptThread(this, bluetooth, this, this)
//        App.executorService.submit(model.thread)
//    }
//
//    override fun readRuteFromConnectedSocket(socket: BluetoothSocket) {
//        model.thread = null
//        App.executorService.submit(ReaderThread(socket, this, this))
//    }
//
//    override fun tryToConnect(selectedDevice: BluetoothDevice) {
//        val zip = storageHelper.routeToZipOutputStream(model.route)
//        model.thread = ConnectThread(selectedDevice, zip, bluetooth, memoConsole = this, context = this)
//        App.executorService.submit(model.thread)
//    }
//
//    override fun print(msg: String) {
//        val handler = Handler(Looper.getMainLooper())
//        handler.post {
//            val memo: EditText = findViewById(R.id.memo)
//            val text = memo.text
//            text.insert(text.length, "\n> $msg")
//            memo.setSelection(text.length)
//        }
//    }
//
//    private fun configureMemo(): EditText {
//        val memo: EditText = findViewById(R.id.memo)
//        memo.isEnabled = false
//        memo.isFocusable = false
//        memo.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
//        return memo
//    }
//}
//
//val MY_BLUETOOTH_UUID: UUID = UUID.fromString("3c7397c5-68ea-487f-bc11-d5b113bcad71")