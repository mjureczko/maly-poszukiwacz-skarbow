package pl.marianjureczko.poszukiwacz.shared.port.location

import pl.marianjureczko.poszukiwacz.model.AveragedLocation
import pl.marianjureczko.poszukiwacz.shared.port.LocationWrapper
import pl.marianjureczko.poszukiwacz.usecase.AndroidLocation
import pl.marianjureczko.poszukiwacz.usecase.AndroidLocationFactory

class AndroidLocationFactoryImpl : AndroidLocationFactory {
    override fun of(averagedLocation: AveragedLocation): AndroidLocation {
        return LocationWrapper(
            latitude = averagedLocation.latitude,
            longitude = averagedLocation.longitude,
            accuracy = 0f,
            observedAt = 0
        )
    }

    override fun of(latitude: Double, longitude: Double, accuracy: Float, observedAt: Long): AndroidLocation {
        return LocationWrapper(
            latitude = latitude,
            longitude = longitude,
            accuracy = accuracy,
            observedAt = observedAt
        )
    }
}