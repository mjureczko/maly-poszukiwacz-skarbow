package pl.marianjureczko.poszukiwacz.activity.main

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.activity.bluetooth.BluetoothActivity
import pl.marianjureczko.poszukiwacz.activity.treasureseditor.TreasuresEditorActivity
import pl.marianjureczko.poszukiwacz.databinding.ActivityMainBinding
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.permissions.PermissionActivity
import pl.marianjureczko.poszukiwacz.permissions.RequirementsForNavigation
import pl.marianjureczko.poszukiwacz.permissions.ActivityRequirements
import pl.marianjureczko.poszukiwacz.shared.StorageHelper

/**
 * Routes creation and selection activity
 */
class MainActivity : PermissionActivity() {

    private val TAG = javaClass.simpleName
    private val storageHelper: StorageHelper by lazy { StorageHelper(this) }
    private lateinit var routesRecyclerView: RecyclerView
    private lateinit var binding: ActivityMainBinding

    override fun onPermissionsGranted(activityRequirements: ActivityRequirements) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_main)
        assurePermissionsAreGranted(RequirementsForNavigation, true)

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
        setContentView(binding.root)

        setUpAds(binding.adView)
    }

    override fun onResume() {
        super.onResume()
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

    private fun fetchRouteFromBluetooth() =
        startActivity(BluetoothActivity.intent(this, BluetoothActivity.Mode.ACCEPTING, null))
}