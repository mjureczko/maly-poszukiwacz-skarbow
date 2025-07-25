package pl.marianjureczko.poszukiwacz.model

import com.ocadotechnology.gembus.test.someFrom
import pl.marianjureczko.poszukiwacz.shared.port.storage.StoragePort

class RouteAndProgressFixture(
    val route: Route,
    val progress: TreasuresProgress
) {

    companion object {
        fun savedWithSelectedTreasure(storagePort: StoragePort): RouteAndProgressFixture {
            val route = RouteArranger.routeWithoutTipFiles()
            val selected = someFrom(route.treasures)
            val progress = TreasuresProgress(route.name, route.treasures[0].id)
            progress.selectedTreasureDescriptionId = selected.id
            saveBoth(storagePort, route, progress)
            return RouteAndProgressFixture(route, progress)
        }

        fun savedWithoutSelectedTreasure(storagePort: StoragePort): RouteAndProgressFixture {
            val route = RouteArranger.routeWithoutTipFiles()
            val progress = TreasuresProgress(route.name, route.treasures[0].id)
            saveBoth(storagePort, route, progress)
            return RouteAndProgressFixture(route, progress)
        }

        private fun saveBoth(storagePort: StoragePort, route: Route, progress: TreasuresProgress) {
            storagePort.save(route)
            storagePort.save(progress)
        }
    }
}