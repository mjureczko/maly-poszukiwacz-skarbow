package pl.marianjureczko.poszukiwacz

import android.location.Location
import kotlinx.coroutines.CoroutineScope
import org.mockito.ArgumentMatchers.any
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import pl.marianjureczko.poszukiwacz.screen.searching.UpdateLocationCallback
import pl.marianjureczko.poszukiwacz.shared.port.LocationPort

class TestLocationPort : LocationPort(mock(), mock(), mock(), mock()) {

    lateinit var callback: UpdateLocationCallback
    override fun startFetching(scope: CoroutineScope, callback: UpdateLocationCallback) {
        this.callback = callback
    }

    override fun stopFetching() {/*do nothing*/
    }

    fun updateLocation(latitude: Double, longitude: Double, distanceToTreasure: Float = 0f) {
        val location = mock<Location>()
        given(location.latitude).willReturn(latitude)
        given(location.longitude).willReturn(longitude)
        given(location.distanceTo(any())).willReturn(distanceToTreasure)
        callback.invoke(location)
    }
}