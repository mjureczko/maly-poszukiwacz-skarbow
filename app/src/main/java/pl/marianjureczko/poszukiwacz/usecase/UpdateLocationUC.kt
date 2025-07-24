package pl.marianjureczko.poszukiwacz.usecase

import android.util.Log
import androidx.compose.runtime.MutableState
import pl.marianjureczko.poszukiwacz.screen.searching.ArcCalculator
import pl.marianjureczko.poszukiwacz.screen.searching.GpsAccuracy
import pl.marianjureczko.poszukiwacz.screen.searching.LocationCalculator
import pl.marianjureczko.poszukiwacz.screen.searching.SharedState
import pl.marianjureczko.poszukiwacz.shared.Coordinates
import pl.marianjureczko.poszukiwacz.shared.port.StorageHelper

class UpdateLocationUC(
    private val storage: StorageHelper,
    private val locationCalculator: LocationCalculator,
) {

    private val TAG = javaClass.simpleName

    operator fun invoke(location: AndroidLocation, state: MutableState<SharedState>) {
        val arcCalculator = ArcCalculator()

        Log.i(TAG, "location updated")
        val selectedTreasure = state.value.selectedTreasureDescription()
        state.value = state.value.copy(
            currentLocation = location,
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
            } else 0f,
            distancesInSteps = state.value.route.treasures
                .associate { it.id to locationCalculator.distanceInSteps(it, location) }
                .toMap()
        )
        updateAccuracy(location, state)
        val currentCoordinates =
            Coordinates(location.latitude, location.longitude, location.accuracy, location.observedAt)
        if (state.value.hunterPath.addLocation(currentCoordinates)) {
            storage.save(state.value.hunterPath)
        }
    }

    private fun updateAccuracy(location: AndroidLocation, state: MutableState<SharedState>) {
        if (location.accuracy <= 30) {
            state.value = state.value.copy(gpsAccuracy = GpsAccuracy.Fine)
        } else if (location.accuracy <= 100) {
            state.value = state.value.copy(gpsAccuracy = GpsAccuracy.Medium)
        } else {
            state.value = state.value.copy(gpsAccuracy = GpsAccuracy.Low)
        }
    }
}