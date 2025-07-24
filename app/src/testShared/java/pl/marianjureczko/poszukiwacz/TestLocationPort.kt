package pl.marianjureczko.poszukiwacz

import kotlinx.coroutines.CoroutineScope
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import pl.marianjureczko.poszukiwacz.screen.searching.UpdateLocationCallback
import pl.marianjureczko.poszukiwacz.shared.port.LocationPort
import pl.marianjureczko.poszukiwacz.usecase.AndroidLocation

class TestLocationPort : LocationPort(mock(), mock(), mock(), mock()) {

    lateinit var callback: UpdateLocationCallback
    override fun startFetching(scope: CoroutineScope, callback: UpdateLocationCallback) {
        this.callback = callback
    }

    override fun stopFetching() {/*do nothing*/
    }

    fun updateLocation(latitude: Double, longitude: Double, distanceToTreasure: Float = 0f) {
        val location = mock<AndroidLocation>()
        given(location.latitude).willReturn(latitude)
        given(location.longitude).willReturn(longitude)
        given(location.distanceTo(any(AndroidLocation::class.java))).willReturn(distanceToTreasure)
        callback.invoke(location)
    }
}