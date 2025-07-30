package pl.marianjureczko.poszukiwacz.screen.searching

import android.media.MediaPlayer
import pl.marianjureczko.poszukiwacz.model.HunterPath
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
import pl.marianjureczko.poszukiwacz.usecase.LocationHolder

interface HasCommemorativePhoto {
    fun hasCommemorativePhoto(treasureId: Int): Boolean
}

interface SelectorSharedState : HasCommemorativePhoto {
    val route: Route
    var treasuresProgress: TreasuresProgress
    val currentLocation: LocationHolder
    val distancesInSteps: Map<Int, Int?>
    fun isTreasureCollected(treasureId: Int): Boolean
    fun allTreasuresCollected(): Boolean
}

interface SearchingSharedState : HasCommemorativePhoto {
    val mediaPlayer: MediaPlayer
    val route: Route
    var treasuresProgress: TreasuresProgress
    val currentLocation: LocationHolder
    val stepsToTreasure: Int?
    var needleRotation: Float
    var hunterPath: HunterPath
    var gpsAccuracy: GpsAccuracy
    fun treasureFoundAndResultAlreadyPresented(): Boolean
    fun selectedTreasureDescription(): TreasureDescription?
}

interface CommemorativeSharedState {
    val route: Route
    var treasuresProgress: TreasuresProgress
}

data class SharedState(
    override val mediaPlayer: MediaPlayer,
    override var route: Route,
    override var treasuresProgress: TreasuresProgress,
    override var currentLocation: LocationHolder,
    override var stepsToTreasure: Int?,
    override var hunterPath: HunterPath,
    override var needleRotation: Float = 0.0f,
    override var gpsAccuracy: GpsAccuracy = GpsAccuracy.Fine,
    override val distancesInSteps: Map<Int, Int?> = route.treasures
        .associate { it.id to null }
        .toMap(),
) : SelectorSharedState, SearchingSharedState, CommemorativeSharedState {
    constructor(mediaPlayer: MediaPlayer, route: Route, treasuresProgress: TreasuresProgress, hunterPath: HunterPath) :
            this(mediaPlayer, route, treasuresProgress, LocationHolder(), null, hunterPath)

    override fun isTreasureCollected(treasureId: Int): Boolean =
        treasuresProgress.collectedTreasuresDescriptionId.contains(treasureId)

    override fun allTreasuresCollected(): Boolean {
        return route.treasures.all { isTreasureCollected(it.id) }
    }

    override fun hasCommemorativePhoto(treasureId: Int): Boolean {
        return treasuresProgress.commemorativePhotosByTreasuresDescriptionIds.containsKey(treasureId)
    }

    override fun treasureFoundAndResultAlreadyPresented() =
        treasuresProgress.justFoundTreasureId != null
                && (treasuresProgress.resultRequiresPresentation == null || treasuresProgress.resultRequiresPresentation == false)

    override fun selectedTreasureDescription(): TreasureDescription? {
        return route.treasures
            .firstOrNull { it.id == treasuresProgress.selectedTreasureDescriptionId }
    }
}
