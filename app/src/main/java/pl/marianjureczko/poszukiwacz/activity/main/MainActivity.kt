package pl.marianjureczko.poszukiwacz.activity.main

import android.Manifest
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.StorageHelper
import pl.marianjureczko.poszukiwacz.activity.treasureseditor.TreasuresEditorActivity
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.shared.addIconToActionBar

/**
 * Routes creation and selection activity
 */
class MainActivity : AppCompatActivity() {

    private val TAG = javaClass.simpleName
    private val MY_PERMISSION_ACCESS_FINE_LOCATION = 12
    private val storageHelper: StorageHelper by lazy { StorageHelper(this) }
    private lateinit var routesRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "########> onCreate")
        super.onCreate(savedInstanceState)
        addIconToActionBar(supportActionBar)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_main)

        routesRecyclerView = findViewById(R.id.routes)
        routesRecyclerView.layoutManager = LinearLayoutManager(this)

        showRoutes(storageHelper.loadAll())

        val newRouteButton = findViewById<Button>(R.id.new_route_button)
        newRouteButton.setOnClickListener {
            startActivity(TreasuresEditorActivity.intent(this))
        }
        setTitle(R.string.main_activity_title);
        requestAccessLocationPermission()
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
}
