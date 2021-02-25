package pl.marianjureczko.poszukiwacz.activity.searching

import android.location.Location
import pl.marianjureczko.poszukiwacz.TreasureDescription


class LocationCalculator {

    companion object {
        private const val METERS_TOS_STEP_FACTOR = 1.8
    }

    fun distanceInSteps(treasure: TreasureDescription, location: Location): Int {
        val treasureLocation = Location("")
        treasureLocation.longitude = treasure.longitude
        treasureLocation.latitude = treasure.latitude
        return (METERS_TOS_STEP_FACTOR * location.distanceTo(treasureLocation)).toInt()
    }
}