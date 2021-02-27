package pl.marianjureczko.poszukiwacz.activity.searching

import kotlin.math.pow
import kotlin.math.sqrt

/**
 *   ^
 * 2 | 1
 * --+-->
 * 3 | 4
 */
enum class Quarter {
    ONE,
    TWO,
    THREE,
    FOUR,
    LINE_X,
    LINE_Y
}

class CartesianCalculator {

    fun quarter(xTreasure: Double, yTreasure: Double, xLocation: Double, yLocation: Double): Quarter {
        return if (yTreasure == yLocation) {
            Quarter.LINE_X
        } else if (xTreasure == xLocation) {
            Quarter.LINE_Y
        } else if (xTreasure > xLocation && yTreasure > yLocation) {
            Quarter.ONE
        } else if (xTreasure < xLocation && yTreasure > yLocation) {
            Quarter.TWO
        } else if (xTreasure < xLocation && yTreasure < yLocation) {
            Quarter.THREE
        } else {
            Quarter.FOUR
        }
    }

    fun cos(xTreasure: Double, yTreasure: Double, xLocation: Double, yLocation: Double): Double {
        val y = yTreasure - yLocation
        val x = xTreasure - xLocation
        val c = sqrt(y.pow(2.0) + x.pow(2.0))
        return y / c
    }
}