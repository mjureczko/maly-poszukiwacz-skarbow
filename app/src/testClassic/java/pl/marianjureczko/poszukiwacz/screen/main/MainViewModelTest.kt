package pl.marianjureczko.poszukiwacz.screen.main

import android.content.Context
import android.content.res.Resources
import com.ocadotechnology.gembus.test.some
import com.ocadotechnology.gembus.test.someString
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.TestStoragePort
import pl.marianjureczko.poszukiwacz.eq
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.shared.GoToTreasureEditor

class MainViewModelTest {

    val storage: TestStoragePort = TestStoragePort(mock<Context>())
    val resources: Resources = mock()

    @Test
    fun shouldLoadAllRoutes_whenStarting() {
        //when
        val viewModel = MainViewModel(storage, resources)

        //then
        assertThat(viewModel.state.value.routes).isEqualTo(storage.loadAll())
    }

    @Test
    fun shouldMarkShowDialogAndClearRouteName_whenOpeningNewRouteName() {
        //given
        val viewModel = MainViewModel(storage, resources)

        //when
        viewModel.openNewRouteDialog()

        //then
        assertThat(viewModel.state.value.newRoute).isEqualTo(NewRoute(true, ""))
    }

    @Test
    fun shouldMarkHideDialog_whenHidingNewRouteName() {
        //given
        val viewModel = MainViewModel(storage, resources)

        //when
        viewModel.hideNewRouteDialog()

        //then
        assertThat(viewModel.state.value.newRoute.showDialog).isEqualTo(false)
    }

    @Test
    fun shouldMarkHideDialog_whenHidingOverridingRoute() {
        //given
        val viewModel = MainViewModel(storage, resources)

        //when
        viewModel.hideOverrideRouteDialog()

        //then
        assertFalse(viewModel.state.value.showOverrideRouteDialog)
    }

    @Test
    fun shouldSetShowDialogAndPromptTextInState_whenRequestingOpeningDeleteConfirmationDialog() {
        //given
        val viewModel = MainViewModel(storage, resources)
        val route = some<Route>()
        val expectedPrompt = someString()
        given(resources.getString(eq(R.string.route_remove_prompt), eq(route.name)))
            .willReturn(expectedPrompt)

        //when
        viewModel.openConfirmDeleteDialog(route)

        //then
        val actual = viewModel.state.value.deleteConfirmation
        assertThat(actual.showDialog).isTrue()
        assertThat(actual.confirmationPrompt).isEqualTo(expectedPrompt)
    }

    @Test
    fun shouldMarkHideDialog_whenHidingDeleteConfirmation() {
        //given
        val viewModel = MainViewModel(storage, resources)

        //when
        viewModel.hideConfirmDeleteDialog()

        //then
        assertFalse(viewModel.state.value.deleteConfirmation.showDialog)
    }

    @Test
    fun shouldOpenOverrideDialog_whenCreatingRouteWithAlreadyExistingName() {
        //given
        val route = storage.loadAll()[0]
        val viewModel = MainViewModel(storage, resources)

        //when
        viewModel.createNewRouteByName(route.name, {})

        //then
        val actualState = viewModel.state.value
        assertThat(actualState.newRoute.routeName).isEqualTo(route.name)
        assertTrue(actualState.showOverrideRouteDialog)
        assertThat(storage.routes.values).containsExactly(route)
    }

    @Test
    fun shoulSaveNewRoute_whenRouteNameIsUnique() {
        //given
        val route = storage.loadAll()[0]
        val viewModel = MainViewModel(storage, resources)
        val newRouteName = someString()
        var calledName = ""
        val goToTreasureEditor: GoToTreasureEditor = { calledName = it }

        //when
        viewModel.createNewRouteByName(newRouteName, goToTreasureEditor)

        //then
        val actualState = viewModel.state.value
        assertThat(actualState.newRoute.routeName).isEqualTo(newRouteName)
        assertFalse(actualState.showOverrideRouteDialog)
        assertThat(storage.routes.values).contains(route)
        assertThat(storage.routes[newRouteName]!!.name).isEqualTo(newRouteName)
        assertThat(storage.routes[newRouteName]!!.treasures).isEmpty()
        assertThat(calledName).isEqualTo(newRouteName)
    }

    @Test
    fun shouldReplaceRouteWithNewOne() {
        //given
        val route = storage.loadAll()[0]
        val viewModel = MainViewModel(storage, resources)
        val routeName = route.name
        var calledName = ""
        val goToTreasureEditor: GoToTreasureEditor = { calledName = it }

        //when
        viewModel.replaceRouteWithNewOne(routeName, goToTreasureEditor)

        //then
        assertThat(storage.routes[routeName]!!.name).isEqualTo(routeName)
        assertThat(storage.routes[routeName]!!.treasures).isEmpty()
        assertThat(calledName).isEqualTo(routeName)
    }

    @Test
    fun shouldDeleteRoute() {
        //given
        val route = storage.loadAll()[0]
        val viewModel = MainViewModel(storage, resources)

        //when
        viewModel.deleteRoute(route)

        //then
        assertThat(storage.routes).isEmpty()
        assertThat(viewModel.state.value.routes).isEmpty()
    }
}