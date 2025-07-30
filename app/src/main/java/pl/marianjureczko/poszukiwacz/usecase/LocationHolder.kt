package pl.marianjureczko.poszukiwacz.usecase

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