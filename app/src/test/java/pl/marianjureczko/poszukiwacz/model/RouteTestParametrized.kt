package pl.marianjureczko.poszukiwacz.model

import com.ocadotechnology.gembus.test.some
import com.ocadotechnology.gembus.test.someInt
import org.apache.commons.io.FileUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import pl.marianjureczko.poszukiwacz.shared.TestContext
import pl.marianjureczko.poszukiwacz.shared.port.storage.StoragePort
import java.io.File

class RouteTestParametrized() {
    companion object {
        @JvmStatic
        fun data(): List<Arguments> {
            return listOf(
                Arguments.of(emptyList<Int>(), 1),
                Arguments.of(listOf(2), 3),
                Arguments.of(listOf(2, 5, 6), 7)
            )
        }
    }

    @ParameterizedTest(name = "SHOULD give {0} as nextId WHEN {1} treasures are in the route")
    @MethodSource("data")
    fun nextId(ids: List<Int>, expectedNextId: Int) {
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
    private val storagePort = StoragePort(context)

    @BeforeEach
    fun cleanup() {
        FileUtils.deleteDirectory(File(context.filesDir.absolutePath + StoragePort.routesDirectory))
    }

    @Test
    fun `SHOULD return TreasureDescription WHEN there is a match with requested id`() {
        //given
        val route = some<Route>()
        val existingId = route.treasures.last().id

        //when
        val actual = route.getTreasureDescriptionById(existingId)

        //then
        assertThat(actual).isEqualTo(route.treasures.last())
    }

    @Test
    fun `SHOULD return null WHEN there is no TreasureDescription with requested id`() {
        //given
        val route = some<Route>()

        //when
        val actual = route.getTreasureDescriptionById(someInt())

        //then
        assertThat(actual).isNull()
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