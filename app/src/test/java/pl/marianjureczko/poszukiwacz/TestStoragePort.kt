package pl.marianjureczko.poszukiwacz

import com.ocadotechnology.gembus.test.some
import org.mockito.Mockito.mock
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
import pl.marianjureczko.poszukiwacz.shared.StorageHelper

class TestStoragePort : StorageHelper(mock()) {
    val routes: MutableMap<String, Route> = mutableMapOf()
    val requestedTipRemovals: MutableList<Int> = mutableListOf()
    val progresses: MutableMap<String, TreasuresProgress> = mutableMapOf()

    init {
        val route = some<Route>()
        routes[route.name] = route
    }

    override fun loadAll(): MutableList<Route> = routes.values.toMutableList()
    override fun loadRoute(name: String): Route = routes[name]!!
    override fun save(route: Route) {
        routes[route.name] = route
    }
    override fun removeRouteByName(routeName: String) {
        routes.remove(routeName)
    }
    override fun remove(toRemove: Route) {
        routes.remove(toRemove.name)
    }

    override fun removeTipFiles(treasureDescription: TreasureDescription) {
        requestedTipRemovals.add(treasureDescription.id)
    }

    override fun save(progress: TreasuresProgress) {
        progresses[progress.routeName] = progress
    }
    override fun loadProgress(routeName: String): TreasuresProgress? = progresses[routeName]
    override fun removeProgress(routeName: String) {
        progresses.remove(routeName)
    }
}