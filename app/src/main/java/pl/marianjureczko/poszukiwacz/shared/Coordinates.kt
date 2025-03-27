package pl.marianjureczko.poszukiwacz.shared

import android.location.Location
import java.io.Serializable

data class Coordinates(
    /**
     * north-south position, raging from -90 to 90
     */
    val latitude: Double,
    /**
     * east–west position, ranging from -180 to 180
     */
    val longitude: Double
) : Serializable {
    companion object {
        fun of(location: Location) = Coordinates(location.latitude, location.longitude)
    }
}
