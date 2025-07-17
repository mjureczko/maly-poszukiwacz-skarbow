package pl.marianjureczko.poszukiwacz.usecase

import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.shared.port.StorageHelper

class RemoveTreasureDescriptionFromRouteUC(
    private val storage: StorageHelper
) {
    operator fun invoke(route: Route, treasureDescriptionId: Int): Route {
        route.getTreasureDescriptionById(treasureDescriptionId)?.let { toRemove ->
            val updatedRoute = route.copy(treasures = route.treasures
                .filter { it.id != toRemove.id }
                .toMutableList())
            if(updatedRoute.treasures.isEmpty()) {
                storage.removeProgress(updatedRoute.name)
            } else {
                storage.loadProgress(route.name)?.let { progress ->
                    if (progress.selectedTreasureDescriptionId == toRemove.id) {
                        progress.selectedTreasureDescriptionId = updatedRoute.treasures[0].id
                        storage.save(progress)
                    }
                }
            }
            storage.removeTipFiles(toRemove)
            storage.save(updatedRoute)
            return updatedRoute
        }
        return route
    }
}