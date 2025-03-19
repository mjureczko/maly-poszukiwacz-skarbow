package pl.marianjureczko.poszukiwacz

import android.content.Context
import com.ocadotechnology.gembus.test.some
import com.ocadotechnology.gembus.test.someString
import pl.marianjureczko.poszukiwacz.model.HunterPath
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.RouteArranger
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
import pl.marianjureczko.poszukiwacz.shared.port.StorageHelper
import java.io.FileNotFoundException

class TestStoragePort(context: Context) : StorageHelper(context) {
    val routes: MutableMap<String, Route> = mutableMapOf()
    val requestedTipRemovals: MutableList<Int> = mutableListOf()
    val progresses: MutableMap<String, TreasuresProgress> = mutableMapOf()
    var newPhotoFile: String = someString()
    var fileNotEmpty = false
    var hunterPath: HunterPath = some<HunterPath>()

    init {
        initRoute(someString())
    }

    fun initRoute(routeName: String) {
        val route = RouteArranger.routeWithoutTipFiles().copy(name = routeName)
        routes[route.name] = route
    }

    fun clear() {
        routes.clear()
        requestedTipRemovals.clear()
        progresses.clear()
    }

    override fun loadAll(): MutableList<Route> = routes.values.toMutableList()
    override fun loadRoute(name: String): Route {
        val result = routes[name] ?: throw FileNotFoundException()
        return result
    }

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

    override fun newPhotoFile(): String = newPhotoFile

    override fun fileNotEmpty(file: String?) = fileNotEmpty

    override fun loadHunterPath(routeName: String): HunterPath? {
        return hunterPath
    }

    override fun save(hunterPath: HunterPath) {
        this.hunterPath = hunterPath
    }
}