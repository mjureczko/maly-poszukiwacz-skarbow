package pl.marianjureczko.poszukiwacz.usecase

import com.ocadotechnology.gembus.test.some
import com.ocadotechnology.gembus.test.someString
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import pl.marianjureczko.poszukiwacz.model.HunterPath
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.screen.treasureseditor.TreasureEditorViewModelFixture

class RemoveRouteUCTest {
    @Test
    fun `SHOULD remove progress and hunter path and commemorative photos and tips WHEN removing route`() {
        //given
        val route = some<Route>()
        val commemorativePhoto = someString()
        val fixture = TreasureEditorViewModelFixture(route)
        fixture.setupProgressWithCommemorativePhoto(commemorativePhoto)
        fixture.storage.save(some<HunterPath>().copy(routeName = route.name))

        //when
        fixture.removeRouteUC(route)

        //then
        val removedFiles = fixture.storage.removedFiles
        assertThat(fixture.storage.requestedTipRemovals).containsExactlyInAnyOrderElementsOf(
            route.treasures.map { it.id }
        )
        assertThat(removedFiles).containsExactlyInAnyOrder(commemorativePhoto)
        assertThat(fixture.storage.hunterPaths[route.name]).isNull()
        assertThat(fixture.storage.progresses[route.name]).isNull()
        assertThat(fixture.storage.routes[route.name]).isNull()
    }
}