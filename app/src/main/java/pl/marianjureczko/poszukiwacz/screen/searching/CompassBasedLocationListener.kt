package pl.marianjureczko.poszukiwacz.screen.searching

import android.location.Location
import android.location.LocationListener

class CompassBasedLocationListener : LocationListener {
    override fun onLocationChanged(location: Location) {
        print(location)
    }
}