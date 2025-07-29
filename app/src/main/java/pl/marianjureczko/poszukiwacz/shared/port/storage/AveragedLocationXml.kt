package pl.marianjureczko.poszukiwacz.shared.port.storage

import org.simpleframework.xml.Element

class AveragedLocationXml {
    constructor(longitude: Double, latitude: Double) {
        this.longitude = longitude
        this.latitude = latitude
    }

    @field:Element
    var longitude: Double
        private set

    @field:Element
    var latitude: Double
        private set
}