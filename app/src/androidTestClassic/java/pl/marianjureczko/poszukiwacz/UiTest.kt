package pl.marianjureczko.poszukiwacz

import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.screen.main.CustomInitializerForRoute
import pl.marianjureczko.poszukiwacz.screen.main.EDIT_ROUTE_BUTTON
import pl.marianjureczko.poszukiwacz.screen.main.ROUTE

open class UiTest : AbstractUITest() {

    fun goToTreasuresEditorScreen(routeName: String) {
        composeRule.waitForIdle()
        performTap(EDIT_ROUTE_BUTTON + routeName)
    }

    fun goToSearchingScreen(route: Route) {
        performTap("$ROUTE ${route.name}")
    }

    override fun getRouteFromStorage(): Route {
        return storage!!.routes.values.first()
    }

}