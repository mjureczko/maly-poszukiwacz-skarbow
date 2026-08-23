package pl.marianjureczko.poszukiwacz.compass.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import pl.marianjureczko.poszukiwacz.compass.api.CompassIoDispatcher
import pl.marianjureczko.poszukiwacz.compass.data.AndroidLocation
import pl.marianjureczko.poszukiwacz.compass.data.HunterPathService
import pl.marianjureczko.poszukiwacz.compass.data.LocationHolder
import pl.marianjureczko.poszukiwacz.compass.data.LocationPort
import pl.marianjureczko.poszukiwacz.compass.domain.ArcCalculator
import pl.marianjureczko.poszukiwacz.compass.domain.LocationCalculator
import pl.marianjureczko.poszukiwacz.compass.domain.UpdateLocationUC
import pl.marianjureczko.poszukiwacz.compass.model.Route
import pl.marianjureczko.poszukiwacz.compass.state.CompassState
import javax.inject.Inject

@HiltViewModel
class CompassViewModel @Inject constructor(
    private val locationPort: LocationPort,
    private val locationCalculator: LocationCalculator,
    private val arcCalculator: ArcCalculator,
    private val updateLocationUC: UpdateLocationUC,
    private val hunterPathService: HunterPathService,
    @CompassIoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _state = mutableStateOf(CompassState())
    val state: State<CompassState> = _state

    private var selectedTreasure: AndroidLocation? = null
    private var route: Route? = null
    private var gpsJob: Job? = null

    init {
        locationPort.startFetching(viewModelScope) { location ->
            updateLocationUC(location, selectedTreasure, _state)
            recalculateIfNeeded()
        }
        scheduleGpsCheck()
    }

    fun setSelectedTreasure(target: AndroidLocation?) {
        this.selectedTreasure = target
        recalculate()
    }

    fun setRoute(route: Route) {
        this.route = route
        recalculate()
    }

    private fun recalculate() {
        val location = _state.value.currentLocation.getCurrentUserLocation()
        if (location != null && selectedTreasure != null && route != null) {
            _state.value = _state.value.copy(
                stepsToTreasure = locationCalculator.distanceInSteps(selectedTreasure!!, location),
                needleRotation = arcCalculator.degree(
                    selectedTreasure!!.longitude,
                    selectedTreasure!!.latitude,
                    location.longitude,
                    location.latitude
                ).toFloat()
            )
        }
    }

    private fun recalculateIfNeeded() {
        if (selectedTreasure != null && route != null) {
            recalculate()
        }
    }

    private fun scheduleGpsCheck() {
        gpsJob = viewModelScope.launch(ioDispatcher) {
            while (isActive) {
                delay(LocationHolder.GPS_NO_SIGNAL_THRESHOLD_IN_MILIS)
                if (hunterPathService.isLocationBeingUpdated() == false) {
                    // GPS signal lost - this will be handled by the state update in updateLocationUC
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        locationPort.stopFetching()
        gpsJob?.cancel()
    }
}