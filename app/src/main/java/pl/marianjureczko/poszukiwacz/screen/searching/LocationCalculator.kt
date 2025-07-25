package pl.marianjureczko.poszukiwacz.screen.searching

import android.location.Location
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.shared.Coordinates
import pl.marianjureczko.poszukiwacz.shared.port.LocationWrapper
import pl.marianjureczko.poszukiwacz.shared.port.storage.CoorinatesXml
import pl.marianjureczko.poszukiwacz.usecase.AndroidLocation

class LocationCalculator {

    companion object {
        const val METERS_TO_STEPS_FACTOR = 1.8
    }

    fun distanceInKm(start: Coordinates, end: Coordinates): Double {
        val locationStart = coordinatesToLocation(start)
        val locationEnd = coordinatesToLocation(end)
        return locationStart.distanceTo(locationEnd).toDouble() / 1000
    }

    fun distanceInSteps(treasure: TreasureDescription, userLocation: AndroidLocation): Int {
        val treasureLocation = treasureLocation(treasure)
        return calculateDistanceInSteps(userLocation, treasureLocation)
    }

    fun distanceInSteps(treasure: TreasureDescription, userCoordinates: Coordinates): Int {
        val treasureLocation = treasureLocation(treasure)
        val userLocation = coordinatesToLocation(userCoordinates)
        return calculateDistanceInSteps(userLocation, treasureLocation)
    }

    private fun calculateDistanceInSteps(userLocation: AndroidLocation, treasureLocation: AndroidLocation) =
        (METERS_TO_STEPS_FACTOR * userLocation.distanceTo(treasureLocation)).toInt()

    private fun coordinatesToLocation(coordinates: Coordinates): AndroidLocation {
        val treasureLocation = LocationWrapper(latitude = coordinates.latitude, longitude = coordinates.longitude)
        return treasureLocation
    }

    private fun coordinatesToLocation(hunterLocation: CoorinatesXml): Location {
        val treasureLocation = Location("")
        treasureLocation.longitude = hunterLocation.longitude
        treasureLocation.latitude = hunterLocation.latitude
        return treasureLocation
    }

    private fun treasureLocation(treasure: TreasureDescription): AndroidLocation {
        val treasureLocation = LocationWrapper(latitude = treasure.latitude, longitude = treasure.longitude)
        return treasureLocation
    }
}