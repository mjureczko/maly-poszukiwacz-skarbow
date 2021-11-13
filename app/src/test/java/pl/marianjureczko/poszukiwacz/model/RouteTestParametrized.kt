package pl.marianjureczko.poszukiwacz.model

import com.ocadotechnology.gembus.test.some
import org.apache.commons.io.FileUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import pl.marianjureczko.poszukiwacz.shared.StorageHelper
import pl.marianjureczko.poszukiwacz.shared.TestContext
import java.io.File

@RunWith(value = Parameterized::class)
class RouteTestParametrized(
    val ids: List<Int>,
    val expectedNextId: Int
) {
    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "SHOULD give {0} as nextId WHEN {1} treasures are in the route")
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

class RouteTest {
    private val context = TestContext()
    private val storageHelper = StorageHelper(context)

    @Before
    fun cleanup() {
        FileUtils.deleteDirectory(File(context.filesDir.absolutePath + StorageHelper.routesDirectory))
    }

    @Test
    fun `SHOULD remove treasureDescription files WHEN removing the TreasureDescription from Route`() {
        //given
        val route = RouteArranger.savedWithFiles(storageHelper)
        val toRemove = route.treasures[0]

        //when
        route.remove(toRemove, storageHelper)

        //then
        assertThat(route.treasures).doesNotContain(toRemove)
        assertThat(File(toRemove.photoFileName).exists()).isFalse()
        assertThat(File(toRemove.tipFileName).exists()).isFalse()
    }

    @Test
    fun `SHOULD add prefix to each photo and sound file`() {
        //given
        val route = some<Route>()
        val prefix = some<String>()
        val expected = route.treasures
            .map {
                it.copy(
                    photoFileName = prefix + it.photoFileName,
                    tipFileName = prefix + it.tipFileName
                )
            }
            .map { it.id to it }
            .toMap()

        //when
        route.addPrefixToFilesPaths(prefix)

        //then
        assertThat(route.treasures.size).isGreaterThan(0)
        route.treasures.forEach {
            val correspondingTreasure = expected[it.id]!!
            assertThat(it.photoFileName).isEqualTo(correspondingTreasure.photoFileName)
            assertThat(it.tipFileName).isEqualTo(correspondingTreasure.tipFileName)
        }
    }

    @Test
    fun `SHOULD not add prefix to photo nor sound file WHEN the file is null`() {
        //given
        val treasure = some<TreasureDescription>().copy(
            tipFileName = null,
            photoFileName = null
        )
        val route = some<Route>().copy(
            treasures = mutableListOf(treasure)
        )

        //when
        route.addPrefixToFilesPaths(some())

        //then
        assertThat(route.treasures[0].tipFileName).isNull()
        assertThat(route.treasures[0].photoFileName).isNull()
    }
}