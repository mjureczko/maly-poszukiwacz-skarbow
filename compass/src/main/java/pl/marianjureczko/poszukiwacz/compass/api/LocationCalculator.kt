package pl.marianjureczko.poszukiwacz.compass.api

import javax.inject.Inject
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class LocationCalculator @Inject constructor() {
    // Average step length in meters
    private val AVERAGE_STEP_LENGTH = 0.7f

    fun distanceInSteps(target: AndroidLocation, userLocation: AndroidLocation): Int {
        val distanceInMeters = distanceInMeters(target, userLocation)
        return (distanceInMeters / AVERAGE_STEP_LENGTH).toInt()
    }

    fun distanceInMeters(target: AndroidLocation, userLocation: AndroidLocation): Float {
        val lat1 = Math.toRadians(userLocation.latitude)
        val lon1 = Math.toRadians(userLocation.longitude)
        val lat2 = Math.toRadians(target.latitude)
        val lon2 = Math.toRadians(target.longitude)

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
        return distanceInKm(
            startLatitude = location1.latitude,
            startLongitude = location1.longitude,
            endLatitude = location2.latitude,
            endLongitude = location2.longitude
        )
    }

    fun distanceInKm(
        startLatitude: Double,
        startLongitude: Double,
        endLatitude: Double,
        endLongitude: Double
    ): Double {
        val lat1 = Math.toRadians(startLatitude)
        val lon1 = Math.toRadians(startLongitude)
        val lat2 = Math.toRadians(endLatitude)
        val lon2 = Math.toRadians(endLongitude)

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