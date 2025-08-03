package pl.marianjureczko.poszukiwacz.model

import pl.marianjureczko.poszukiwacz.screen.searching.LocationCalculator
import pl.marianjureczko.poszukiwacz.shared.port.LocationWrapper
import pl.marianjureczko.poszukiwacz.usecase.AndroidLocation
import pl.marianjureczko.poszukiwacz.usecase.CalculateAveragedLocationUC
import java.util.Date

class HunterPath() {

    constructor(routeName: String) : this() {
        this.routeName = routeName
    }

    constructor(
        routeName: String,
        locations: MutableList<AndroidLocation>,
        start: Date?,
        end: Date?,
        chunkStart: Date?,
        chunkedCoordinates: MutableList<AveragedLocation>
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
    var locations = mutableListOf<AndroidLocation>()
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
    var chunkedCoordinates = mutableListOf<AveragedLocation>()
        private set

    /**
     * @return  true if chunked changed
     */
    fun addLocation(newLocation: AndroidLocation): Boolean {
        val observationDate = Date(newLocation.observedAt)
        establishEnd(observationDate)
        establishStart(observationDate)
        return establishLocations(newLocation)
    }

    fun pathLengthInKm(): Double {
        val calculator = LocationCalculator()
        var result = 0.0
        chunkedCoordinates.forEachIndexed { index, location ->
            if (index > 0) {
                result += calculator.distanceInKm(
                    LocationWrapper(chunkedCoordinates[index - 1]),
                    LocationWrapper(location)
                )
            }
        }
        return result
    }

    fun path(): List<AveragedLocation> = chunkedCoordinates

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
    private fun establishLocations(newLocation: AndroidLocation): Boolean {
        val date = Date(newLocation.observedAt)
        val result = if (collectedForNextChunk(date)) {
            val averagedLocation = CalculateAveragedLocationUC().invoke(locations)
            if (averagedLocation != null) {
                chunkedCoordinates.add(averagedLocation)
                chunkStart = date
                locations.clear()
                true
            } else {
                false
            }
        } else {
            false
        }
        locations.add(newLocation)
        return result
    }

    private fun collectedForNextChunk(newMeasurementDate: Date): Boolean =
        (timeSpanInMilis(newMeasurementDate) >= 20_000L) && (locations.isNotEmpty())

    private fun timeSpanInMilis(newMeasurementDate: Date): Long =
        if (end == null) {
            0L
        } else {
            newMeasurementDate.time - chunkStart!!.time
        }
}