package pl.marianjureczko.poszukiwacz.activity.searching

import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.util.Log
import pl.marianjureczko.poszukiwacz.model.TreasureDescription

interface DataStorageWrapper {
    fun getTreasure(): TreasureDescription?
    fun setCurrentLocation(location: Location?)
}

class CompassBasedLocationListener(
    private val dataStorageWrapper: DataStorageWrapper,
    private val compassPresenter: CompassPresenter
) : LocationListener {
    private val TAG = javaClass.simpleName

    override fun onLocationChanged(location: Location) {
        Log.d(TAG, "onLocationChanged - location: $location")
        dataStorageWrapper.setCurrentLocation(location)
        compassPresenter.adjustCompassToCurrentLocationAndTreasure(location, dataStorageWrapper.getTreasure())
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        Log.d(TAG, "onStatusChanged - provider: $provider")
    }

    override fun onProviderEnabled(provider: String) {
        Log.d(TAG, "onProviderEnabled - provider: $provider")
    }

    override fun onProviderDisabled(provider: String) {
        Log.d(TAG, "onProviderDisabled - provider: $provider")
    }

}