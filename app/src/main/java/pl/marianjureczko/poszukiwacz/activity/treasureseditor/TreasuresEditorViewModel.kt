package pl.marianjureczko.poszukiwacz.activity.treasureseditor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.shared.StorageHelper
import java.io.File
import java.io.FileNotFoundException

class TreasuresEditorViewModel(private val state: SavedStateHandle) : ViewModel() {
    companion object {
        const val TREASURE_NEEDING_PHOTO = "treasureNeedingPhoto"
        const val ROUTE_NAME = "routeName"
    }

    private var route: Route = Route.nullObject()
    private var treasureNeedingPhotoId: Int? = state.get<Int>(TREASURE_NEEDING_PHOTO)

    fun initializeFromState(storageHelper: StorageHelper) {
        state.get<String>(ROUTE_NAME)?.let {
            try {
                route = storageHelper.loadRoute(it)
            } catch (ex: FileNotFoundException) {
                //route hasn't been saved when no treasures had been added yet, and then the exception is thrown
                this.route = Route(it)
            }
        }
    }

    /** Route with the route name must exist. To be used when started from an intent. */
    fun initialize(routeName: String, storageHelper: StorageHelper) =
        setRoute(storageHelper.loadRoute(routeName))

    fun getRoute(): Route = route

    fun setRoute(route: Route) {
        this.route = route
        state[ROUTE_NAME] = route.name
    }

    fun setupTreasureNeedingPhotoById(id: Int) {
        treasureNeedingPhotoId = route.treasures.first { it.id == id }.id
        state[TREASURE_NEEDING_PHOTO] = treasureNeedingPhotoId
    }

    fun photoFileForTreasureNeedingPhoto(storageHelper: StorageHelper): File? =
        route.treasures.find { it.id == treasureNeedingPhotoId }
            ?.instantiatePhotoFile(storageHelper)

    fun nextTreasureId() =
        route.nextId()

    fun addTreasure(treasureDescription: TreasureDescription, storageHelper: StorageHelper) {
        route.treasures.add(treasureDescription)
        storageHelper.save(route)
    }

    fun routeNameWasInitialized(): Boolean =
        route != Route.nullObject()
}