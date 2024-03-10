package pl.marianjureczko.poszukiwacz.activity.searching.n

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.suspendCoroutine


class LocationFetcher(val context: Context) {

    private val TAG = javaClass.simpleName
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var updateLocationCallback: (Location) -> Unit
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            locationResult.lastLocation?.let {
                updateLocationCallback(it)
            }
        }
    }

    fun startFetching(viewModelScope: CoroutineScope, updateLocationCallback: (Location) -> Unit) {
        this.updateLocationCallback = updateLocationCallback
        viewModelScope.launch {
            requestLocation()
        }
    }

    fun stopFetching() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    suspend fun requestLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        val locationRequest = LocationRequest.Builder(1_000).build()
        return suspendCoroutine { _ ->
            //The permission should be already granted, but Idea reports error when the check is missing
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
            }
        }
    }
}