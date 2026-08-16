package pl.marianjureczko.poszukiwacz.compass.data

import android.location.Location

class LocationWrapper(
    override var latitude: Double,
    override var longitude: Double,
    override val accuracy: Float,
    override val observedAt: Long
) : AndroidLocation {

    constructor(location: Location) : this(
        latitude = location.latitude,
        longitude = location.longitude,
        accuracy = location.accuracy,
        observedAt = location.time
    )

    override fun distanceTo(dest: AndroidLocation): Float {
        val results = FloatArray(1)
        Location.distanceBetween(
            latitude, longitude,
            dest.latitude, dest.longitude,
            results
        )
        return results[0]
    }

    override fun toString(): String {
        return "LocationWrapper(latitude=$latitude, longitude=$longitude, accuracy=$accuracy, observedAt=$observedAt)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LocationWrapper

        if (latitude != other.latitude) return false
        if (longitude != other.longitude) return false
        if (accuracy != other.accuracy) return false
        if (observedAt != other.observedAt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = latitude.hashCode()
        result = 31 * result + longitude.hashCode()
        result = 31 * result + accuracy.hashCode()
        result = 31 * result + observedAt.hashCode()
        return result
    }
}