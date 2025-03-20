package pl.marianjureczko.poszukiwacz.model

import org.apache.commons.math3.stat.StatUtils
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import pl.marianjureczko.poszukiwacz.screen.searching.LocationCalculator
import pl.marianjureczko.poszukiwacz.shared.Coordinates
import java.io.Serializable
import java.util.Date

@Root
class HunterPath() : Serializable {

    constructor(routeName: String) : this() {
        this.routeName = routeName
    }

    @field:Element
    lateinit var routeName: String

    /**
     * Measurements for the next chunk. When te chunk creation criteria are met, the measurements are used to produce the chunk and are remove.
     * Measurements are sorted by time increasingly.
     */
    @field:ElementList
    private var locations = mutableListOf<HunterLocation>()

    // Public getter for test purpose
    val publicLocations: List<HunterLocation>
        get() = locations

    @field:Element(required = false)
    private var start: Date? = null

    @field:Element(required = false)
    private var end: Date? = null

    @field:Element(required = false)
    private var chunkStart: Date? = null

    /**
     * Chunks are in chronological order.
     */
    @field:ElementList
    private var chunkedCoordinates = mutableListOf<HunterLocation>()

    fun getStartTime(): Date? {
        return start
    }

    fun getEndTime(): Date? {
        return end
    }

    /**
     * @param date exposed for test, production relies on the default value
     * @return  true if chunked changed
     */
    fun addLocation(coordinates: Coordinates, date: Date = Date()): Boolean {
        val newLocation = HunterLocation(coordinates)
        establishEnd(date)
        establishStart(date)
        return establishLocations(newLocation, date)
    }

    fun pathLengthInKm(): Double {
        val calculator = LocationCalculator()
        var result = 0.0
        chunkedCoordinates.forEachIndexed { index, location ->
            if (index > 0) {
                result += calculator.distanceInKm(chunkedCoordinates[index - 1], location)
            }
        }
        return result
    }

    fun pathAsCoordinates(): List<Coordinates> = chunkedCoordinates.map { Coordinates(it.latitude, it.longitude) }

    private fun establishEnd(date: Date) {
        end = date
    }

    private fun establishStart(date: Date) {
        if (start == null) {
            start = date
        }
        if (chunkStart == null) {
            chunkStart = date
        }
    }

    /**
     * @return  true if chunked changed
     */
    private fun establishLocations(newLocation: HunterLocation, date: Date): Boolean {
        val result = if (collectedForChunk(date)) {
            val longitude = StatUtils.percentile(locations.map { it.longitude }.toList().toDoubleArray(), 50.0)
            val latitude = StatUtils.percentile(locations.map { it.latitude }.toList().toDoubleArray(), 50.0)
            chunkedCoordinates.add(HunterLocation(longitude, latitude))
            chunkStart = date
            locations.clear()
            true
        } else {
            false
        }
        locations.add(newLocation)
        return result
    }

    private fun collectedForChunk(newMeasurementDate: Date): Boolean =
        (timeSpanInMilis(newMeasurementDate) >= 20_000L) && (locations.isNotEmpty())

    private fun timeSpanInMilis(newMeasurementDate: Date): Long =
        if (end == null) {
            0L
        } else {
            newMeasurementDate.time - chunkStart!!.time
        }
}