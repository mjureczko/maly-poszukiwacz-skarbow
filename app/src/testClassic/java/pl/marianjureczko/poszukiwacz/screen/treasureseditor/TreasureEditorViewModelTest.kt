package pl.marianjureczko.poszukiwacz.screen.treasureseditor

import androidx.lifecycle.SavedStateHandle
import com.ocadotechnology.gembus.test.some
import com.ocadotechnology.gembus.test.someString
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import pl.marianjureczko.poszukiwacz.TestLocationPort
import pl.marianjureczko.poszukiwacz.TestStoragePort
import pl.marianjureczko.poszukiwacz.any
import pl.marianjureczko.poszukiwacz.model.Route

class TreasureEditorViewModelTest{
    @Test
    fun shouldUpdateLocationInState_whenLocationPortProvidesNewOne() {
        // given
        val locationPort = TestLocationPort()
        val stateHandleMock = mock<SavedStateHandle>()
        val routeName = someString()
        val route = some<Route> { name = routeName }
        val storage = TestStoragePort()
        storage.routes[routeName] = route
        given(stateHandleMock.get<String>(any(String::class.java))).willReturn(routeName)
        val viewModel = TreasureEditorViewModel(storage, stateHandleMock, locationPort)
        val newLatitude = some<Double>()
        val newLongitude = some<Double>()

        //when
        locationPort.updateLocation(newLatitude, newLongitude)

        //then
        val actual = viewModel.state.value.currentLocation!!
        assertThat(actual.latitude).isEqualTo(newLatitude)
        assertThat(actual.longitude).isEqualTo(newLongitude)
    }
}