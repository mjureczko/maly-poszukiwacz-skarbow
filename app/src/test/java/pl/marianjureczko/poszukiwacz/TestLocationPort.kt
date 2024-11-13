package pl.marianjureczko.poszukiwacz

import android.location.Location
import kotlinx.coroutines.CoroutineScope
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import pl.marianjureczko.poszukiwacz.activity.searching.n.UpdateLocationCallback
import pl.marianjureczko.poszukiwacz.shared.port.LocationPort

class TestLocationPort : LocationPort(mock(), mock(), mock(), mock()) {

    lateinit var callback: UpdateLocationCallback
    override fun startFetching(scope: CoroutineScope, callback: UpdateLocationCallback) {
        this.callback = callback
    }

    override fun stopFetching() {/*do nothing*/}

    fun updateLocation(latitude: Double, longitude: Double) {
        val location = mock<Location>()
        given(location.latitude).willReturn(latitude)
        given(location.longitude).willReturn(longitude)
        callback.invoke(location)
    }
}