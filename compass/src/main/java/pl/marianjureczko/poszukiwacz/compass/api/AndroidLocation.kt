package pl.marianjureczko.poszukiwacz.compass.api

import pl.marianjureczko.poszukiwacz.compass.data.LocationWrapper

interface AndroidLocation {
    var longitude: Double
    var latitude: Double

    /** Represents the estimated accuracy radius in meters */
    val accuracy: Float

    /**  The time when the location was observed in milliseconds since epoch. */
    val observedAt: Long

    companion object {
        /**
         * Factory creating an [AndroidLocation] from raw coordinates.
         * Hides the concrete implementation ([LocationWrapper]) from module clients.
         */
        fun create(
            latitude: Double,
            longitude: Double,
            accuracy: Float,
            observedAt: Long
        ): AndroidLocation = LocationWrapper(
            latitude = latitude,
            longitude = longitude,
            accuracy = accuracy,
            observedAt = observedAt
        )

        /**
         * Simplified constructor that uses dummy values for accurace and observedAt
         */
        fun create(latitude: Double, longitude: Double): AndroidLocation = LocationWrapper(
            latitude = latitude,
            longitude = longitude,
            accuracy = 0.0f,
            observedAt = 0
        )

        /**
         * Factory creating an [AndroidLocation] from the platform location.
         * Hides the concrete implementation ([LocationWrapper]) from module clients.
         */
        fun create(location: android.location.Location): AndroidLocation = LocationWrapper(location)
    }
}
