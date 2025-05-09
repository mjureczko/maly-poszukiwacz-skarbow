package pl.marianjureczko.poszukiwacz.shared

import android.content.Context
import android.content.res.Resources
import android.graphics.BitmapFactory
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.CoordinateBounds
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style
import com.mapbox.maps.ViewAnnotationAnchor
import com.mapbox.maps.extension.style.layers.properties.generated.IconAnchor
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.viewannotation.ViewAnnotationManager
import com.mapbox.maps.viewannotation.viewAnnotationOptions
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.databinding.TreasureOnMapViewBinding
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureDescription

class MapHelper {

    private val TAG = javaClass.simpleName

    private class TreasureOnMapHelper(resources: Resources, mapView: MapView) {
        private val iconBitmap = BitmapFactory.decodeResource(resources, R.drawable.chest_closed_small)
        private val pointAnnotationManager = mapView.annotations.createPointAnnotationManager()
        private val viewAnnotationManager = mapView.viewAnnotationManager

        fun addTreasure(treasure: TreasureDescription) {
            val point = Point.fromLngLat(treasure.longitude, treasure.latitude)
            val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
                .withPoint(point)
                .withIconImage(iconBitmap)
                .withIconAnchor(IconAnchor.CENTER)
                .withDraggable(false)
            pointAnnotationManager.create(pointAnnotationOptions)
            addViewAnnotation(viewAnnotationManager, point, treasure.id.toString())
        }
    }

    companion object {
        fun renderTreasures(context: Context, treasures: List<TreasureDescription>, mapView: MapView) {
            mapView.getMapboxMap().loadStyleUri(Style.OUTDOORS)
            val cameraPosition = CameraOptions.Builder()
                .center(LocationHelper(treasures).center())
                .build()
            mapView.getMapboxMap().setCamera(cameraPosition)

            val treasureOnMapHelper = TreasureOnMapHelper(context.resources, mapView)
            treasures.forEach {
                treasureOnMapHelper.addTreasure(it)
            }
        }

        fun positionMapOnTreasures(route: Route, mapView: MapView, tightness: Double) {
            val mapboxMap: MapboxMap = mapView.getMapboxMap()
            val locationHelper = LocationHelper(route.treasures)
            //according to mapbox doc, executing below code in onCreate may be nondeterministic
            val cameraOptions = mapboxMap.cameraForCoordinateBounds(
                CoordinateBounds(locationHelper.southwest(), locationHelper.northeast(), false),
                EdgeInsets(-tightness, -tightness, -tightness, -tightness)
            )
            mapboxMap.setCamera(cameraOptions)
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
    }
}