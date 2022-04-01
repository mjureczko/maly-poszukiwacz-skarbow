package pl.marianjureczko.poszukiwacz.activity.treasureselector

import androidx.lifecycle.ViewModel
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureBag

class SelectorViewModel() : ViewModel() {
    lateinit var route: Route
    lateinit var progress: TreasureBag
    var userLocation: Coordinates? = null
    var selectedTreasure: Int = TreasureSelectorActivity.NON_SELECTED
}