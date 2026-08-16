package pl.marianjureczko.poszukiwacz.compass.domain

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class CartesianCalculator {
    /**
     * Converts polar coordinates (distance, angle) to Cartesian coordinates (x, y)
     */
    fun polarToCartesian(distance: Double, angleDegrees: Double): Pair<Double, Double> {
        val angleRadians = Math.toRadians(angleDegrees)
        val x = distance * cos(angleRadians)
        val y = distance * sin(angleRadians)
        return Pair(x, y)
    }

    /**
     * Converts Cartesian coordinates (x, y) to polar coordinates (distance, angle)
     */
    fun cartesianToPolar(x: Double, y: Double): Pair<Double, Double> {
        val distance = sqrt(x * x + y * y)
        val angleRadians = atan2(y, x)
        val angleDegrees = Math.toDegrees(angleRadians)
        return Pair(distance, (angleDegrees + 360) % 360)
    }

    private fun sqrt(value: Double): Double {
        return kotlin.math.sqrt(value)
    }
}