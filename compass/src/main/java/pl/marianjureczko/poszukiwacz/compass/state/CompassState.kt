package pl.marianjureczko.poszukiwacz.compass.state

import pl.marianjureczko.poszukiwacz.compass.GpsAccuracy
import pl.marianjureczko.poszukiwacz.compass.data.LocationHolder

data class CompassState(
    val needleRotation: Float = 0f,
    val gpsAccuracy: GpsAccuracy = GpsAccuracy.Fine,
    val stepsToTreasure: Int? = null,
    val currentLocation: LocationHolder = LocationHolder(),
)