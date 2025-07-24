package pl.marianjureczko.poszukiwacz.model

import org.simpleframework.xml.Element
import pl.marianjureczko.poszukiwacz.shared.Coordinates
import java.io.Serializable

/**
 * Serializable to xml version of Coordinates
 */
class CoorinatesXml : Serializable {

    constructor(longitude: Double, latitude: Double) {
        this.longitude = longitude
        this.latitude = latitude
    }

    constructor() : this(0.0, 0.0)
    constructor(coordinates: Coordinates) : this(coordinates.longitude, coordinates.latitude)

    @field:Element
    var longitude: Double
        private set

    @field:Element
    var latitude: Double
        private set

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CoorinatesXml

        if (longitude != other.longitude) return false
        return latitude == other.latitude
    }

    override fun hashCode(): Int {
        var result = longitude.hashCode()
        result = 31 * result + latitude.hashCode()
        return result
    }


}
