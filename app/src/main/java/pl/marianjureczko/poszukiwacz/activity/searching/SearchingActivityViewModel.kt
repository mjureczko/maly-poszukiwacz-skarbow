package pl.marianjureczko.poszukiwacz.activity.searching

import androidx.lifecycle.ViewModel
import pl.marianjureczko.poszukiwacz.Route
import pl.marianjureczko.poszukiwacz.TreasureDescription
import pl.marianjureczko.poszukiwacz.XmlHelper

class SearchingActivityViewModel : ViewModel(), TreasureSupplier {
    private val TAG = javaClass.simpleName
    private val xmlHelper = XmlHelper()
    lateinit var route: Route
    var routeXml: String? = null
    var selectedTreasure: TreasureDescription? = null
    var treasureIndex: Int? = null

    override fun getTreasure(): TreasureDescription? {
        return selectedTreasure
    }

    fun setup(routeXml: String) {
        this.routeXml = routeXml
        route = xmlHelper.loadFromString(routeXml)
    }

    fun selectTreasure(which: Int) {
        treasureIndex = which
        selectedTreasure = route.treasures[which]
    }
}