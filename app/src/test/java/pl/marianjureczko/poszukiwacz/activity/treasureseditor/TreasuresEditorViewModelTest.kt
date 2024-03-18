package pl.marianjureczko.poszukiwacz.activity.treasureseditor

import androidx.lifecycle.SavedStateHandle
import com.ocadotechnology.gembus.test.some
import org.assertj.core.api.Assertions.assertThat

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.shared.StorageHelper
import java.io.File
import java.io.FileNotFoundException

@ExtendWith(MockitoExtension::class)
internal class TreasuresEditorViewModelTest {

    @Mock
    lateinit var state: SavedStateHandle

    @Mock
    lateinit var storageHelper: StorageHelper

    @Test
    fun `SHOULD initialize with route loaded from file WHEN route name exists in the state`() {
        //given
        val model = TreasuresEditorViewModel(state)
        val routeName = some<String>()
        given(state.get<String>(TreasuresEditorViewModel.ROUTE_NAME))
            .willReturn(routeName)
        var route = some<Route>().copy(name = routeName)
        given(storageHelper.loadRoute(routeName))
            .willReturn(route)

        //when
        model.initializeFromState(storageHelper)

        //then
        val actual = model.getRoute()
        assertThat(actual).isEqualTo(route)
    }

    @Test
    fun `SHOULD initialize with empty route with given name WHEN route name exists in the state but there is no corresponding file`() {
        //given
        val model = TreasuresEditorViewModel(state)
        val routeName = some<String>()
        given(state.get<String>(TreasuresEditorViewModel.ROUTE_NAME))
            .willReturn(routeName)
        given(storageHelper.loadRoute(routeName))
            .willThrow(FileNotFoundException())

        //when
        model.initializeFromState(storageHelper)

        //then
        val actual = model.getRoute()
        assertThat(actual).isEqualTo(Route(routeName))
    }

    @Test
    fun `SHOULD not alter the null route initialization WHEN there is no route name in the state`() {
        //given
        val model = TreasuresEditorViewModel(state)
        given(state.get<String>(TreasuresEditorViewModel.ROUTE_NAME))
            .willReturn(null)

        //when
        model.initializeFromState(storageHelper)

        //then
        val actual = model.getRoute()
        assertThat(actual).isEqualTo(Route.nullObject())
    }

    @Test
    internal fun `SHOULD load route by name from state WHEN initializing`() {
        //given
        val model = TreasuresEditorViewModel(state)
        val routeName = some<String>()
        val route = some<Route>().copy(name = routeName)
        given(storageHelper.loadRoute(routeName))
            .willReturn(route)

        //when
        model.initialize(routeName, storageHelper)

        //then
        assertThat(model.getRoute()).isEqualTo(route)
        then(state).should().set(TreasuresEditorViewModel.ROUTE_NAME, routeName)
    }

    @Test
    internal fun `SHOULD say route name not initialized WHEN route equals null object`() {
        //given
        val model = TreasuresEditorViewModel(state)

        //when
        val actual = model.routeNameWasInitialized()

        //then
        assertThat(actual).isFalse()
    }

    @Test
    internal fun `SHOULD say route name is initialized WHEN route was set`() {
        //given
        val model = TreasuresEditorViewModel(state)
        val route = some<Route>()
        model.setRoute(route)

        //when
        val actual = model.routeNameWasInitialized()

        //then
        assertThat(actual).isTrue()
    }

    @Test
    internal fun `SHOULD instantiate photo file WHEN treasure needing photo was set up`() {
        //given
        val model = TreasuresEditorViewModel(state)
        val route = some<Route>()
        val treasure = Mockito.spy(TreasureDescription::class.java)
        val treasureId = route.nextId()
        treasure.id = treasureId
        route.treasures.add(treasure)
        model.setRoute(route)
        doReturn(File(".")).`when`(treasure).instantiatePhotoFile(pl.marianjureczko.poszukiwacz.any(StorageHelper::class.java))

        //when
        model.setupTreasureNeedingPhotoById(treasureId)
        model.photoFileForTreasureNeedingPhoto(storageHelper)

        //then
        then(treasure.instantiatePhotoFile(storageHelper))
    }
}
