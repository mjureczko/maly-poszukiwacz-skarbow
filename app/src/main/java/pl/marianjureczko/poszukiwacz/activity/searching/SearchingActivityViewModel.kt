package pl.marianjureczko.poszukiwacz.activity.searching

import android.location.Location
import androidx.lifecycle.ViewModel
import pl.marianjureczko.poszukiwacz.activity.treasureselector.SelectTreasureInputData
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureBag
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.shared.XmlHelper

class SearchingActivityViewModel : ViewModel(), DataStorageWrapper, TreasuresStorage, TipNameProvider {
    private val TAG = javaClass.simpleName
    private val xmlHelper = XmlHelper()
    lateinit var route: Route
    var routeXml: String? = null
    var selectedTreasure: TreasureDescription? = null
    var treasureIndex: Int? = null
    var treasureSelectionInitialized = false
    lateinit var treasureBag: TreasureBag
    private var currentLocation: Location? = null

    override fun getTreasure(): TreasureDescription? {
        return selectedTreasure
    }

    override fun getTreasureSelectorActivityInputData(): SelectTreasureInputData =
        SelectTreasureInputData(route, treasureBag, selectedTreasure, currentLocation)

    override fun setCurrentLocation(location: Location?) {
        currentLocation = location
    }

    fun setup(routeXml: String) {
        this.routeXml = routeXml
        route = xmlHelper.loadFromString<Route>(routeXml)
//        treasureBag = TreasureBag(route.name)
    }

    fun selectTreasure(treasureId: Int) {
        run loop@{
            route.treasures.forEachIndexed { index, treasure ->
                if (treasure.id == treasureId) {
                    treasureIndex = index
                    selectedTreasure = treasure
                    return@loop
                }
            }
        }
    }

    override fun tipName(): String? =
        selectedTreasure?.tipFileName
}