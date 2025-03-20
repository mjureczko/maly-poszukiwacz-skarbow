package pl.marianjureczko.poszukiwacz.screen.searching

import kotlin.math.acos

/** Needle degree calculator */
class ArcCalculator {

    private val cartesian = CartesianCalculator()

    fun degree(xTreasure: Double, yTreasure: Double, xLocation: Double, yLocation: Double): Double {
        val cos = cartesian.cos(xTreasure, yTreasure, xLocation, yLocation)
        return when (cartesian.quarter(xTreasure, yTreasure, xLocation, yLocation)) {
            Quarter.LINE_Y -> if (yTreasure > yLocation) 0.0 else 180.0
            Quarter.LINE_X -> if (xTreasure > xLocation) 90.0 else 270.0
            Quarter.ONE -> Math.toDegrees(acos(cos))
            Quarter.FOUR -> Math.toDegrees(acos(cos))

            Quarter.TWO -> 360 - Math.toDegrees(acos(cos))
            Quarter.THREE -> 360 - Math.toDegrees(acos(cos))
        }
    }
}