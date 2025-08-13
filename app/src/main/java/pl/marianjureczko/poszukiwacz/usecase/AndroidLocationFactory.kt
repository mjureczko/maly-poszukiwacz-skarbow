package pl.marianjureczko.poszukiwacz.usecase

import pl.marianjureczko.poszukiwacz.model.AveragedLocation

interface AndroidLocationFactory {
    fun of(averagedLocation: AveragedLocation): AndroidLocation
    fun of(latitude: Double, longitude: Double, accuracy: Float, observedAt: Long): AndroidLocation
}