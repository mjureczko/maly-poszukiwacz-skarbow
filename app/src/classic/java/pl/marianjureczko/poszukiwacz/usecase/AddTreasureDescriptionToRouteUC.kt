package pl.marianjureczko.poszukiwacz.usecase

import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.shared.port.storage.StoragePort

class AddTreasureDescriptionToRouteUC(
    private val storage: StoragePort
) {
    operator fun invoke(route: Route, location: AndroidLocation): Route {
        val newTreasure = TreasureDescription(route.nextId(), location.latitude, location.longitude)
        val updatedRoute = route.copy(treasures = (route.treasures + listOf(newTreasure)).toMutableList())
        storage.save(updatedRoute)
        return updatedRoute
    }
}