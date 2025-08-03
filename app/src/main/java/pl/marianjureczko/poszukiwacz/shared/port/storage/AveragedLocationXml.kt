package pl.marianjureczko.poszukiwacz.shared.port.storage

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root//(name = "AveragedLocationXml", strict = false)
data class AveragedLocationXml(
    @field:Element(name = "longitude")
    var longitude: Double = Double.NaN,

    @field:Element(name = "latitude")
    var latitude: Double = Double.NaN
)