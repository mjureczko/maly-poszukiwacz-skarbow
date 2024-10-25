package pl.marianjureczko.poszukiwacz.screen.treasureseditor

import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.shared.Coordinates
import pl.marianjureczko.poszukiwacz.shared.StorageHelper
import javax.inject.Inject

class AddTreasureUseCase @Inject constructor(
    private val storage: StorageHelper
) {
    operator fun invoke(route: Route, coordinates: Coordinates): Route {

        return route
    }
}