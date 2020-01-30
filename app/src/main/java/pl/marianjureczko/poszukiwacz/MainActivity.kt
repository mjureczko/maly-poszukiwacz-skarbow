package pl.marianjureczko.poszukiwacz

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), View.OnClickListener  {

    private val MY_PERMISSION_ACCESS_FINE_LOCATION = 12

    private var locationManager: LocationManager? = null

    private lateinit var qrScan: IntentIntegrator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        qrScan = IntentIntegrator(this)
        scanBtn.setOnClickListener(this)

        val latValue = findViewById<TextView>(R.id.latValue)
        latValue.text = "changed"

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager


        val locationListener = MyLocationListener()
        val handler = Handler()
        val context: Context = this
        val activity: Activity = this

        val runnableCode: Runnable = object : Runnable {
                override fun run() { // Do something here on the main thread
                    if (ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        locationManager!!.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            1000L,
                            1.0F,
                            locationListener
                        )
                        handler.postDelayed(this, 1000L)
                    }  else {
                        ActivityCompat.requestPermissions(activity, arrayOf(ACCESS_FINE_LOCATION),MY_PERMISSION_ACCESS_FINE_LOCATION)
                    }

                }
        }
        handler.post(runnableCode)
    }

    override fun onClick(view: View) {
        println("clicked")
        qrScan.initiateScan()
    }

    override fun onActivityResult(requestCode: Int,resultCode: Int,data: Intent?) {
        println("scanning")
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            println("scanned: ${result.contents}")
//            val builder = Dialog.Builder(it)

        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}

class MyLocationListener : LocationListener {
    override fun onLocationChanged(location: Location?) {
//        println("szerokość: ${location?.latitude} długość: ${location?.longitude}")
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