package pl.marianjureczko.poszukiwacz.shared.port

import android.location.Location
import pl.marianjureczko.poszukiwacz.model.AveragedLocation
import pl.marianjureczko.poszukiwacz.usecase.AndroidLocation

class LocationWrapper : AndroidLocation {
    private val location: Location
    override var observedAt: Long = System.currentTimeMillis()
        private set

    constructor(location: Location) {
        this.location = location
    }

    constructor(averagedLocation: AveragedLocation) :
            this(
                latitude = averagedLocation.latitude,
                longitude = averagedLocation.longitude,
                accuracy = 0f,
                observedAt = 0
            )


    constructor(latitude: Double, longitude: Double, accuracy: Float, observedAt: Long) {
        this.location = Location("dummy").apply {
            this.latitude = latitude
            this.longitude = longitude
            this.accuracy = accuracy
        }
        this.observedAt = observedAt
    }

    override fun distanceTo(dest: AndroidLocation): Float {
        val destLocation = when (dest) {
            is LocationWrapper -> dest.location
            else -> LocationWrapper(dest.latitude, dest.longitude, dest.accuracy, dest.observedAt).location
        }
        return location.distanceTo(destLocation)
    }

    override var longitude: Double
        get() = location.longitude
        set(value) {
            location.longitude = value
        }

    override var latitude: Double
        get() = location.latitude
        set(value) {
            location.latitude = value
        }

    override val accuracy: Float
        get() = location.accuracy

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AndroidLocation) return false

        if (latitude != other.latitude) return false
        if (longitude != other.longitude) return false
        if (accuracy != other.accuracy) return false

        return true
    }

    override fun hashCode(): Int {
        var result = 1
        result = 31 * result + latitude.hashCode()
        result = 31 * result + longitude.hashCode()
        result = 31 * result + accuracy.hashCode()
        return result
    }
}