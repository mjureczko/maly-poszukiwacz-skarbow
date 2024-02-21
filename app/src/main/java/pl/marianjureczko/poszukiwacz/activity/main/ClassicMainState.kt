package pl.marianjureczko.poszukiwacz.activity.main

import pl.marianjureczko.poszukiwacz.model.Route

data class ClassicMainState(
    var routes: List<Route> = emptyList(),
    var showNewRouteDialog: Boolean = false,
    var showOverrideRouteDialog: Boolean = false,
    var showConfirmDeleteDialog: Boolean = false,
    var newRouteName: String = "",
)