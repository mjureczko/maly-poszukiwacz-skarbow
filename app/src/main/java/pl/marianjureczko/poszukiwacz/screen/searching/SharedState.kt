package pl.marianjureczko.poszukiwacz.screen.searching

import android.media.MediaPlayer
import pl.marianjureczko.poszukiwacz.compass.api.AndroidLocation
import pl.marianjureczko.poszukiwacz.model.HunterPath
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress

interface HasCommemorativePhoto {
    fun hasCommemorativePhoto(treasureId: Int): Boolean
}

interface SelectorSharedState : HasCommemorativePhoto {
    val route: Route
    var treasuresProgress: TreasuresProgress
    val distancesInSteps: Map<Int, Int?>
    fun isTreasureCollected(treasureId: Int): Boolean
    fun allTreasuresCollected(): Boolean
}

interface SearchingSharedState : HasCommemorativePhoto {
    val mediaPlayer: MediaPlayer
    val route: Route
    var treasuresProgress: TreasuresProgress
    var hunterPath: HunterPath

    /** Most recent location reported by the user. Used to decide whether a scanned treasure is the selected one. */
    var lastLocation: AndroidLocation?

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
    override var hunterPath: HunterPath,
    override val distancesInSteps: Map<Int, Int?> = route.treasures
        .associate { it.id to null }
        .toMap(),
    override var lastLocation: AndroidLocation? = null,
) : SelectorSharedState, SearchingSharedState, CommemorativeSharedState {

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