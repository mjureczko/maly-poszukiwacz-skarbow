package pl.marianjureczko.poszukiwacz.model

import pl.marianjureczko.poszukiwacz.screen.searching.LocationCalculator
import pl.marianjureczko.poszukiwacz.shared.port.LocationWrapper
import pl.marianjureczko.poszukiwacz.usecase.AndroidLocation
import pl.marianjureczko.poszukiwacz.usecase.CalculateAveragedLocationUC
import java.util.Date

data class HunterPath(
    val routeName: String,
    /**
     * Measurements for the next chunk. When te chunk creation criteria are met, the measurements are used to produce the chunk and are remove.
     * Measurements are sorted by time increasingly.
     */
    val locations: List<AndroidLocation>,
    val start: Date?,
    val end: Date?,
    val chunkStart: Date?,
    /**
     * Chunks are in chronological order.
     * Each chunk represents a collection of observed coordinates that were processed to produce a single coordinate.
     */
    val chunkedCoordinates: List<AveragedLocation>
) {

    constructor(routeName: String) : this(
        routeName = routeName,
        locations = mutableListOf(),
        start = null,
        end = null,
        chunkStart = null,
        chunkedCoordinates = mutableListOf()
    )

    fun addLocation(newLocation: AndroidLocation, relevantChangePersister: (HunterPath) -> Unit = {}): HunterPath {
        val observationDate = Date(newLocation.observedAt)
        var updated = copy(end = observationDate)
            .establishStart(observationDate)
            .establishLocations(newLocation, relevantChangePersister)
        return updated
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

    private fun establishStart(date: Date): HunterPath {
        var updated = this
        if (start == null) {
            updated = updated.copy(start = date)
        }
        if (chunkStart == null) {
            updated = updated.copy(chunkStart = date)
        }
        return updated
    }

    /**
     * @param relevantChangePersister - will be called when chunked changed
     */
    private fun establishLocations(
        newLocation: AndroidLocation,
        relevantChangePersister: (HunterPath) -> Unit
    ): HunterPath {
        val date = Date(newLocation.observedAt)
        var result = this
        if (collectedForNextChunk(date)) {
            val averagedLocation = CalculateAveragedLocationUC().invoke(locations)
            if (averagedLocation != null) {
                result = result.copy(
                    chunkedCoordinates = result.chunkedCoordinates.plus(averagedLocation),
                    chunkStart = date,
                    locations = listOf()
                )
                relevantChangePersister(result)
            }
        }
        return result.copy(locations = result.locations.plus(newLocation))
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