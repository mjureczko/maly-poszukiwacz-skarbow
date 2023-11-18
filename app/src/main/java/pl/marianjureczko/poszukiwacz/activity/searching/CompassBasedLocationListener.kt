package pl.marianjureczko.poszukiwacz.activity.searching

import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.util.Log
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.shared.StorageHelper

interface DataStorageWrapper {
    fun getSelectedForHuntTreasure(): TreasureDescription?
    fun setCurrentLocation(location: Location?, storageHelper: StorageHelper)
}

class CompassBasedLocationListener(
    private val dataStorageWrapper: DataStorageWrapper,
    private val compassPresenter: CompassPresenter,
    private val storageHelper: StorageHelper
) : LocationListener {
    private val TAG = javaClass.simpleName

    override fun onLocationChanged(location: Location) {
        dataStorageWrapper.setCurrentLocation(location, storageHelper)
        compassPresenter.adjustCompassToCurrentLocationAndTreasure(location, dataStorageWrapper.getSelectedForHuntTreasure())
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