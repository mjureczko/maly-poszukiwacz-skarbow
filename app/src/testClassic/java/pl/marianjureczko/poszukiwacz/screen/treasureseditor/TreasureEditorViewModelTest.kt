package pl.marianjureczko.poszukiwacz.screen.treasureseditor

import android.net.Uri
import com.ocadotechnology.gembus.test.some
import com.ocadotechnology.gembus.test.someString
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.shared.Coordinates
import pl.marianjureczko.poszukiwacz.testhelpers.assertRouteContainsTreasureWith
import pl.marianjureczko.poszukiwacz.testhelpers.assertRouteDoesNotContainTresureWithId

class TreasureEditorViewModelTest {

    @Test
    fun `SHOULD hide sound recording dialog WHEN calling the corresponding method`() {
        //given
        val fixture = TreasureEditorViewModelFixture(some<Route>())

        //when
        fixture.viewModel.hideSoundRecordingDialog()

        //then
        val hideState = fixture.viewModel.state.value
        assertThat(hideState.showSoundRecordingDialog).isFalse()
        assertThat(hideState.fileForTipRecording).isNull()
    }

    @Test
    fun `SHOULD show sound recording dialog WHEN calling the corresponding method`() {
        //given
        val fixture = TreasureEditorViewModelFixture(some<Route>())
        val selectedTreasureDescription = fixture.route.treasures[0]

        //when
        fixture.viewModel.showSoundRecordingDialog(selectedTreasureDescription)

        //then
        val showState = fixture.viewModel.state.value
        assertThat(showState.showSoundRecordingDialog).isTrue()
        assertThat(showState.fileForTipRecording).isEqualTo(selectedTreasureDescription.tipFileName)
    }

    @Test
    fun `SHOULD use existing photo file WHEN treasure description already has photo`() {
        //given
        val fixture = TreasureEditorViewModelFixture(some<Route>())
        val uri = Uri.parse("https://${someString()}")
        given(fixture.photoHelper.createPhotoUri(fixture.route.treasures[0].photoFileName!!)).willReturn(uri)

        //when
        val actual = fixture.viewModel.createUriToPhotoFile(fixture.route.treasures[0])

        //then
        assertThat(actual).isEqualTo(uri)
    }

    @Test
    fun `SHOULD create new photo and update route WHEN treasure description has no photo`() {
        //given
        val route = some<Route>() {
            treasures = listOf(
                some<TreasureDescription> {
                    photoFileName = null
                }
            )
        }
        val fixture = TreasureEditorViewModelFixture(route)
        val uri = Uri.parse("https://${someString()}")
        given(fixture.photoHelper.createPhotoUri(fixture.storage.newPhotoFile)).willReturn(uri)

        //when
        val actual = fixture.viewModel.createUriToPhotoFile(fixture.route.treasures[0])

        //then
        assertThat(actual).isEqualTo(uri)
        val actualRouteFromState = fixture.viewModel.state.value.route
        assertThat(actualRouteFromState.treasures[0].photoFileName).isEqualTo(fixture.storage.newPhotoFile)
        val actualPersistedRoute = fixture.storage.routes[fixture.routeName]!!
        assertThat(actualPersistedRoute.treasures[0].photoFileName).isEqualTo(fixture.storage.newPhotoFile)
    }

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
        val fixture = TreasureEditorViewModelFixture(some<Route> { treasures = listOf() })
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