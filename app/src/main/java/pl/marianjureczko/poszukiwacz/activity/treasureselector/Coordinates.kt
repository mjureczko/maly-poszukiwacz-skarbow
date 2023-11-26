package pl.marianjureczko.poszukiwacz.activity.treasureselector

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
) : Serializable
