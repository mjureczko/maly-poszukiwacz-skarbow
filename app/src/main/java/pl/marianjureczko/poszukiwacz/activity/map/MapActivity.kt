package pl.marianjureczko.poszukiwacz.activity.map

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.viewModels
import com.mapbox.bindgen.Value
import com.mapbox.maps.MapView
import pl.marianjureczko.poszukiwacz.App
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.databinding.ActivityMapBinding
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.shared.ActivityWithAdsAndBackButton
import pl.marianjureczko.poszukiwacz.shared.MapHelper

class MapActivity : ActivityWithAdsAndBackButton() {

    companion object {
        const val MAP = "pl.marianjureczko.poszukiwacz.activity.map"
    }

    private val TAG = javaClass.simpleName
    private lateinit var binding: ActivityMapBinding
    private lateinit var mapView: MapView
    private val model: MapViewModel by viewModels()

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_map)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        supportActionBar?.title = "${App.getResources().getString(R.string.map_activity_title)}"

        model.setup(intent.getSerializableExtra(MAP) as Route)
        mapView = binding.mapView
        MapHelper.renderTreasures(model.route, mapView, this.resources)
        hideRoads()

        setContentView(binding.root)
        setUpAds(binding.adView)
    }

    override fun onResume() {
        super.onResume()
        MapHelper.positionMapOnTreasures(model.route, mapView, 400.0)
    }

    private fun hideRoads() {
        mapView.getMapboxMap().getStyle { style ->
            style.styleLayers.asSequence()
                .filter { l -> l.id.startsWith("road") }
                .forEach { l ->
                    style.setStyleLayerProperty(l.id, "visibility", Value("none"))
                }
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }
}