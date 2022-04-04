package pl.marianjureczko.poszukiwacz.activity.searching

import android.location.Location
import pl.marianjureczko.poszukiwacz.activity.treasureselector.Coordinates
import pl.marianjureczko.poszukiwacz.model.TreasureDescription

class LocationCalculator {

    companion object {
        private const val METERS_TOS_STEP_FACTOR = 1.8
    }

    fun distanceInSteps(treasure: TreasureDescription, userLocation: Location): Int {
        val treasureLocation = treasureLocation(treasure)
        return calculateDistanceInSteps(userLocation, treasureLocation)
    }

    fun distanceInSteps(treasure: TreasureDescription, userCoordinates: Coordinates): Int {
        val treasureLocation = treasureLocation(treasure)
        val userLocation = userLocation(userCoordinates)
        return calculateDistanceInSteps(userLocation, treasureLocation)
    }

    private fun calculateDistanceInSteps(userLocation: Location, treasureLocation: Location) =
        (METERS_TOS_STEP_FACTOR * userLocation.distanceTo(treasureLocation)).toInt()

    private fun userLocation(coordinates: Coordinates): Location {
        val treasureLocation = Location("")
        treasureLocation.longitude = coordinates.longitude
        treasureLocation.latitude = coordinates.latitude
        return treasureLocation
    }

    private fun treasureLocation(treasure: TreasureDescription): Location {
        val treasureLocation = Location("")
        treasureLocation.longitude = treasure.longitude
        treasureLocation.latitude = treasure.latitude
        return treasureLocation
    }
}