package pl.marianjureczko.poszukiwacz

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Handler
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import pl.marianjureczko.poszukiwacz.listener.TextViewBasedLocationListener

class LocationPresenter(
    val context: Context,
    val locationListener: TextViewBasedLocationListener,
    val handler: Handler,
    val activity: Activity,
    val locationManager: LocationManager
) : Runnable {

    private val MY_PERMISSION_ACCESS_FINE_LOCATION = 12

    override fun run() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 1000L, 1.0F, locationListener
            )
            handler.postDelayed(this, 1000L)
        } else {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MY_PERMISSION_ACCESS_FINE_LOCATION
            )
        }
    }
}