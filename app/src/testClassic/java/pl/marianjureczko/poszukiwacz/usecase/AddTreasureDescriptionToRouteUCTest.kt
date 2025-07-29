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
import pl.marianjureczko.poszukiwacz.testhelpers.assertRouteContainsTreasureWith

class AddTreasureDescriptionToRouteUCTest {

    private val storage = TestStoragePort(mock<Context>())
    private val useCase = AddTreasureDescriptionToRouteUC(storage)
    private val location = some<AndroidLocation>()

    @Test
    fun shouldAddTreasureDescription_whenAddingFiestTreasureToRoute() {
        //given
        val route = Route(someString())

        //when
        val actualRoute = useCase(route, location)
        val persistedRoute = storage.routes[route.name]

        //then
        assertThat(actualRoute.treasures).hasSize(1)
        assertRouteContainsTreasureWith(actualRoute, 1, location)

        assertThat(persistedRoute).isNotNull
        assertThat(persistedRoute!!.treasures).hasSize(1)
        assertRouteContainsTreasureWith(persistedRoute, 1, location)
    }

    @Test
    fun shouldAddTreasureDescription_whenAddingSecondTreasureToRoute() {
        //given
        val treasure = some<TreasureDescription>()
        val route = pl.marianjureczko.poszukiwacz.model.RouteArranger.saveWithTreasureDescription(treasure, storage)

        //when
        val actualRoute = useCase(route, location)
        val persistedRoute = storage.routes[route.name]

        //then
        val expectedId = treasure.id + 1
        assertThat(actualRoute.treasures).hasSize(2)
        assertRouteContainsTreasureWith(actualRoute, expectedId, location)

        assertThat(persistedRoute).isNotNull
        assertThat(persistedRoute!!.treasures).hasSize(2)
        assertRouteContainsTreasureWith(persistedRoute, expectedId, location)
    }
}

