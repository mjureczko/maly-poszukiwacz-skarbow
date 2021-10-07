package pl.marianjureczko.poszukiwacz.activity.bluetooth

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_bluetooth.*
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.activity.main.Bluetooth
import pl.marianjureczko.poszukiwacz.shared.PermissionsManager
import pl.marianjureczko.poszukiwacz.shared.addIconToActionBar

interface MemoConsole {
    fun print(msg: String)
}

class BluetoothActivity : AppCompatActivity(), MemoConsole {

    companion object {
        //        private val xmlHelper = XmlHelper()
//        const val REQUEST_PHOTO = 2
        private const val PARAM = "pl.marianjureczko.poszukiwacz.activity.devices";

        fun intent(packageContext: Context) = Intent(packageContext, BluetoothActivity::class.java)

        fun intent(packageContext: Context, devices: Array<String>) =
            Intent(packageContext, BluetoothActivity::class.java).apply {
                putExtra(PARAM, devices)
            }
    }

    private lateinit var devicesRecyclerView: RecyclerView
    private val permissionsManager = PermissionsManager(this)
    private val bluetooth: Bluetooth = Bluetooth(this)

    //TODO: try to use array of devices directly in DeviceAdapter and remove the model
    private val model: BluetoothViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addIconToActionBar(supportActionBar)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setContentView(R.layout.activity_bluetooth)

        setTitle(R.string.sending_route)
        devicesRecyclerView = findViewById(R.id.devices)
        devicesRecyclerView.layoutManager = LinearLayoutManager(this)
//        requestAccessBluetoothDrivenLocationPermission()
//        permissionsManager.requestBluetoothPermission()

        val device = bluetooth.findDevice()
        if (device != null) {
            Toast.makeText(this, "Bluetooth device found, starting accept thread", Toast.LENGTH_SHORT).show()
            App.executorService.submit(AcceptThread(this))
        } else {
            Toast.makeText(this, "Bluetooth device not found", Toast.LENGTH_SHORT).show()
        }


        model.devices = intent.getStringArrayExtra(PARAM)
        devicesRecyclerView.adapter = DevicesAdapter(this, model.devices, this, devicesRecyclerView)

        val memo: EditText = findViewById(R.id.memo)
        memo.setText(R.string.select_bluetooth_device)
        memo.isEnabled = false
        memo.isFocusable = false
        memo.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))

    }

    override fun print(msg: String) {
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            val text = memo.text
            text.insert(text.length, "> $msg")
            memo.setSelection(text.length)
        }
    }
}