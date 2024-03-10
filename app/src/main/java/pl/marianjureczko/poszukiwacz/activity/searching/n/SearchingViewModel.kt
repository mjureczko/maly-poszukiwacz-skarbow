package pl.marianjureczko.poszukiwacz.activity.searching.n

import android.location.Location
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.marianjureczko.poszukiwacz.activity.searching.ArcCalculator
import pl.marianjureczko.poszukiwacz.activity.searching.LocationCalculator
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.shared.StorageHelper
import javax.inject.Inject

//TODO: consider shared ViewModel to avoid reloading content from disc across screens

@HiltViewModel
class SearchingViewModel @Inject constructor(
    private val storageHelper: StorageHelper,
    private val locationFetcher: LocationFetcher,
    private val locationCalculator: LocationCalculator,
    private val stateHandle: SavedStateHandle
) : ViewModel() {
    private val TAG = javaClass.simpleName
    private var _state: MutableState<SearchingState> = mutableStateOf(SearchingState(loadRoute()))
    private val arcCalculator = ArcCalculator()

    val state: State<SearchingState>
        get() = _state

    init {
        locationFetcher.startFetching(viewModelScope) {
            updateLocation(it)
        }
    }

    private fun updateLocation(location: Location) {
        _state.value = _state.value.copy(
            currentLocation = location,
            stepsToTreasure = locationCalculator.distanceInSteps(state.value.currentTreasure, location),
            needleRotation = arcCalculator.degree(
                state.value.currentTreasure.longitude,
                state.value.currentTreasure.latitude,
                location.longitude,
                location.latitude
            ).toFloat()
        )
    }

    //TODO t: route_name to const
    private fun loadRoute(): Route {
//        return Route(
//            "Kalinowice", mutableListOf(
//                TreasureDescription(
//                    1, 25.1, 26.1, null, null
//                )
//            )
//        )
        return storageHelper.loadRoute(stateHandle.get<String>("route_name")!!)
    }

    override fun onCleared() {
        locationFetcher.stopFetching()
        super.onCleared()
    }
}