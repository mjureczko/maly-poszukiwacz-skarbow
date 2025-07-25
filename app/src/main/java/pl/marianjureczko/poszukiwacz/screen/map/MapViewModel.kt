package pl.marianjureczko.poszukiwacz.screen.map

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.screen.Screens
import pl.marianjureczko.poszukiwacz.shared.port.storage.StoragePort
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val storagePort: StoragePort,
    private val stateHandle: SavedStateHandle
) : ViewModel()  {

    private var _state: MutableState<MapState> = mutableStateOf(createState())

    val state: State<MapState>
        get() = _state

    private fun createState(): MapState {
        val routeName = stateHandle.get<String>(Screens.Map.PARAMETER_ROUTE_NAME)!!
        return MapState(loadRoute(routeName))
    }

    private fun loadRoute(routeName: String): Route {
        return storagePort.loadRoute(routeName)
    }
}