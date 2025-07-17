package pl.marianjureczko.poszukiwacz.usecase

import com.ocadotechnology.gembus.test.some
import com.ocadotechnology.gembus.test.someInt
import com.ocadotechnology.gembus.test.someObjects
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.screen.treasureseditor.TreasureEditorViewModelFixture
import pl.marianjureczko.poszukiwacz.testhelpers.assertRouteDoesNotContainTresureWithId

class RemoveTreasureDescriptionFromRouteUCTest {
    @Test
    fun `SHOULD remove treasure its tips and change selected treasure in progress WHEN both tips exist and removed treasure is selected`() {
        //given
        val route = some<Route> { treasures = someObjects<TreasureDescription>(2).toList() }
        val fixture = TreasureEditorViewModelFixture(route)
        val toRemove = route.treasures[0]

        fixture.setupProgress(treasureToSelect = toRemove)
        val useCase = fixture.removeTreasureDescriptionFromRouteUC

        //when
        val actualRoute = useCase(route, toRemove.id)
        val actualPersistedRoute = fixture.storage.routes[route.name]!!
        val actualPersistedProgress = fixture.storage.progresses[route.name]!!

        //then
        assertRouteDoesNotContainTresureWithId(actualRoute, toRemove.id)
        assertRouteDoesNotContainTresureWithId(actualPersistedRoute, toRemove.id)
        assertThat(fixture.storage.requestedTipRemovals).containsExactly(toRemove.id)
        assertThat(actualPersistedProgress.selectedTreasureDescriptionId).isNotEqualTo(toRemove.id)
        assertThat(actualPersistedProgress.selectedTreasureDescriptionId)
            .`as`("An existing TreasureDescription should be selected")
            .isEqualTo(actualRoute.treasures[0].id)
    }

    @Test
    fun `SHOULD remove treasure and its progress WHEN removing the last TreasureDescription`() {
        //given
        val route = some<Route> { treasures = someObjects<TreasureDescription>(1).toList() }
        val fixture = TreasureEditorViewModelFixture(route)
        val toRemove = route.treasures[0]

        fixture.setupProgress()
        val useCase = fixture.removeTreasureDescriptionFromRouteUC

        //when
        val actualRoute = useCase(route, toRemove.id)
        val actualPersistedRoute = fixture.storage.routes[route.name]!!
        val actualPersistedProgress = fixture.storage.progresses[route.name]

        //then
        assertRouteDoesNotContainTresureWithId(actualRoute, toRemove.id)
        assertRouteDoesNotContainTresureWithId(actualPersistedRoute, toRemove.id)
        assertThat(actualPersistedProgress)
            .`as`("It is pointless to have a progress when no TreasureDescriptions exists")
            .isNull()
    }

    @Test
    fun `SHOULD do nothing WHEN treasure requested for removal does not exist`() {
        //given
        val route = some<Route>()
        val fixture = TreasureEditorViewModelFixture(route)
        val expectedRoute = route.copy()
        val useCase = fixture.removeTreasureDescriptionFromRouteUC

        //when
        val actualRoute = useCase(route, someInt())
        val actualPersistedRoute = fixture.storage.routes[route.name]

        //then
        assertThat(actualRoute).isEqualTo(expectedRoute)
        assertThat(actualPersistedRoute).isEqualTo(expectedRoute)
    }
}