package pl.marianjureczko.poszukiwacz.usecase

import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.shared.port.storage.StoragePort

class RemoveTreasureDescriptionFromRouteUC(
    private val storage: StoragePort
) {
    operator fun invoke(route: Route, treasureDescriptionId: Int): Route {
        route.getTreasureDescriptionById(treasureDescriptionId)?.let { toRemove: TreasureDescription ->
            val updatedRoute = route.copy(treasures = route.treasures
                .filter { it.id != toRemove.id }
                .toMutableList())
            if(updatedRoute.treasures.isEmpty()) {
                storage.removeProgress(updatedRoute.name)
            } else {
                storage.loadProgress(route.name)?.let { progress ->
                    if (progress.selectedTreasureDescriptionId == toRemove.id && updatedRoute.treasures.isNotEmpty()) {
                        progress.selectedTreasureDescriptionId = updatedRoute.treasures[0].id
                        storage.save(progress)
                    }
                    if (progress.commemorativePhotosByTreasuresDescriptionIds[toRemove.id] != null) {
                        storage.removeFile(progress.commemorativePhotosByTreasuresDescriptionIds[toRemove.id]!!)
                        progress.commemorativePhotosByTreasuresDescriptionIds.remove(toRemove.id)
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