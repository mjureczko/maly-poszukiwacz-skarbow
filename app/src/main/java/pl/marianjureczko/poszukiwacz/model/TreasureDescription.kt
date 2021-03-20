package pl.marianjureczko.poszukiwacz.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root
data class TreasureDescription(
    @field:Element var id: Int,
    @field:Element var latitude: Double,
    @field:Element var longitude: Double,
    @field:Element(required = false) var tipFileName: String?
) {
    constructor(id: Int, latitude: Double, longitude: Double) : this(id, latitude, longitude, null)
    constructor() : this(0, 0.0, 0.0, null)

    fun prettyName(): String = "[$id] $latitude $longitude"
}