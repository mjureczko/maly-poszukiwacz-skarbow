package pl.marianjureczko.poszukiwacz.screen.searching

import android.location.Location
import pl.marianjureczko.poszukiwacz.model.HunterLocation
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.shared.Coordinates

class LocationCalculator {

    companion object {
        const val METERS_TOS_STEP_FACTOR = 1.8
    }

    fun distanceInKm(start: HunterLocation, end: HunterLocation): Double {
        val locationStart = coordinatesToLocation(start)
        val locationEnd = coordinatesToLocation(end)
        return locationStart.distanceTo(locationEnd).toDouble() / 1000
    }

    fun distanceInSteps(treasure: TreasureDescription, userLocation: Location): Int {
        val treasureLocation = treasureLocation(treasure)
        return calculateDistanceInSteps(userLocation, treasureLocation)
    }

    fun distanceInSteps(treasure: TreasureDescription, userCoordinates: Coordinates): Int {
        val treasureLocation = treasureLocation(treasure)
        val userLocation = coordinatesToLocation(userCoordinates)
        return calculateDistanceInSteps(userLocation, treasureLocation)
    }

    private fun calculateDistanceInSteps(userLocation: Location, treasureLocation: Location) =
        (METERS_TOS_STEP_FACTOR * userLocation.distanceTo(treasureLocation)).toInt()

    private fun coordinatesToLocation(coordinates: Coordinates): Location {
        val treasureLocation = Location("")
        treasureLocation.longitude = coordinates.longitude
        treasureLocation.latitude = coordinates.latitude
        return treasureLocation
    }

    private fun coordinatesToLocation(hunterLocation: HunterLocation): Location {
        val treasureLocation = Location("")
        treasureLocation.longitude = hunterLocation.longitude
        treasureLocation.latitude = hunterLocation.latitude
        return treasureLocation
    }

    private fun treasureLocation(treasure: TreasureDescription): Location {
        val treasureLocation = Location("")
        treasureLocation.longitude = treasure.longitude
        treasureLocation.latitude = treasure.latitude
        return treasureLocation
    }
}