package pl.marianjureczko.poszukiwacz.activity.searching.n

import android.location.Location
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureDescription

data class SearchingState(
    var route: Route,
    var currentTreasure: TreasureDescription,
    var currentLocation: Location?,
    var stepsToTreasure: Int?
) {
    constructor(route: Route) : this(route, route.treasures[0], null, null)
}
