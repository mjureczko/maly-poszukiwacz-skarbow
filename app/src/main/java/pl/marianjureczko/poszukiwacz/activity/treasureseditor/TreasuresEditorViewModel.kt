package pl.marianjureczko.poszukiwacz.activity.treasureseditor

import androidx.lifecycle.ViewModel
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureDescription

class TreasuresEditorViewModel : ViewModel() {
    var route: Route = Route.nullObject()
    var treasureNeedingPhoto: TreasureDescription? = null

    fun setupTreasureNeedingPhotoById(id: Int) {
        treasureNeedingPhoto = route.treasures.first { it.id == id }
    }
}