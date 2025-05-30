package pl.marianjureczko.poszukiwacz.shared

import com.mapbox.geojson.Point
import pl.marianjureczko.poszukiwacz.model.TreasureDescription

class LocationHelper(treasures: List<TreasureDescription>) {

    data class Borders(var minLng: Double, var maxLng: Double, var minLat: Double, var maxLat: Double)

    private val borders: Borders = if (treasures.isNotEmpty()) {
        val a = treasures[0]
        treasures.fold(Borders(a.longitude, a.longitude, a.latitude, a.latitude)) { acc, td ->
            if (acc.minLng > td.longitude) {
                acc.minLng = td.longitude
            }
            if (acc.minLat > td.latitude) {
                acc.minLat = td.latitude
            }
            if (acc.maxLng < td.longitude) {
                acc.maxLng = td.longitude
            }
            if (acc.maxLat < td.latitude) {
                acc.maxLat = td.latitude
            }
            acc
        }
    } else {
        Borders(0.0, 0.0, 0.0, 0.0)
    }

    fun center(): Point =
        Point.fromLngLat((borders.minLng + borders.maxLng) / 2, (borders.minLat + borders.maxLat) / 2)

    fun southwest(): Point =
        Point.fromLngLat(borders.minLng, borders.minLat)

    fun northeast(): Point =
        Point.fromLngLat(borders.maxLng, borders.maxLat)
}