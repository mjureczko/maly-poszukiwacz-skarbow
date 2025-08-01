package pl.marianjureczko.poszukiwacz

import kotlinx.coroutines.CoroutineScope
import org.mockito.Mockito.mock
import pl.marianjureczko.poszukiwacz.screen.searching.UpdateLocationCallback
import pl.marianjureczko.poszukiwacz.shared.port.LocationPort
import pl.marianjureczko.poszukiwacz.usecase.TestLocation

class TestLocationPort : LocationPort(mock(), mock(), mock(), mock()) {

    lateinit var callback: UpdateLocationCallback
    override fun startFetching(scope: CoroutineScope, callback: UpdateLocationCallback) {
        this.callback = callback
    }

    override fun stopFetching() {/*do nothing*/
    }

    fun updateLocation(latitude: Double, longitude: Double, distanceToTreasure: Float = 0f) {
        val location = TestLocation(
            latitude = latitude,
            longitude = longitude,
            observedAt = System.currentTimeMillis()
        )
        location.setDistance(distanceToTreasure)
        callback.invoke(location)
    }
}