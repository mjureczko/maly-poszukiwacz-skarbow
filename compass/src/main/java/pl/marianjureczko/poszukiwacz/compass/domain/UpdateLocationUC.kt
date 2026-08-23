package pl.marianjureczko.poszukiwacz.compass.domain

import androidx.compose.runtime.MutableState
import pl.marianjureczko.poszukiwacz.compass.GpsAccuracy
import pl.marianjureczko.poszukiwacz.compass.api.AndroidLocation
import pl.marianjureczko.poszukiwacz.compass.api.LocationCalculator
import pl.marianjureczko.poszukiwacz.compass.api.LocationUpdateCallback
import pl.marianjureczko.poszukiwacz.compass.viewmodel.CompassState
import javax.inject.Inject

class UpdateLocationUC @Inject constructor(
    private val locationCalculator: LocationCalculator
) {

    private val TAG = javaClass.simpleName

    operator fun invoke(
        location: AndroidLocation,
        target: AndroidLocation?,
        state: MutableState<CompassState>,
        callback: LocationUpdateCallback? = null
    ) {
        val arcCalculator = ArcCalculator()

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
            //TODO t: update lastLocationUpdateTime
        )

        callback?.onLocationUpdated(location)
    }
}
