package pl.marianjureczko.poszukiwacz.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.pm.ActivityInfo
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import pl.marianjureczko.poszukiwacz.*
import pl.marianjureczko.poszukiwacz.listener.TextViewBasedLocationListener


class TreasuresEditorActivity : AppCompatActivity() {

    companion object {
        var treasuresList = TreasuresList("Nienazwana", ArrayList())
    }

    private val storageHelper = StorageHelper(this)
    private val xmlHelper = XmlHelper()
    var treasuresAdapter = TreasuresAdapter(treasuresList, this, storageHelper)
    lateinit var list: ListView

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_treasures_editor)

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
        builder.setPositiveButton("OK") { dialog, which ->
            val name = input.text.toString()
            treasuresList = TreasuresList(name, ArrayList())
            treasuresAdapter = TreasuresAdapter(treasuresList, this, storageHelper)
            list.adapter = treasuresAdapter
            listName.text = name
        }
        builder.show()
    }
}
