package pl.marianjureczko.poszukiwacz.shared.port.storage

import org.simpleframework.xml.Element
import pl.marianjureczko.poszukiwacz.shared.port.LocationWrapper
import pl.marianjureczko.poszukiwacz.usecase.AndroidLocation

class AndroidLocationXml {

    constructor(longitude: Double, latitude: Double, accuracy: Float, observedAt: Long) {
        this.longitude = longitude
        this.latitude = latitude
        this.accuracy = accuracy
        this.observedAt = observedAt
    }

    constructor(location: AndroidLocation) : this(
        longitude = location.longitude,
        latitude = location.latitude,
        accuracy = location.accuracy,
        observedAt = location.observedAt
    )

    constructor() : this(0.0, 0.0, 0.0f, System.currentTimeMillis())

    @field:Element
    var longitude: Double
        private set

    @field:Element
    var latitude: Double
        private set

    @field:Element(required = false)
    var accuracy: Float = 0.0f
        private set

    @field:Element(required = false)
    var observedAt: Long = System.currentTimeMillis()
        private set

    fun toAndroidLocation(): AndroidLocation {
        return LocationWrapper(
            longitude = longitude,
            latitude = latitude,
            accuracy = accuracy,
            observedAt = observedAt
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AndroidLocationXml

        if (longitude != other.longitude) return false
        return latitude == other.latitude
    }

    override fun hashCode(): Int {
        var result = longitude.hashCode()
        result = 31 * result + latitude.hashCode()
        return result
    }
}
