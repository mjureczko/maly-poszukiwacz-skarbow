package pl.marianjureczko.poszukiwacz

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Handler
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class LocationPresenter(
    private val activity: Activity,
    private val locationListener: LocationListener,
    private val handler: Handler,
    private val locationManager: LocationManager
) : Runnable {

    companion object {
        private const val MY_PERMISSION_ACCESS_FINE_LOCATION = 12
    }

    override fun run() {
        //The permission should be already granted, but Idea reports error when the check is missing
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 1.0F, locationListener)
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