package pl.marianjureczko.poszukiwacz.screen.searching

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.math.sqrt

class CartesianCalculatorQuarterTest {

    companion object {
        @JvmStatic
        fun data(): List<Arguments> {
            return listOf(
                Arguments.of(0f, 2f, 0f, 0f, Quarter.LINE_Y),
                Arguments.of(-2f, 0f, 0f, 0f, Quarter.LINE_X),
                Arguments.of(-2f, -3f, -5f, -6f, Quarter.ONE),
                Arguments.of(-2f, 3f, 0f, 0f, Quarter.TWO),
                Arguments.of(-2f, -3f, 0f, 0f, Quarter.THREE),
                Arguments.of(2f, -3f, 1f, -2f, Quarter.FOUR)
            )
        }
    }

    @ParameterizedTest(name = "treasure({0},{1}) against location({2},{3}) is in {4}")
    @MethodSource("data")
    fun quarter(xTreasure: Double, yTreasure: Double, xLocation: Double, yLocation: Double, expected: Quarter) {
        //when
        val actual = CartesianCalculator().quarter(xTreasure, yTreasure, xLocation, yLocation)

        //then
        assertEquals(expected, actual)
    }
}

class CartesianCalculatorCosTest() {
    companion object {
        @JvmStatic
        fun data(): List<Arguments> {
            return listOf(
                Arguments.of(2f, 2f, 1f, 1f, 0.707106781187),
                Arguments.of(0.5f, sqrt(3.0) / 2, 0f, 0f, 0.866025403785),
                Arguments.of(sqrt(3.0) / 2, 0.5f, 0f, 0f, 0.5)
            )
        }
    }

    @ParameterizedTest(name = "treasure({0},{1}) against location({2},{3}) creates arc with cos {4}")
    @MethodSource("data")
    fun cos(xTreasure: Double, yTreasure: Double, xLocation: Double, yLocation: Double, expected: Double) {
        //when
        val actual = CartesianCalculator().cos(xTreasure, yTreasure, xLocation, yLocation)

        //then
        assertEquals(expected, actual, 0.00001)
    }
}