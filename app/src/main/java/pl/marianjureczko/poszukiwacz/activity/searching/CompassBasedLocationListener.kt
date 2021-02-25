package pl.marianjureczko.poszukiwacz.activity.searching

import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import pl.marianjureczko.poszukiwacz.TreasureDescription

interface TreasureSupplier {
    fun getTreasure(): TreasureDescription?
}

class CompassBasedLocationListener(
    private val stepsToDo: TextView,
    private val treasureSupplier: TreasureSupplier
) : LocationListener {
    private val TAG = javaClass.simpleName
    private val locationCalculator = LocationCalculator()

    override fun onLocationChanged(location: Location?) {
        Log.d(TAG, "onLocationChanged - location: $location")
        location?.let { location ->
            treasureSupplier.getTreasure()?.let { treasure ->
                stepsToDo.text = locationCalculator.distanceInSteps(treasure, location).toString()
            }
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        Log.d(TAG, "onStatusChanged - provider: $provider")
    }

    override fun onProviderEnabled(provider: String?) {
        Log.d(TAG, "onProviderEnabled - provider: $provider")
    }

    override fun onProviderDisabled(provider: String?) {
        Log.d(TAG, "onProviderDisabled - provider: $provider")
    }

}