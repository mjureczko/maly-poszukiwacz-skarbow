package pl.marianjureczko.poszukiwacz.usecase

import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.shared.Coordinates
import pl.marianjureczko.poszukiwacz.shared.port.StorageHelper

class AddTreasureDescriptionToRouteUC(
    private val storage: StorageHelper
) {
    operator fun invoke(route: Route, coordinates: Coordinates): Route {
        val newTreasure = TreasureDescription(route.nextId(), coordinates.latitude, coordinates.longitude)
        val updatedRoute = route.copy(treasures = (route.treasures + listOf(newTreasure)).toMutableList())
        storage.save(updatedRoute)
        return updatedRoute
    }
}