package pl.marianjureczko.poszukiwacz.usecase

import android.content.Context
import com.ocadotechnology.gembus.test.some
import com.ocadotechnology.gembus.test.someString
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import pl.marianjureczko.poszukiwacz.TestStoragePort
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.shared.Coordinates
import pl.marianjureczko.poszukiwacz.testhelpers.assertRouteContainsTreasureWith

class AddTreasureDescriptionToRouteTest {

    private val storage = TestStoragePort(mock<Context>())
    private val useCase = AddTreasureDescriptionToRoute(storage)
    private val coordinates = some<Coordinates>()

    @Test
    fun shouldAddTreasureDescription_whenAddingFiestTreasureToRoute() {
        //given
        val route = Route(someString())

        //when
        val actualRoute = useCase(route, coordinates)
        val persistedRoute = storage.routes[route.name]

        //then
        assertThat(actualRoute.treasures).hasSize(1)
        assertRouteContainsTreasureWith(actualRoute, 1, coordinates)

        assertThat(persistedRoute).isNotNull
        assertThat(persistedRoute!!.treasures).hasSize(1)
        assertRouteContainsTreasureWith(persistedRoute, 1, coordinates)
    }

    @Test
    fun shouldAddTreasureDescription_whenAddingSecondTreasureToRoute() {
        //given
        val treasure = some<TreasureDescription>()
        val route = pl.marianjureczko.poszukiwacz.model.RouteArranger.saveWithTreasureDescription(treasure, storage)

        //when
        val actualRoute = useCase(route, coordinates)
        val persistedRoute = storage.routes[route.name]

        //then
        val expectedId = treasure.id + 1
        assertThat(actualRoute.treasures).hasSize(2)
        assertRouteContainsTreasureWith(actualRoute, expectedId, coordinates)

        assertThat(persistedRoute).isNotNull
        assertThat(persistedRoute!!.treasures).hasSize(2)
        assertRouteContainsTreasureWith(persistedRoute, expectedId, coordinates)
    }
}

