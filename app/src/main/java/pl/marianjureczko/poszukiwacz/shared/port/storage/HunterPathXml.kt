package pl.marianjureczko.poszukiwacz.shared.port.storage

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import java.util.Date

@Root
class HunterPathXml {

    @field:Element
    lateinit var routeName: String

    @field:ElementList
    var locations = mutableListOf<AndroidLocationXml>()

    @field:Element(required = false)
    var start: Date? = null

    @field:Element(required = false)
    var end: Date? = null

    @field:Element(required = false)
    var chunkStart: Date? = null

    @field:ElementList
    var chunkedCoordinates = mutableListOf<AveragedLocationXml>()
}