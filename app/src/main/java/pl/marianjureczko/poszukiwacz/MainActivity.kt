package pl.marianjureczko.poszukiwacz

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val MY_PERMISSION_ACCESS_FINE_LOCATION = 12

    private var locationManager: LocationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 1.0F, MyLocationListener())
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(ACCESS_FINE_LOCATION),MY_PERMISSION_ACCESS_FINE_LOCATION)
        }

    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
//    {
//        switch(requestCode) {
//                case MY_PERMISSION_ACCESS_FINE_LOCATION : {
//                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                        // permission was granted
//                    } else {
//                        // permission denied
//                    }
//                    break;
//                }
//
//            }
//        }
//    }
}

class MyLocationListener : LocationListener {
    override fun onLocationChanged(location: Location?) {
        println("szerokość: ${location?.latitude} długość: ${location?.longitude}")
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        println("onStatusChanged - provider: $provider")
    }

    override fun onProviderEnabled(provider: String?) {
        println("onProviderEnabled - provider: $provider")
    }

    override fun onProviderDisabled(provider: String?) {
        println("onProviderDisabled - provider: $provider")
    }

}