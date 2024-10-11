package pl.marianjureczko.poszukiwacz.shared

import com.ocadotechnology.gembus.test.some
import org.mockito.Mockito.mock
import pl.marianjureczko.poszukiwacz.model.Route

class TestStorage : StorageHelper(mock()) {
    val routes: MutableMap<String, Route> = mutableMapOf()

    init {
        val route = some<Route>()
        routes[route.name] = route
    }

    override fun loadAll(): MutableList<Route> = routes.values.toMutableList()
    override fun save(route: Route) {
        routes[route.name] = route
    }
    override fun removeRouteByName(routeName: String) {
        routes.remove(routeName)
    }
    override fun remove(toRemove: Route) {
        routes.remove(toRemove.name)
    }
}