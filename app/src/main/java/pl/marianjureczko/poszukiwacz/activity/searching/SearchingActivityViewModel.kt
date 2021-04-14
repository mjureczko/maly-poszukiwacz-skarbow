package pl.marianjureczko.poszukiwacz.activity.searching

import android.location.Location
import androidx.lifecycle.ViewModel
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.shared.XmlHelper

class SearchingActivityViewModel : ViewModel(), DataStorageWrapper, TreasureLocationStorage, TipNameProvider {
    private val TAG = javaClass.simpleName
    private val xmlHelper = XmlHelper()
    lateinit var route: Route
    var routeXml: String? = null
    var selectedTreasure: TreasureDescription? = null
    var treasureIndex: Int? = null
    var treasureSelectionInitialized = false
    private var currentLocation: Location? = null

    override fun getTreasure(): TreasureDescription? {
        return selectedTreasure
    }

    override fun getCurrentLocation(): Location? =
        currentLocation

    override fun setCurrentLocation(location: Location?) {
        currentLocation = location
    }

    fun setup(routeXml: String) {
        this.routeXml = routeXml
        route = xmlHelper.loadFromString(routeXml)
    }

    override fun selectTreasure(which: Int) {
        treasureIndex = which
        selectedTreasure = route.treasures[which]
    }

    override fun tipName(): String? =
        selectedTreasure?.tipFileName
}