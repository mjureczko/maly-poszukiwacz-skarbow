package pl.marianjureczko.poszukiwacz

import kotlinx.coroutines.CoroutineScope
import pl.marianjureczko.poszukiwacz.compass.api.AndroidLocation
import pl.marianjureczko.poszukiwacz.compass.api.LocationPort
import pl.marianjureczko.poszukiwacz.usecase.TestLocation

class TestLocationPort : LocationPort {
    private var locationCallback: ((AndroidLocation) -> Unit)? = null

    override fun startFetching(
        coroutineScope: CoroutineScope,
        updateLocationCallback: (AndroidLocation) -> Unit
    ) {
        locationCallback = updateLocationCallback
    }

    override fun stopFetching() {
        locationCallback = null
    }

    fun updateLocation(latitude: Double, longitude: Double, distanceToTreasure: Float = 0f) {
        val location = TestLocation(
            latitude = latitude,
            longitude = longitude,
            observedAt = System.currentTimeMillis()
        )
        locationCallback?.invoke(location)
    }
}
