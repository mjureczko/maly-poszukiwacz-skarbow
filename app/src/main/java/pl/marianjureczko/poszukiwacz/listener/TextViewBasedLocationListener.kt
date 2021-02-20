package pl.marianjureczko.poszukiwacz.listener

import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import pl.marianjureczko.poszukiwacz.CoordinatesFormatter

class TextViewBasedLocationListener(
    private val latValue: TextView?,
    private val longValue: TextView?
) : LocationListener {

    private val TAG = javaClass.simpleName
    private val formatter = CoordinatesFormatter()

    override fun onLocationChanged(location: Location?) {
//        latValue.text = formatter.format(location?.latitude)
//        longValue.text = formatter.format(location?.longitude)
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        Log.d(TAG,"onStatusChanged - provider: $provider")
    }

    override fun onProviderEnabled(provider: String?) {
        Log.d(TAG, "onProviderEnabled - provider: $provider")
    }

    override fun onProviderDisabled(provider: String?) {
        Log.d(TAG, "onProviderDisabled - provider: $provider")
    }

}