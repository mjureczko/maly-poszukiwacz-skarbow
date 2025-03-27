package pl.marianjureczko.poszukiwacz.shared.port

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pl.marianjureczko.poszukiwacz.screen.searching.UpdateLocationCallback
import pl.marianjureczko.poszukiwacz.shared.di.IoDispatcher
import pl.marianjureczko.poszukiwacz.shared.di.MainDispatcher
import kotlin.coroutines.suspendCoroutine

open class LocationPort(
    private val context: Context,
    private val fusedLocationClient: FusedLocationProviderClient,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher
) {

    private val TAG = javaClass.simpleName
    private lateinit var updateLocationCallback: (Location) -> Unit
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            locationResult.lastLocation?.let {
                Log.i(TAG, "New location, lat: " + it.latitude + " long: " + it.longitude)
                updateLocationCallback(it)
            }
        }
    }

    open fun startFetching(
        viewModelScope: CoroutineScope,
        updateLocationCallback: UpdateLocationCallback
    ) {
        fetch(1_000, viewModelScope, updateLocationCallback)
        // after between screen navigation location updating may stop, hence needs to be retriggered periodically
        viewModelScope.launch(ioDispatcher) {
            delay(20_000)
            stopFetching()
            fetch(1_000, viewModelScope, updateLocationCallback)
        }
    }

    open fun stopFetching() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun fetch(
        interval: Long,
        viewModelScope: CoroutineScope,
        updateLocationCallback: UpdateLocationCallback
    ) {
        this.updateLocationCallback = updateLocationCallback
        viewModelScope.launch(mainDispatcher) {
            requestLocation(interval)
        }
    }

    private suspend fun requestLocation(interval: Long = 1_000) {
        val locationRequest = LocationRequest.Builder(interval).build()
        return suspendCoroutine { _ ->
            //The permission should be already granted, but Idea reports error when the check is missing
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
            }
        }
    }
}