package pl.marianjureczko.poszukiwacz.shared

import pl.marianjureczko.poszukiwacz.usecase.AndroidLocation
import java.io.Serializable

//TODO t: remove, use AndroidLocation instead
data class Coordinates(
    /**
     * north-south position, raging from -90 to 90
     */
    val latitude: Double,
    /**
     * east–west position, ranging from -180 to 180
     */
    val longitude: Double,
    /**
     * Represents the estimated accuracy radius in meters
     */
    val accuracy: Float,
    /**
     * The time when the location was observed in milliseconds since epoch.
     */
    val observedAt: Long
) : Serializable {
    constructor(latitude: Double, longitude: Double) : this(latitude, longitude, 0.0f, 0)

    companion object {
        fun of(location: AndroidLocation) =
            Coordinates(location.latitude, location.longitude, location.accuracy, location.observedAt)
    }
}
