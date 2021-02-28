package pl.marianjureczko.poszukiwacz.activity.searching

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.lang.Math.sqrt

@RunWith(value = Parameterized::class)
class CartesianCalculatorQuarterTest(
    private val xTreasure: Double,
    private val yTreasure: Double,
    private val xLocation: Double,
    private val yLocation: Double,
    private val expected: Quarter
) {
    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "treasure({0},{1}) against location({2},{3}) is in {4}")
        fun data(): Iterable<Array<Any>> {
            return arrayListOf(
                arrayOf(0f, 2f, 0f, 0f, Quarter.LINE_Y),
                arrayOf(-2f, 0f, 0f, 0f, Quarter.LINE_X),
                arrayOf(-2f, -3f, -5f, -6f, Quarter.ONE),
                arrayOf(-2f, 3f, 0f, 0f, Quarter.TWO),
                arrayOf(-2f, -3f, 0f, 0f, Quarter.THREE),
                arrayOf(2f, -3f, 1f, -2f, Quarter.FOUR)
            ).toList()
        }
    }

    @Test
    fun quarter() {
        //when
        val actual = CartesianCalculator().quarter(xTreasure, yTreasure, xLocation, yLocation)

        //then
        assertEquals(expected, actual)
    }
}

@RunWith(value = Parameterized::class)
class CartesianCalculatorCosTest(
    private val xTreasure: Double,
    private val yTreasure: Double,
    private val xLocation: Double,
    private val yLocation: Double,
    private val expected: Double
) {
    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "treasure({0},{1}) against location({2},{3}) creates arc with cos {4}")
        fun data(): Iterable<Array<Any>> {
            return arrayListOf(
                arrayOf(2f, 2f, 1f, 1f, 0.707106781187),
                arrayOf(0.5f, sqrt(3.0) / 2, 0f, 0f, 0.866025403785),
                arrayOf(sqrt(3.0) / 2, 0.5f, 0f, 0f, 0.5)
            ).toList()
        }
    }

    @Test
    fun cos() {
        //when
        val actual = CartesianCalculator().cos(xTreasure, yTreasure, xLocation, yLocation)

        //then
        assertEquals(expected, actual, 0.00001)
    }
}