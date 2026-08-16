package pl.marianjureczko.poszukiwacz.compass.domain

import pl.marianjureczko.poszukiwacz.compass.data.AndroidLocation
import pl.marianjureczko.poszukiwacz.compass.model.TreasureDescription
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class LocationCalculator {
    // Average step length in meters
    private val AVERAGE_STEP_LENGTH = 0.7f

    fun distanceInSteps(treasure: TreasureDescription, userLocation: AndroidLocation): Int {
        val distanceInMeters = distanceInMeters(treasure, userLocation)
        return (distanceInMeters / AVERAGE_STEP_LENGTH).toInt()
    }

    fun distanceInMeters(treasure: TreasureDescription, userLocation: AndroidLocation): Float {
        val lat1 = Math.toRadians(userLocation.latitude)
        val lon1 = Math.toRadians(userLocation.longitude)
        val lat2 = Math.toRadians(treasure.latitude)
        val lon2 = Math.toRadians(treasure.longitude)

        val dLat = lat2 - lat1
        val dLon = lon2 - lon1

        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(lat1) * cos(lat2) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        // Earth's radius in meters
        val R = 6371000.0
        return (R * c).toFloat()
    }

    fun distanceInKm(location1: AndroidLocation, location2: AndroidLocation): Double {
        val lat1 = Math.toRadians(location1.latitude)
        val lon1 = Math.toRadians(location1.longitude)
        val lat2 = Math.toRadians(location2.latitude)
        val lon2 = Math.toRadians(location2.longitude)

        val dLat = lat2 - lat1
        val dLon = lon2 - lon1

        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(lat1) * cos(lat2) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        // Earth's radius in kilometers
        val R = 6371.0
        return R * c
    }
}