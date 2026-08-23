package pl.marianjureczko.poszukiwacz.shared.port.location

import pl.marianjureczko.poszukiwacz.compass.api.AndroidLocation
import pl.marianjureczko.poszukiwacz.model.AveragedLocation
import pl.marianjureczko.poszukiwacz.usecase.AndroidLocationFactory

class AndroidLocationFactoryImpl : AndroidLocationFactory {
    override fun of(averagedLocation: AveragedLocation): AndroidLocation {
        return AndroidLocation.create(
            latitude = averagedLocation.latitude,
            longitude = averagedLocation.longitude,
            accuracy = 0f,
            observedAt = 0
        )
    }

    override fun of(latitude: Double, longitude: Double, accuracy: Float, observedAt: Long): AndroidLocation {
        return AndroidLocation.create(
            latitude = latitude,
            longitude = longitude,
            accuracy = accuracy,
            observedAt = observedAt
        )
    }
}