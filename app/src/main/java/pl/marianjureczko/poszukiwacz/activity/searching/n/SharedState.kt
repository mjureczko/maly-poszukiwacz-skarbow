package pl.marianjureczko.poszukiwacz.activity.searching.n

import android.location.Location
import android.media.MediaPlayer
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
interface SelectorSharedState {
    val route: Route
    val currentLocation: Location?
    val distancesInSteps: Map<Int, Int?>
    fun isTreasureCollected(treasureId: Int): Boolean
    fun hasCommemorativePhoto(treasureId: Int): Boolean
}

interface SearchingSharedState {
    val mediaPlayer: MediaPlayer
    val route: Route
    var currentTreasure: TreasureDescription
    var treasuresProgress: TreasuresProgress
    val currentLocation: Location?
    val stepsToTreasure: Int?
    var needleRotation: Float
    fun treasureFoundAndResultAlreadyPresented(): Boolean
}

interface CommemorativeSharedState {
    val route: Route
    var treasuresProgress: TreasuresProgress
}

data class SharedState(
    override val mediaPlayer: MediaPlayer,
    override var route: Route,
    //TODO: should be persisted to not loose the state
    override var currentTreasure: TreasureDescription,
    override var treasuresProgress: TreasuresProgress,
    override var currentLocation: Location?,
    override var stepsToTreasure: Int?,
    override var needleRotation: Float = 0.0f,
    override val distancesInSteps: Map<Int, Int?> = route.treasures
        .associate { it.id to null }
        .toMap()
) : SelectorSharedState, SearchingSharedState, CommemorativeSharedState {
    constructor(mediaPlayer: MediaPlayer, route: Route, treasuresProgress: TreasuresProgress) :
            this(mediaPlayer, route, route.treasures[0], treasuresProgress, null, null)

    override fun isTreasureCollected(treasureId: Int): Boolean =
        treasuresProgress.collectedTreasuresDescriptionId.contains(treasureId)

    override fun hasCommemorativePhoto(treasureId: Int): Boolean {
        return treasuresProgress.commemorativePhotosByTreasuresDescriptionIds.containsKey(treasureId)
    }

    override fun treasureFoundAndResultAlreadyPresented() =
        treasuresProgress.justFoundTreasureId != null
                && (treasuresProgress.resultRequiresPresentation == null || treasuresProgress.resultRequiresPresentation == false)

}
