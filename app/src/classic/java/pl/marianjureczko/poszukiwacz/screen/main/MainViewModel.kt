package pl.marianjureczko.poszukiwacz.screen.main

import android.content.res.Resources
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.shared.GoToTreasureEditor
import pl.marianjureczko.poszukiwacz.shared.port.StorageHelper
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val storageHelper: StorageHelper,
    private val resources: Resources
) : ViewModel() {

    private var _state = mutableStateOf(MainState())

    val state: State<MainState>
        get() = _state

    init {
        loadAllRoutes()
    }

    fun openNewRouteDialog() {
        _state.value = _state.value.copy(
            newRoute = NewRoute(true, ""),
        )
    }

    fun hideNewRouteDialog() {
        _state.value = _state.value.copy(newRoute = _state.value.newRoute.copy(showDialog = false))
    }

    fun hideOverrideRouteDialog() {
        _state.value = _state.value.copy(showOverrideRouteDialog = false)
    }

    fun openConfirmDeleteDialog(route: Route) {
        val newValue = _state.value.deleteConfirmation.copy(
            showDialog = true,
            confirmationPrompt = resources.getString(R.string.route_remove_prompt, route.name),
            deleteCandidate = route
        )
        _state.value = _state.value.copy(deleteConfirmation = newValue)
    }

    fun hideConfirmDeleteDialog() {
        val newValue = _state.value.deleteConfirmation.copy(showDialog = false)
        _state.value = _state.value.copy(deleteConfirmation = newValue)
    }

    fun createNewRouteByName(routeName: String, goToTreasureEditor: GoToTreasureEditor) {
        _state.value = _state.value.copy(newRoute = _state.value.newRoute.copy(routeName = routeName))
        if (routeExists(routeName)) {
            openOverrideRouteDialog()
        } else {
            saveNewRoute(routeName)
            goToTreasureEditor.invoke(routeName)
        }
    }

    fun replaceRouteWithNewOne(newRouteName: String, goToTreasureEditor: GoToTreasureEditor) {
        storageHelper.removeRouteByName(newRouteName)
        saveNewRoute(newRouteName)
        goToTreasureEditor.invoke(newRouteName)
    }

    fun deleteRoute(route: Route) {
        storageHelper.remove(route)
        loadAllRoutes()
    }

    private fun loadAllRoutes() {
        _state.value = _state.value.copy(routes = storageHelper.loadAll())
    }

    private fun routeExists(routeName: String): Boolean {
        return _state.value.routes
            .map { it.name }
            .find { it.equals(routeName, ignoreCase = true) } != null
    }

    private fun openOverrideRouteDialog() {
        _state.value = _state.value.copy(showOverrideRouteDialog = true)
    }

    private fun saveNewRoute(name: String) {
        val route = Route(name)
        storageHelper.save(route)
        loadAllRoutes()
    }
}