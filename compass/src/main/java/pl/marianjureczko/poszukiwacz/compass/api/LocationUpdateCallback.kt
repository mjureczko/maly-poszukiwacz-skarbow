package pl.marianjureczko.poszukiwacz.compass.api

/**
 * Callback interface for receiving location updates from the compass module.
 */
fun interface LocationUpdateCallback {
    /**
     * Called when the location is updated in the compass module.
     *
     * @param location The updated AndroidLocation
     */
    fun onLocationUpdated(location: AndroidLocation)
}