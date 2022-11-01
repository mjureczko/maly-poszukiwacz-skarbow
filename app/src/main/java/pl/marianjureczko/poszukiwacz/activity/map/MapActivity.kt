package pl.marianjureczko.poszukiwacz.activity.map

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.viewModels
import com.mapbox.bindgen.Value
import com.mapbox.geojson.Point
import com.mapbox.maps.*
import com.mapbox.maps.extension.style.layers.properties.generated.IconAnchor
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.viewannotation.ViewAnnotationManager
import com.mapbox.maps.viewannotation.viewAnnotationOptions
import pl.marianjureczko.poszukiwacz.App
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.databinding.ActivityMapBinding
import pl.marianjureczko.poszukiwacz.databinding.TreasureOnMapViewBinding
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.shared.ActivityWithAdsAndBackButton

class MapActivity : ActivityWithAdsAndBackButton() {

    companion object {
        const val MAP = "pl.marianjureczko.poszukiwacz.activity.map";
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
        showTreasures(model.route)
        hideRoads()

        setContentView(binding.root)
        setUpAds(binding.adView)
    }

    override fun onResume() {
        super.onResume()
        val mapboxMap: MapboxMap = mapView.getMapboxMap()
        val locationHelper = LocationHelper(model.route)
        //according to mapbox doc, executing below code in onCreate may be nondeterministic
        val cameraOptions = mapboxMap.cameraForCoordinateBounds(
            CoordinateBounds(locationHelper.southwest(), locationHelper.northeast(), false),
            EdgeInsets(-400.0, -400.0, -400.0, -400.0)
        )
        mapboxMap.setCamera(cameraOptions)
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

    private fun showTreasures(route: Route) {
        mapView.getMapboxMap().loadStyleUri(Style.OUTDOORS)
        val cameraPosition = CameraOptions.Builder()
            .center(LocationHelper(model.route).center())
            .build()
        mapView.getMapboxMap().setCamera(cameraPosition)

        val iconBitmap = BitmapFactory.decodeResource(resources, R.drawable.chest_closed_small)
        val annotationApi = mapView.annotations
        val pointAnnotationManager = annotationApi.createPointAnnotationManager()
        val viewAnnotationManager = mapView.viewAnnotationManager

        route.treasures.forEach {
            val point = Point.fromLngLat(it.longitude, it.latitude)
            val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
                .withPoint(point)
                .withIconImage(iconBitmap)
                .withIconAnchor(IconAnchor.CENTER)
                .withDraggable(false)
            pointAnnotationManager.create(pointAnnotationOptions)
            addViewAnnotation(viewAnnotationManager, point, it.id.toString())
        }
    }

    private fun addViewAnnotation(viewAnnotationManager: ViewAnnotationManager, point: Point, label: String) {
        val viewAnnotation = viewAnnotationManager.addViewAnnotation(
            resId = R.layout.treasure_on_map_view,
            options = viewAnnotationOptions {
                geometry(point)
                anchor(ViewAnnotationAnchor.TOP)
            }
        )
        TreasureOnMapViewBinding.bind(viewAnnotation).apply {
            treasureLabel.text = label
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