package pl.marianjureczko.poszukiwacz.usecase

import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.shared.port.storage.StoragePort

class RemoveRouteUC(
    private val storagePort: StoragePort,
    private val removeTreasureDescriptionFromRouteUC: RemoveTreasureDescriptionFromRouteUC
) {
    operator fun invoke(route: Route) {
        route.treasures.forEach { toRemove -> removeTreasureDescriptionFromRouteUC(route, toRemove.id) }
        storagePort.removeProgress(route.name)
        storagePort.removeHunterPath(route.name)
        storagePort.remove(route)
    }
}