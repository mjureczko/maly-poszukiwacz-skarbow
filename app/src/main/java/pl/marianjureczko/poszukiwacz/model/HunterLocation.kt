package pl.marianjureczko.poszukiwacz.model

import org.simpleframework.xml.Element
import pl.marianjureczko.poszukiwacz.activity.treasureselector.Coordinates

/**
 * Serializable to xml version of Coordinates
 */
class HunterLocation {

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
}
