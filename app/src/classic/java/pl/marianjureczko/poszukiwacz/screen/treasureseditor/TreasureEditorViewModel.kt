package pl.marianjureczko.poszukiwacz.screen.treasureseditor

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.marianjureczko.poszukiwacz.activity.searching.n.LocationPort
import pl.marianjureczko.poszukiwacz.activity.searching.n.PARAMETER_ROUTE_NAME
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.shared.Coordinates
import pl.marianjureczko.poszukiwacz.shared.StorageHelper
import pl.marianjureczko.poszukiwacz.usecase.AddTreasureDescriptionToRoute
import pl.marianjureczko.poszukiwacz.usecase.RemoveTreasureDescriptionFromRoute
import javax.inject.Inject

@HiltViewModel
class TreasureEditorViewModel @Inject constructor(
    private val storageHelper: StorageHelper,
    private val stateHandle: SavedStateHandle,
    private val locationPort: LocationPort,
    private val addTreasureUseCase: AddTreasureDescriptionToRoute,
    private val removeTreasureDescriptionFromRoute: RemoveTreasureDescriptionFromRoute
) : ViewModel() {
    private val TAG = javaClass.simpleName
    private var _state: MutableState<TreasureEditorState> = mutableStateOf(createState())

    val state: State<TreasureEditorState>
        get() = _state

    init {
        locationPort.startFetching(viewModelScope) { location ->
            _state.value = _state.value.copy(currentLocation = Coordinates(location.latitude, location.longitude))
        }
    }

    override fun onCleared() {
        Log.i(TAG, "ViewModel cleared")
        locationPort.stopFetching()
        super.onCleared()
    }

    fun addTreasure() {
        state.value.currentLocation?.let { location ->
            val updateRoute: Route = addTreasureUseCase(state.value.route, location)
            _state.value = _state.value.copy(route = updateRoute)
        }
    }

    fun removeTreasure(treasureDescriptionId: Int) {
        val updatedRoute = removeTreasureDescriptionFromRoute(state.value.route, treasureDescriptionId)
        _state.value = state.value.copy(route = updatedRoute)
    }

    private fun createState(): TreasureEditorState {
        val route = storageHelper.loadRoute(stateHandle.get<String>(PARAMETER_ROUTE_NAME)!!)
        return TreasureEditorState(route, null)
    }
}