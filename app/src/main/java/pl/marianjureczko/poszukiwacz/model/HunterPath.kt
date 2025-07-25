package pl.marianjureczko.poszukiwacz.model

import org.apache.commons.math3.stat.StatUtils
import pl.marianjureczko.poszukiwacz.screen.searching.LocationCalculator
import pl.marianjureczko.poszukiwacz.shared.Coordinates
import pl.marianjureczko.poszukiwacz.shared.port.storage.CoorinatesXml
import java.util.Date

class HunterPath() {

    constructor(routeName: String) : this() {
        this.routeName = routeName
    }

    constructor(
        routeName: String,
        locations: MutableList<CoorinatesXml>,
        start: Date?,
        end: Date?,
        chunkStart: Date?,
        chunkedCoordinates: MutableList<AveragedCoordinateXml>
    ) : this() {
        this.routeName = routeName
        this.locations = locations
        this.start = start
        this.end = end
        this.chunkStart = chunkStart
        this.chunkedCoordinates = chunkedCoordinates
    }

    lateinit var routeName: String

    /**
     * Measurements for the next chunk. When te chunk creation criteria are met, the measurements are used to produce the chunk and are remove.
     * Measurements are sorted by time increasingly.
     */
    // Public getter for test purpose
    var locations = mutableListOf<CoorinatesXml>()
        private set

    var start: Date? = null
        private set

    var end: Date? = null
        private set

    var chunkStart: Date? = null
        private set

    /**
     * Chunks are in chronological order.
     * Each chunk represents a collection of observed coordinates that were processed to produce a single coordinate.
     */
    var chunkedCoordinates = mutableListOf<AveragedCoordinateXml>()
        private set

    /**
     * @param date exposed for test, production relies on the default value
     * @return  true if chunked changed
     */
    fun addLocation(coordinates: Coordinates, date: Date = Date()): Boolean {
        val newLocation = CoorinatesXml(coordinates)
        establishEnd(date)
        establishStart(date)
        return establishLocations(newLocation, date)
    }

    fun pathLengthInKm(): Double {
        val calculator = LocationCalculator()
        var result = 0.0
        chunkedCoordinates.forEachIndexed { index, location ->
            if (index > 0) {
                result += calculator.distanceInKm(
                    chunkedCoordinates[index - 1].toCoordinates(),
                    location.toCoordinates()
                )
            }
        }
        return result
    }

    fun pathAsCoordinates(): List<Coordinates> =
        chunkedCoordinates.map { it.toCoordinates() }

    fun isLocationBeingUpdated(): Boolean {
        if (end == null) return true
        val now = Date()
        return (now.time - end!!.time) < 5000
    }

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
    private fun establishLocations(newLocation: CoorinatesXml, date: Date): Boolean {
        val result = if (collectedForChunk(date)) {
            val longitude = StatUtils.percentile(locations.map { it.longitude }.toList().toDoubleArray(), 50.0)
            val latitude = StatUtils.percentile(locations.map { it.latitude }.toList().toDoubleArray(), 50.0)
            chunkedCoordinates.add(AveragedCoordinateXml(longitude, latitude))
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