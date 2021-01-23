package pl.marianjureczko.poszukiwacz.activity

import android.Manifest
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.StorageHelper
import pl.marianjureczko.poszukiwacz.TreasuresList
import pl.marianjureczko.poszukiwacz.TreasuresListsAdapter


class MainActivity : AppCompatActivity() {

    companion object {
        const val SELECTED_LIST = "SELECTED_LIST"
    }

    private val TAG = javaClass.simpleName
    private val MY_PERMISSION_ACCESS_FINE_LOCATION = 12
    private lateinit var storageHelper: StorageHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "########> onCreate")
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        storageHelper = StorageHelper(this)

        setContentView(R.layout.activity_main)

        val treasures = storageHelper.loadAll()
        showTreasuresLists(treasures)

        val newListButton = findViewById<Button>(R.id.new_list_button)
        newListButton.setOnClickListener {
            val intent = Intent(this, TreasuresEditorActivity::class.java).apply {}
            startActivity(intent)
        }
        requestAccessLocationPermission()
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "########> onResume")
        val treasures = storageHelper.loadAll()
        showTreasuresLists(treasures)
    }

    private fun showTreasuresLists(treasures: MutableList<TreasuresList>) {
        val treasuresList = findViewById<ListView>(R.id.treasures_lists)
        val adapter = TreasuresListsAdapter(treasures, this, storageHelper)
        treasuresList.adapter = adapter
    }

    // invoked when the activity may be temporarily destroyed, save the instance state here
    override fun onSaveInstanceState(outState: Bundle?) {
        Log.d(TAG, "########> onSaveInstanceState")
        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState)

    }

    /**
     * Exit application when permission to access location was not granted.
     */
    override fun onRequestPermissionsResult(code: Int, perms: Array<String>, results: IntArray) {
        super.onRequestPermissionsResult(code, perms, results)
        if (code == MY_PERMISSION_ACCESS_FINE_LOCATION && results[0] != PackageManager.PERMISSION_GRANTED) {
            finish()
        }
    }

    private fun requestAccessLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), MY_PERMISSION_ACCESS_FINE_LOCATION)
        }
    }
}
