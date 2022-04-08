package pl.marianjureczko.poszukiwacz.activity.treasureselector

import androidx.lifecycle.ViewModel
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureBag

class SelectorViewModel : ViewModel() {
    lateinit var route: Route
    lateinit var progress: TreasureBag
    var userLocation: Coordinates? = null

    fun selectTreasureById(treasureId: Int) {
        route.treasures.asSequence()
            .find { it.id == treasureId }
            ?.let { progress.selectedTreasure = it }
    }
}