package pl.marianjureczko.poszukiwacz.compass.domain

import android.util.Log
import androidx.compose.runtime.MutableState
import pl.marianjureczko.poszukiwacz.compass.GpsAccuracy
import pl.marianjureczko.poszukiwacz.compass.data.AndroidLocation
import pl.marianjureczko.poszukiwacz.compass.data.HunterPathService
import pl.marianjureczko.poszukiwacz.compass.state.CompassState

class UpdateLocationUC(
    private val locationCalculator: LocationCalculator,
    private val hunterPathService: HunterPathService
) {

    private val TAG = javaClass.simpleName

    operator fun invoke(
        location: AndroidLocation,
        target: AndroidLocation?,
        state: MutableState<CompassState>
    ) {
        val arcCalculator = ArcCalculator()

        Log.i(TAG, "location updated")

        val gpsAccuracy = when {
            location.accuracy <= 30 -> GpsAccuracy.Fine
            location.accuracy <= 100 -> GpsAccuracy.Medium
            else -> GpsAccuracy.Low
        }
        state.value = state.value.copy(
            currentLocation = state.value.currentLocation.updateLocation(location),
            stepsToTreasure = target?.let { locationCalculator.distanceInSteps(it, location) },
            needleRotation = target?.let {
                arcCalculator.degree(
                    it.longitude,
                    it.latitude,
                    location.longitude,
                    location.latitude
                ).toFloat()
            } ?: 0f,
            gpsAccuracy = gpsAccuracy
            //  TODO t: need to be coverd in the main app          ,
//            distancesInSteps = state.value.route.treasures
//                .associate { it.id to locationCalculator.distanceInSteps(it, location) }
//                .toMap()
        )
        hunterPathService.addLocation(location)
    }
}