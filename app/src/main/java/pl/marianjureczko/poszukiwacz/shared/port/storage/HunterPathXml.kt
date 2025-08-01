package pl.marianjureczko.poszukiwacz.shared.port.storage

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import java.util.Date

@Root(name = "HunterPathXml", strict = false)
data class HunterPathXml(

    @field:Element(name = "routeName")
    var routeName: String,

    @field:ElementList(name = "locations")
    var locations: MutableList<AndroidLocationXml>,

    @field:Element(name = "start", required = false)
    var start: Date? = null,

    @field:Element(name = "end", required = false)
    var end: Date? = null,

    @field:Element(name = "chunkStart", required = false)
    var chunkStart: Date? = null,

    @field:ElementList(name = "chunkedCoordinates")
    var chunkedCoordinates: MutableList<AveragedLocationXml>
) {

    constructor() : this("", mutableListOf(), null, null, null, mutableListOf())
}