package pl.marianjureczko.poszukiwacz.model

import org.simpleframework.xml.Element
import pl.marianjureczko.poszukiwacz.shared.Coordinates

class AveragedCoordinateXml {
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

    //TODO t: create AveragedCoordinate (no xml) design it in such way that this conversion won't be needed
    fun toCoordinates(): Coordinates =
        Coordinates(longitude = longitude, latitude = latitude, accuracy = 0.0f, observedAt = 0)
}