package pl.marianjureczko.poszukiwacz.compass.domain

import android.util.Log
import androidx.compose.runtime.MutableState
import pl.marianjureczko.poszukiwacz.compass.GpsAccuracy
import pl.marianjureczko.poszukiwacz.compass.data.AndroidLocation
import pl.marianjureczko.poszukiwacz.compass.data.HunterPathService
import pl.marianjureczko.poszukiwacz.compass.state.CompassState

class UpdateLocationUC(
    private val locationCalculator: LocationCalculator
    private val hunterPathService: HunterPathService
) {

    private val TAG = javaClass.simpleName

    operator fun invoke(location: AndroidLocation, state: MutableState<CompassState>) {
        val arcCalculator = ArcCalculator()

        Log.i(TAG, "location updated")

        state.value = state.value.copy(
            currentLocation = state.value.currentLocation.updateLocation(location),
            stepsToTreasure = if (selectedTreasure != null) {
                locationCalculator.distanceInSteps(selectedTreasure, location)
            } else 0,
            needleRotation = if (selectedTreasure != null) {
                arcCalculator.degree(
                    selectedTreasure.longitude,
                    selectedTreasure.latitude,
                    location.longitude,
                    location.latitude
                ).toFloat()
            } else 0f
//            ,
//            distancesInSteps = state.value.route.treasures
//                .associate { it.id to locationCalculator.distanceInSteps(it, location) }
//                .toMap()
        )
        updateAccuracy(location, state)
        hunterPathService.addLocation(location)
    }

    private fun updateAccuracy(location: AndroidLocation, state: MutableState<CompassState>) {
        if (location.accuracy <= 30) {
            state.value = state.value.copy(gpsAccuracy = GpsAccuracy.Fine)
        } else if (location.accuracy <= 100) {
            state.value = state.value.copy(gpsAccuracy = GpsAccuracy.Medium)
        } else {
            state.value = state.value.copy(gpsAccuracy = GpsAccuracy.Low)
        }
    }
}