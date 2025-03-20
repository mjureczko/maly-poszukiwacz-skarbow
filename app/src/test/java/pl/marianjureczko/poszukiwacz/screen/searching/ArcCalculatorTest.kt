package pl.marianjureczko.poszukiwacz.screen.searching

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.math.sqrt

class ArcCalculatorTest {

    @ParameterizedTest(name = "treasure({0},{1}) against location({2},{3}) creates direction with arc {4}")
    @MethodSource("data")
    fun cos(xTreasure: Double, yTreasure: Double, xLocation: Double, yLocation: Double, expected: Double) {
        //when
        val actual = ArcCalculator().degree(xTreasure, yTreasure, xLocation, yLocation)

        //then
        assertEquals(expected, actual, 0.001)
    }

    companion object {
        @JvmStatic
        fun data(): List<Arguments> {
            return listOf(
                Arguments.of(1f, 0.003, 1f, 0f, 0),
                Arguments.of(1f, -0.003, 1f, 0f, 180),
                Arguments.of(1.1f, 0f, 1f, 0f, 90),
                Arguments.of(-1.1f, 0f, -1f, 0f, 270),
                Arguments.of(2f, 2f, 1f, 1f, 45),
                Arguments.of(0.5f, sqrt(3.0) / 2, 0f, 0f, 30),
                Arguments.of(sqrt(3.0) / 2, 0.5f, 0f, 0f, 60),
                Arguments.of(1f, -sqrt(3.0), 0f, 0f, 150),
                Arguments.of(sqrt(3.0) / 2, -0.5f, 0f, 0f, 120),

                Arguments.of(-0.5f, sqrt(3.0) / 2, 0f, 0f, 330),
                Arguments.of(-sqrt(3.0) / 2, 0.5f, 0f, 0f, 300),
                Arguments.of(-1f, -sqrt(3.0), 0f, 0f, 210),
                Arguments.of(-sqrt(3.0) / 2, -0.5f, 0f, 0f, 240)
            ).toList()
        }
    }

    @Test
    fun cos() {

    }
}