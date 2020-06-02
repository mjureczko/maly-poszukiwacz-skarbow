package pl.marianjureczko.poszukiwacz.listener

import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.widget.TextView
import pl.marianjureczko.poszukiwacz.CoordinatesFormatter

class TextViewBasedLocationListener(
    private val latValue: TextView,
    private val longValue: TextView
) : LocationListener {

    private val formatter = CoordinatesFormatter()

    override fun onLocationChanged(location: Location?) {
        latValue.text = formatter.format(location?.latitude)
        longValue.text = formatter.format(location?.longitude)
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