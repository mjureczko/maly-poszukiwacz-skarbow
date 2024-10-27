package pl.marianjureczko.poszukiwacz.screen.treasureseditor

import com.ocadotechnology.gembus.test.some
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.shared.Coordinates
import pl.marianjureczko.poszukiwacz.testhelpers.assertRouteContainsTreasureWith
import pl.marianjureczko.poszukiwacz.testhelpers.assertRouteDoesNotContainTresureWithId

class TreasureEditorViewModelTest{

    @Test
    fun shouldUpdateLocationInState_whenLocationPortProvidesNewOne() {
        // given
        val fixture = TreasureEditorViewModelFixture(some<Route>())
        val newLocation = some<Coordinates>()

        //when
        fixture.locationPort.updateLocation(newLocation.latitude, newLocation.longitude)

        //then
        val actual = fixture.viewModel.state.value.currentLocation!!
        assertThat(actual.latitude).isEqualTo(newLocation.latitude)
        assertThat(actual.longitude).isEqualTo(newLocation.longitude)
    }

    @Test
    fun shouldAddTreasureWithCurrentCoordinatesToRouteFromState_whenRequested() {
        //given
        val fixture = TreasureEditorViewModelFixture(some<Route> { treasures = mutableListOf() })
        val newCoordinates = some<Coordinates>()
        fixture.locationPort.updateLocation(newCoordinates.latitude, newCoordinates.longitude)
        val stateValue = fixture.viewModel.state.value

        //when
        fixture.viewModel.addTreasure()

        //then
        assertThat(fixture.viewModel.state.value)
            .`as`("state value instance should be replaced to trigger recomposition")
            .isNotSameAs(stateValue)
        val actualRoute = fixture.viewModel.state.value.route
        //first TreasureDescription will have id=1
        assertRouteContainsTreasureWith(actualRoute, 1, newCoordinates)
        val actualPersistedRoute = fixture.storage.routes[fixture.routeName]!!
        assertRouteContainsTreasureWith(actualPersistedRoute, 1, newCoordinates)
    }

    @Test
    fun `SHOULD remove TreasureDescription`() {
        //given
        val fixture = TreasureEditorViewModelFixture(some<Route>())
        val toRemove = fixture.route.treasures.last()
        val stateValue = fixture.viewModel.state.value

        //when
        fixture.viewModel.removeTreasure(toRemove.id)

        //then
        assertThat(fixture.viewModel.state.value)
            .`as`("state value instance should be replaced to trigger recomposition")
            .isNotSameAs(stateValue)
        val actualRoute = fixture.viewModel.state.value.route
        assertRouteDoesNotContainTresureWithId(actualRoute, toRemove.id)
        val actualPersistedRoute = fixture.storage.routes[fixture.routeName]!!
        assertRouteDoesNotContainTresureWithId(actualPersistedRoute, toRemove.id)
    }
}