package pl.marianjureczko.poszukiwacz.screen.main

import pl.marianjureczko.poszukiwacz.model.Route

data class MainState(
    var routes: List<Route> = emptyList(),
    val newRoute: NewRoute = NewRoute(),
    var showOverrideRouteDialog: Boolean = false,
    val deleteConfirmation: DeleteConfirmation = DeleteConfirmation()
)

data class DeleteConfirmation(
    val showDialog: Boolean = false,
    val confirmationPrompt: String = ""
)

data class NewRoute(
    var showDialog: Boolean = false,
    var routeName: String = ""
)