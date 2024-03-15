package pl.marianjureczko.poszukiwacz.activity.searching.n

import android.location.Location
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress

data class SearchingState(
    var route: Route,
    var currentTreasure: TreasureDescription,
    var treasuresProgress: TreasuresProgress,
    var currentLocation: Location?,
    var stepsToTreasure: Int?,
    var needleRotation: Float = 0.0f
) {
    constructor(route: Route, treasuresProgress: TreasuresProgress) :
            this(route, route.treasures[0], treasuresProgress, null, null)
}
