package pl.marianjureczko.poszukiwacz.activity.main

import android.Manifest
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.activity.bluetooth.BluetoothActivity
import pl.marianjureczko.poszukiwacz.activity.treasureseditor.TreasuresEditorActivity
import pl.marianjureczko.poszukiwacz.databinding.ActivityMainBinding
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.shared.PermissionsManager
import pl.marianjureczko.poszukiwacz.shared.StorageHelper
import pl.marianjureczko.poszukiwacz.shared.addIconToActionBar

/**
 * Routes creation and selection activity
 */
class MainActivity : AppCompatActivity() {

    private val TAG = javaClass.simpleName
    private val MY_PERMISSION_ACCESS_FINE_LOCATION = 12
    private val storageHelper: StorageHelper by lazy { StorageHelper(this) }
    private val permissionsManager = PermissionsManager(this)
    private lateinit var routesRecyclerView: RecyclerView
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "########> onCreate")
        binding = ActivityMainBinding.inflate(layoutInflater)
        addIconToActionBar(supportActionBar)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_main)

        routesRecyclerView = binding.routes
        routesRecyclerView.layoutManager = LinearLayoutManager(this)

        val routes = storageHelper.loadAll()
        showRoutes(routes)

        binding.newRouteButton.setOnClickListener {
            startActivity(TreasuresEditorActivity.intent(this))
        }
        binding.routeFromBluetoothButton.setOnClickListener {
            fetchRouteFromBluetooth()
        }
        setTitle(R.string.main_activity_title)
        requestAccessLocationPermission()
        permissionsManager.requestBluetoothPermissions()
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "########> onResume")
        val routes = storageHelper.loadAll()
        showRoutes(routes)
        if (routes.isNotEmpty()) {
            binding.noRoutes.visibility = View.GONE
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
        if (code == MY_PERMISSION_ACCESS_FINE_LOCATION && results[0] != PackageManager.PERMISSION_GRANTED) {
            finish()
        }
    }

    private fun requestAccessLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), MY_PERMISSION_ACCESS_FINE_LOCATION)
        }
    }

    private fun fetchRouteFromBluetooth() =
        startActivity(BluetoothActivity.intent(this, BluetoothActivity.Mode.ACCEPTING, null))
}