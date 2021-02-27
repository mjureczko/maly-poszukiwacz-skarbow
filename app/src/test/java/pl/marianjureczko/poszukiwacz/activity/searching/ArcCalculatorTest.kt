package pl.marianjureczko.poszukiwacz.activity.searching

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class ArcCalculatorTest(
    private val xTreasure: Double,
    private val yTreasure: Double,
    private val xLocation: Double,
    private val yLocation: Double,
    private val expected: Double
) {
    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "treasure({0},{1}) against location({2},{3}) creates direction with arc {4}")
        fun data(): Iterable<Array<Any>> {
            return arrayListOf(
                arrayOf(1f, 0.003, 1f, 0f, 0),
                arrayOf(1f, -0.003, 1f, 0f, 180),
                arrayOf(1.1f, 0f, 1f, 0f, 90),
                arrayOf(-1.1f, 0f, -1f, 0f, 270),
                arrayOf(2f, 2f, 1f, 1f, 45),
                arrayOf(0.5f, Math.sqrt(3.0) / 2, 0f, 0f, 30),
                arrayOf(Math.sqrt(3.0) / 2, 0.5f, 0f, 0f, 60),
                arrayOf(1f, -Math.sqrt(3.0), 0f, 0f, 150),
                arrayOf(Math.sqrt(3.0) / 2, -0.5f, 0f, 0f, 120),

                arrayOf(-0.5f, Math.sqrt(3.0) / 2, 0f, 0f, 330),
                arrayOf(-Math.sqrt(3.0) / 2, 0.5f, 0f, 0f, 300),
                arrayOf(-1f, -Math.sqrt(3.0), 0f, 0f, 210),
                arrayOf(-Math.sqrt(3.0) / 2, -0.5f, 0f, 0f, 240)
            ).toList()
        }
    }

    @Test
    fun cos() {
        //when
        val actual = ArcCalculator().arc(xTreasure, yTreasure, xLocation, yLocation)

        //then
        assertEquals(expected, actual, 0.001)
    }
}