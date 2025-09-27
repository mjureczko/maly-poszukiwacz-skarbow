package pl.marianjureczko.poszukiwacz.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.ViewAnnotation
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.extension.compose.annotation.rememberIconImage
import com.mapbox.maps.extension.compose.style.MapStyle
import com.mapbox.maps.viewannotation.geometry
import com.mapbox.maps.viewannotation.viewAnnotationOptions
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.shared.LocationHelper

@Composable
fun TreasureHunterMap(
    modifier: Modifier,
    treasureDescriptions: List<TreasureDescription>, mapStyle: String
) {
    val locationHelper = LocationHelper(treasureDescriptions)
    val center = locationHelper.center()
    key(treasureDescriptions.isNotEmpty()) {
        MapboxMap(
            modifier = modifier.fillMaxSize(),
            mapViewportState = rememberMapViewportState {
                setCameraOptions {
                    zoom(if (treasureDescriptions.isEmpty()) 1.0 else 13.0)
                    center(center)
                }
            },
            style = { MapStyle(mapStyle) }
        ) {
            val chestImage = rememberIconImage(
                key = R.drawable.chest_closed_small,
                painter = painterResource(R.drawable.chest_closed_small)
            )
            treasureDescriptions.forEach { treasure ->
                PointAnnotation(point = Point.fromLngLat(treasure.longitude, treasure.latitude)) {
                    iconImage = chestImage
                    iconSize = 1.5
                }
                ViewAnnotation(options = viewAnnotationOptions { geometry(Point.fromLngLat(treasure.longitude, treasure.latitude)) }) {
                    Text(
                        text = treasure.id.toString(),
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }
        }
    }
}