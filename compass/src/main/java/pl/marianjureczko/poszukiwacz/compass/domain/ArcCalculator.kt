package pl.marianjureczko.poszukiwacz.compass.domain

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class ArcCalculator {
    fun degree(targetLon: Double, targetLat: Double, currentLon: Double, currentLat: Double): Double {
        val lat1 = Math.toRadians(currentLat)
        val lon1 = Math.toRadians(currentLon)
        val lat2 = Math.toRadians(targetLat)
        val lon2 = Math.toRadians(targetLon)

        val dLon = lon2 - lon1

        val y = sin(dLon) * cos(lat2)
        val x = cos(lat1) * sin(lat2) - sin(lat1) * cos(lat2) * cos(dLon)

        val bearing = Math.toDegrees(atan2(y, x))

        // Normalize to 0-360 range
        return (bearing + 360) % 360
    }
}