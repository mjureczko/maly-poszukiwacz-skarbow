package pl.marianjureczko.poszukiwacz.activity.map.n

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.shared.port.StorageHelper
import javax.inject.Inject

const val PARAMETER_ROUTE_NAME_2 = "route_name"

@HiltViewModel
class MapViewModel @Inject constructor(
    private val storageHelper: StorageHelper,
    private val stateHandle: SavedStateHandle
) : ViewModel()  {

    private var _state: MutableState<MapState> = mutableStateOf(createState())

    val state: State<MapState>
        get() = _state

    private fun createState(): MapState {
        val routeName = stateHandle.get<String>(PARAMETER_ROUTE_NAME_2)!!
        return MapState(loadRoute(routeName))
    }

    private fun loadRoute(routeName: String): Route {
        return storageHelper.loadRoute(routeName)
    }
}