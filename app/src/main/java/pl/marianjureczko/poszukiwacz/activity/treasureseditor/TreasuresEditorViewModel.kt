package pl.marianjureczko.poszukiwacz.activity.treasureseditor

import androidx.lifecycle.ViewModel
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.Treasure
import pl.marianjureczko.poszukiwacz.model.TreasureDescription

class TreasuresEditorViewModel : ViewModel() {
    private val TAG = javaClass.simpleName
    var route: Route = Route.nullObject()
    var treasureNeedingPhoto: TreasureDescription? = null
}