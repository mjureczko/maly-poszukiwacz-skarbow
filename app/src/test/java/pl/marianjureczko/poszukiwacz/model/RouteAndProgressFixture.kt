package pl.marianjureczko.poszukiwacz.model

import com.ocadotechnology.gembus.test.someFrom
import pl.marianjureczko.poszukiwacz.shared.StorageHelper

class RouteAndProgressFixture(
    val route: Route,
    val progress: TreasureBag
) {

    companion object {
        fun savedWithSelectedTreasure(storageHelper: StorageHelper): RouteAndProgressFixture {
            val route = RouteArranger.routeWithoutTipFiles()
            val selected = someFrom(route.treasures)
            val progress = TreasureBag(route.name)
            progress.selectedTreasure = selected
            saveBoth(storageHelper, route, progress)
            return RouteAndProgressFixture(route, progress)
        }

        fun savedWithoutSelectedTreasure(storageHelper: StorageHelper): RouteAndProgressFixture {
            val route = RouteArranger.routeWithoutTipFiles()
            val progress = TreasureBag(route.name)
            saveBoth(storageHelper, route, progress)
            return RouteAndProgressFixture(route, progress)
        }

        private fun saveBoth(storageHelper: StorageHelper, route: Route, progress: TreasureBag) {
            storageHelper.save(route)
            storageHelper.save(progress)
        }
    }

    fun selectedTreasure(): TreasureDescription? =
        route.treasures
            .find { t -> t.id == progress.selectedTreasure?.id }

}