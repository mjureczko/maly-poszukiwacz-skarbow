package pl.marianjureczko.poszukiwacz.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.location.LocationManager
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Handler
import android.text.InputType
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import pl.marianjureczko.poszukiwacz.*
import pl.marianjureczko.poszukiwacz.listener.TextViewBasedLocationListener
import java.io.IOException

private const val REQUEST_RECORD_AUDIO_PERMISSION = 200
private const val LOG_TAG = "TreasuresEditorActivity"

class TreasuresEditorActivity : AppCompatActivity() {

    companion object {
        var treasuresList = TreasuresList("Nienazwana", ArrayList())
    }

    private var permissionToRecordAccepted = false
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)
    private val storageHelper = StorageHelper(this)
    private val xmlHelper = XmlHelper()
    private var treasuresAdapter = TreasuresAdapter(treasuresList, this, storageHelper)
    lateinit var list: ListView

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_treasures_editor)
        requestRecordingPermission()

        val existingList = intent.getStringExtra(MainActivity.SELECTED_LIST)
        if (existingList != null) {
            treasuresList = xmlHelper.loadFromString(existingList)
            treasuresAdapter = TreasuresAdapter(treasuresList, this, storageHelper)
            //TODO: treasuresList and treasuresAdapter are changed together
        } else {
            setupTreasuresListUsingDialog()
        }

        val addTreasureButton = findViewById<Button>(R.id.add_treasure)
        val lat = findViewById<TextView>(R.id.editorLatValue)
        val lon = findViewById<TextView>(R.id.editorLongValue)
        list = findViewById(R.id.treasures)
        list.adapter = treasuresAdapter

        addTreasureButton.setOnClickListener {
            val treasure = TreasureDescription(
                latitude = lat.text.toString().toDouble(),
                longitude = lon.text.toString().toDouble()
            )
            treasuresList.tresures.add(treasure)
            treasuresAdapter.notifyDataSetChanged()
            storageHelper.save(treasuresList)
        }

        val locationListener = TextViewBasedLocationListener(lat, lon)
        //TODO: copied from SearchingActivity
        val handler = Handler()
        val location = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val presenter = LocationPresenter(this, locationListener, handler, this, location)
        handler.post(presenter)
    }

    private fun setupTreasuresListUsingDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Podaj nazwę listy skarbów:")
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
        builder.setView(input)
        val listName = findViewById<TextView>(R.id.treasures_list_name)
        builder.setPositiveButton("OK") { _, _ ->
            val name = input.text.toString()
            treasuresList = TreasuresList(name, ArrayList())
            treasuresAdapter = TreasuresAdapter(treasuresList, this, storageHelper)
            treasuresAdapter.permissionToRecordAccepted = permissionToRecordAccepted
            list.adapter = treasuresAdapter
            listName.text = name
        }
        builder.show()
    }




    override fun onRequestPermissionsResult(code: Int, perms: Array<String>, results: IntArray) {
        super.onRequestPermissionsResult(code, perms, results)
        permissionToRecordAccepted = if (code == REQUEST_RECORD_AUDIO_PERMISSION) {
            results[0] == PackageManager.PERMISSION_GRANTED
        } else {
            false
        }
        //TODO: using this setter is ugly
        treasuresAdapter.permissionToRecordAccepted = permissionToRecordAccepted
        if (!permissionToRecordAccepted) {
            finish()
        }
    }

    private fun requestRecordingPermission() {
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)
    }
}
