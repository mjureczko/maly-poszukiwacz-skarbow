# Mały Poszukiwacz Skarbów

GPLv3 (https://www.gnu.org/licenses/gpl-3.0.html)

## Available apps:

- [Little Treasure Hunter](https://play.google.com/store/apps/details?id=pl.marianjureczko.poszukiwacz&hl=en)
- [Kalinowice](https://play.google.com/store/apps/details?id=pl.marianjureczko.poszukiwacz.kalinowice&hl=en)
- [Pęgów](https://play.google.com/store/apps/details?id=pl.marianjureczko.poszukiwacz.pegow)

Copmilation fails with

```

> Task :compass:compileDebugKotlin FAILED
e: file:///home/marian/Projects/maly-poszukiwacz-skarbow/compass/src/main/java/pl/marianjureczko/poszukiwacz/compass/api/CompassAndSteps.kt:14:17 Unresolved reference 'hilt'.
e: file:///home/marian/Projects/maly-poszukiwacz-skarbow/compass/src/main/java/pl/marianjureczko/poszukiwacz/compass/api/CompassAndSteps.kt:31:39 Unresolved reference 'hiltViewModel'.
e: file:///home/marian/Projects/maly-poszukiwacz-skarbow/compass/src/main/java/pl/marianjureczko/poszukiwacz/compass/data/HunterPathPort.kt:4:49 Unresolved reference 'HunterPath'.
e: file:///home/marian/Projects/maly-poszukiwacz-skarbow/compass/src/main/java/pl/marianjureczko/poszukiwacz/compass/domain/UpdateLocationUC.kt:4:46 Unresolved reference 'compass'.
e: file:///home/marian/Projects/maly-poszukiwacz-skarbow/compass/src/main/java/pl/marianjureczko/poszukiwacz/compass/domain/UpdateLocationUC.kt:21:41 Unresolved reference 'GpsAccuracy'.
e: file:///home/marian/Projects/maly-poszukiwacz-skarbow/compass/src/main/java/pl/marianjureczko/poszukiwacz/compass/domain/UpdateLocationUC.kt:22:42 Unresolved reference 'GpsAccuracy'.
e: file:///home/marian/Projects/maly-poszukiwacz-skarbow/compass/src/main/java/pl/marianjureczko/poszukiwacz/compass/domain/UpdateLocationUC.kt:23:21 Unresolved reference 'GpsAccuracy'.
e: file:///home/marian/Projects/maly-poszukiwacz-skarbow/compass/src/main/java/pl/marianjureczko/poszukiwacz/compass/model/TreasureDescription.kt:5:38 Unresolved reference 'shared'.
e: file:///home/marian/Projects/maly-poszukiwacz-skarbow/compass/src/main/java/pl/marianjureczko/poszukiwacz/compass/model/TreasureDescription.kt:40:43 Unresolved reference 'StoragePort'.
e: file:///home/marian/Projects/maly-poszukiwacz-skarbow/compass/src/main/java/pl/marianjureczko/poszukiwacz/compass/model/TreasureDescription.kt:42:41 Unresolved reference 'newPhotoFile'.
e: file:///home/marian/Projects/maly-poszukiwacz-skarbow/compass/src/main/java/pl/marianjureczko/poszukiwacz/compass/viewmodel/CompassViewModel.kt:83:23 Unresolved reference 'LocationHolder'.

FAILURE: Build failed with an exception.
``` 

Explain why and what need to be changed to pass compilation.

interface HunterPathPort {
fun addLocation(location: AndroidLocation): HunterPath
fun isLocationBeingUpdated(): Boolean
}

```kt
data class LocationHolder(
    private val currentUserLocation: AndroidLocation? = null,

    /**
     * Temporarily stores a location update with low accuracy.
     * If a more accurate location is received, this one is discarded.
     * Otherwise, it will eventually be used as the current user location.
     */
    private var locationCandidate: AndroidLocation? = null

) {
    val goodAccuracyThresholdInMeters = 50f

    companion object {
        val GPS_NO_SIGNAL_THRESHOLD_IN_MILIS = 5000L
    }

    fun updateLocation(newLocation: AndroidLocation): LocationHolder {
        return if (newLocation.accuracy < goodAccuracyThresholdInMeters) {
            updateCurrentLocation(newLocation)
        } else {
            val timeSinceCurrentLocationObservation = timeSinceCurrentLocationObservation(newLocation)
            val betterAccuracyLocation = selectWithBetterAccuracy(newLocation, locationCandidate)
            if (GPS_NO_SIGNAL_THRESHOLD_IN_MILIS - 1000L < timeSinceCurrentLocationObservation) {
                updateCurrentLocation(betterAccuracyLocation)
            } else {
                copy(locationCandidate = betterAccuracyLocation)
            }
        }
    }

    private fun selectWithBetterAccuracy(
        newLocation: AndroidLocation,
        locationCandidate: AndroidLocation?
    ): AndroidLocation {
        return if (locationCandidate == null) {
            newLocation
        } else {
            if (newLocation.accuracy < locationCandidate.accuracy) {
                newLocation
            } else {
                locationCandidate
            }
        }
    }

    private fun updateCurrentLocation(newLocation: AndroidLocation) =
        copy(currentUserLocation = newLocation, locationCandidate = null)

    /** returns approximately infinity when there is no current location */
    private fun timeSinceCurrentLocationObservation(newLocation: AndroidLocation): Long {
        val observedAt = currentUserLocation?.observedAt ?: 0L
        return newLocation.observedAt - observedAt
    }

    fun getCurrentUserLocation(): AndroidLocation? {
        return currentUserLocation
    }
}
```