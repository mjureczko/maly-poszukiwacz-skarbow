package pl.marianjureczko.poszukiwacz.compass.viewmodel

import androidx.compose.runtime.MutableState
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
import pl.marianjureczko.poszukiwacz.compass.GpsAccuracy
import pl.marianjureczko.poszukiwacz.compass.api.AndroidLocation
import pl.marianjureczko.poszukiwacz.compass.api.CompassIoDispatcher
import pl.marianjureczko.poszukiwacz.compass.api.LocationCalculator
import pl.marianjureczko.poszukiwacz.compass.api.LocationPort
import pl.marianjureczko.poszukiwacz.compass.api.LocationUpdateCallback
import pl.marianjureczko.poszukiwacz.compass.data.LocationHolder
import pl.marianjureczko.poszukiwacz.compass.domain.ArcCalculator
import pl.marianjureczko.poszukiwacz.compass.domain.UpdateLocationUC
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class CompassViewModel @Inject constructor(
    private val locationPort: LocationPort,
    private val locationCalculator: LocationCalculator,
    private val updateLocationUC: UpdateLocationUC,
    @CompassIoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val arcCalculator: ArcCalculator = ArcCalculator()
    private val _state = mutableStateOf(CompassState())
    val state: State<CompassState> = _state

    private var selectedTreasure: AndroidLocation? = null
    private var locationUpdateCallback: LocationUpdateCallback? = null
    private var gpsJob: Job? = null
    private var started: Boolean = false

    fun start() {
        if (!started) {
            locationPort.startFetching(viewModelScope) { location ->
                updateLocationUC(location, selectedTreasure, _state, locationUpdateCallback)
                _state.value = _state.value.copy(lastLocationUpdateTime = Date(location.observedAt))
            }
            scheduleGpsCheck()
            started = true
        }
    }

    //visibility for tests
    fun getMutableStateForTest(): MutableState<CompassState> = _state

    fun setLocationUpdateCallback(callback: LocationUpdateCallback?) {
        this.locationUpdateCallback = callback
    }

    fun setSelectedTreasure(target: AndroidLocation?) {
        this.selectedTreasure = target
        recalculate()
    }

    private fun recalculate() {
        val location = _state.value.currentLocation.getCurrentUserLocation()
        if (location != null && selectedTreasure != null) {
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
        if (selectedTreasure != null) {
            recalculate()
        }
    }

    private fun scheduleGpsCheck() {
        gpsJob = viewModelScope.launch(ioDispatcher) {
            while (isActive) {
                delay(LocationHolder.GPS_NO_SIGNAL_THRESHOLD_IN_MILIS)
                val isUpdated = _state.value.lastLocationUpdateTime?.let {
                    (Date().time - it.time) < LocationHolder.GPS_NO_SIGNAL_THRESHOLD_IN_MILIS
                } ?: true
                if (!isUpdated) {
                    _state.value = _state.value.copy(gpsAccuracy = GpsAccuracy.NoSignal)
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