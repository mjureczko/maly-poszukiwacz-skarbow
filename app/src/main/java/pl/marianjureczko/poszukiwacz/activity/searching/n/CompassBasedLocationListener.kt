package pl.marianjureczko.poszukiwacz.activity.searching.n

import android.location.Location
import android.location.LocationListener

class CompassBasedLocationListener : LocationListener {
    override fun onLocationChanged(location: Location) {
        print(location)
    }
}