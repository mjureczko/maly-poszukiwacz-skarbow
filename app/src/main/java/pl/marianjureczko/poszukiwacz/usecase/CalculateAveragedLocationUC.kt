package pl.marianjureczko.poszukiwacz.usecase

import org.apache.commons.math3.stat.StatUtils
import pl.marianjureczko.poszukiwacz.model.AveragedLocation

class CalculateAveragedLocationUC {
    operator fun invoke(locations: List<AndroidLocation>): AveragedLocation? {
        require(locations.isNotEmpty())

        val bestLocation = locations.minByOrNull { it.accuracy }!!
        if (bestLocation.accuracy > 200f) {
            return null
        }
        val accuracyThreshold = bestLocation.accuracy * 1.2
        val accurateLocations = locations
            .filter { it.accuracy <= accuracyThreshold }
        val longitude = StatUtils.percentile(accurateLocations.map { it.longitude }.toList().toDoubleArray(), 50.0)
        val latitude = StatUtils.percentile(accurateLocations.map { it.latitude }.toList().toDoubleArray(), 50.0)

        return AveragedLocation(latitude = latitude, longitude = longitude)
    }
}