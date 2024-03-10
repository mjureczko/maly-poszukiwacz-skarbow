package pl.marianjureczko.poszukiwacz.activity.searching.n

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pl.marianjureczko.poszukiwacz.App
import pl.marianjureczko.poszukiwacz.activity.searching.LocationCalculator
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.shared.StorageHelper
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

//TODO: consider shared ViewModel to avoid reloading content from disc across screens

@HiltViewModel
class SearchingViewModel @Inject constructor(
    private val storageHelper: StorageHelper,
    private val stateHandle: SavedStateHandle
) : ViewModel() {
    private val TAG = javaClass.simpleName
    private var _state = mutableStateOf(SearchingState(loadRoute()))
    private val locationCalculator = LocationCalculator()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            // Get the last known location
            val lastLocation: Location? = locationResult.lastLocation
            val formatter = SimpleDateFormat("h:mm:ss.SSS")
            Log.i(TAG, "${formatter.format(Date())} lastLocation: ${lastLocation?.latitude}")
        }
    }
    val state: State<SearchingState>
        get() = _state

    init {
        viewModelScope.launch {
            fetchLocation()
        }
    }

    private suspend fun fetchLocation() {
        val context: Context = App.getAppContext()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        val locationRequest = LocationRequest.Builder(1_000).build()
        return suspendCoroutine { _ ->
            //The permission should be already granted, but Idea reports error when the check is missing
            if (ContextCompat.checkSelfPermission(App.getAppContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
            }
        }
    }

    private fun loadRoute(): Route {
        return storageHelper.loadRoute(stateHandle.get<String>("route_name")!!)
    }

    override fun onCleared() {
        Log.i(TAG, "onCleared")
        // Stop location updates after obtaining the location
        fusedLocationClient.removeLocationUpdates(locationCallback)
        super.onCleared()
    }
}