package pl.marianjureczko.poszukiwacz

import com.ocadotechnology.gembus.test.some
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class RouteTest(
    val ids: List<Int>,
    val expectedNextId: Int
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "should give {0} as nextId when {1} treasures are in the route")
        fun data(): Iterable<Array<Any>> {
            return arrayListOf(
                arrayOf(emptyList<Int>(), 1),
                arrayOf(listOf(2), 3),
                arrayOf(listOf(2, 5, 6), 7)
            ).toList()
        }
    }

    @Test
    fun nextId() {
        //given
        val treasures = ids.map { some<TreasureDescription>().copy(id = it) }.toMutableList()
        val route = some<Route>().copy(treasures = treasures)

        //when
        val actual = route.nextId()

        //then
        assertEquals(expectedNextId, actual)
    }
}