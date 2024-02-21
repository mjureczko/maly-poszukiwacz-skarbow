package pl.marianjureczko.poszukiwacz.activity.main

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import pl.marianjureczko.poszukiwacz.App
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.shared.StorageHelper
import androidx.compose.runtime.State

class ClassicMainViewModel : ViewModel() {
    private val storageHelper = StorageHelper(App.getAppContext())

    private var _state = mutableStateOf(ClassicMainState())

    val state: State<ClassicMainState>
        get() = _state

    init {
        loadAllRoutes()
    }

    fun routeExists(routeName: String): Boolean {
        return _state.value.routes
            .map { it.name }
            .find { it.equals(routeName, ignoreCase = true) } != null
    }

    fun openNewRouteDialog() {
        _state.value = _state.value.copy(
            showNewRouteDialog = true,
            newRouteName = ""
        )
    }

    fun hideNewRouteDialog() {
        _state.value = _state.value.copy(showNewRouteDialog = false)
    }

    fun openOverrideRouteDialog() {
        _state.value = _state.value.copy(showOverrideRouteDialog = true)
    }

    fun hideOverrideRouteDialog() {
        _state.value = _state.value.copy(showOverrideRouteDialog = false)
    }

    fun openConfirmDeleteDialog() {
        _state.value = _state.value.copy(showConfirmDeleteDialog = true)
    }

    fun hideConfirmDeleteDialog() {
        _state.value = _state.value.copy(showConfirmDeleteDialog = false)
    }

    fun createNewRouteByName(routeName: String) {
        _state.value = _state.value.copy(newRouteName = routeName)
        if (routeExists(routeName)) {
            openOverrideRouteDialog()
        } else {
            saveNewRoute(routeName)
            //TODO: go to edit route screen
        }
    }

    fun saveNewRoute(name: String) {
        val route = Route(name)
        storageHelper.save(route)
        loadAllRoutes()
    }

    fun replaceRouteWithNewOne(newRouteName: String) {
        storageHelper.removeRouteByName(newRouteName)
        saveNewRoute(newRouteName)
    }

    fun deleteRoute(route: Route) {
        storageHelper.remove(route)
        loadAllRoutes()
    }

    private fun loadAllRoutes() {
        _state.value = _state.value.copy(routes = storageHelper.loadAll())
    }
}