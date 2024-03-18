package pl.marianjureczko.poszukiwacz.activity.searching.n

import android.location.Location
import android.media.MediaPlayer
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress

data class SearchingState(
    val mediaPlayer: MediaPlayer,
    var route: Route,
    var currentTreasure: TreasureDescription,
    var treasuresProgress: TreasuresProgress,
    var currentLocation: Location?,
    var stepsToTreasure: Int?,
    var needleRotation: Float = 0.0f
) {
    constructor(mediaPlayer: MediaPlayer, route: Route, treasuresProgress: TreasuresProgress) :
            this(mediaPlayer, route, route.treasures[0], treasuresProgress, null, null)
}
