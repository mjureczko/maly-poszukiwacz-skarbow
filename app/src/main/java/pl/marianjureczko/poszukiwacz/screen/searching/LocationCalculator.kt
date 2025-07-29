package pl.marianjureczko.poszukiwacz.screen.searching

import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.shared.port.LocationWrapper
import pl.marianjureczko.poszukiwacz.usecase.AndroidLocation

class LocationCalculator {

    companion object {
        const val METERS_TO_STEPS_FACTOR = 1.8
    }

    fun distanceInKm(start: AndroidLocation, end: AndroidLocation): Double {
        return start.distanceTo(end).toDouble() / 1000
    }

    fun distanceInSteps(treasure: TreasureDescription, userLocation: AndroidLocation): Int {
        val treasureLocation = treasureLocation(treasure)
        return calculateDistanceInSteps(userLocation, treasureLocation)
    }

    private fun calculateDistanceInSteps(userLocation: AndroidLocation, treasureLocation: AndroidLocation) =
        (METERS_TO_STEPS_FACTOR * userLocation.distanceTo(treasureLocation)).toInt()

    private fun treasureLocation(treasure: TreasureDescription): AndroidLocation {
        val treasureLocation =
            LocationWrapper(latitude = treasure.latitude, longitude = treasure.longitude, accuracy = 0f, observedAt = 0)
        return treasureLocation
    }
}