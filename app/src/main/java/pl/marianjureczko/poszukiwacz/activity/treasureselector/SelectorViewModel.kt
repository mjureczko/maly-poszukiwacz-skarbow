package pl.marianjureczko.poszukiwacz.activity.treasureselector

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import pl.marianjureczko.poszukiwacz.activity.searching.LocationCalculator
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.Treasure
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
import pl.marianjureczko.poszukiwacz.shared.XmlHelper
import java.io.File

interface TreasureDescriptionTemplateProvider {
    fun provide(treasureId: Int, distanceInSteps: Int): String
}

class SelectorViewModel(private val state: SavedStateHandle) : ViewModel() {
    companion object {
        const val IDS_OF_COLLECTED = "ids"
        const val COMMEMORATIVE_PHOTOS = "photos"
        const val TREASURE_DESCRIPTION_ID = "td_id"
    }

    private val TAG = javaClass.simpleName
    private lateinit var route: Route
    lateinit var progress: TreasuresProgress
        private set
    private var userLocation: Coordinates? = null
    private var justFoundTreasure: Treasure? = null
    private var treasureDescriptionSelectedForPhoto: Int? = null
    private val locationCalculator = LocationCalculator()

    fun initialize(route: Route, progress: TreasuresProgress, userLocation: Coordinates?, justFound: Treasure?) {
        this.route = route
        this.progress = progress
        this.userLocation = userLocation
        this.justFoundTreasure = justFound
        state.get<Set<Int>>(IDS_OF_COLLECTED)?.let {
            this.progress.collectedTreasuresDescriptionId.clear()
            this.progress.collectedTreasuresDescriptionId.addAll(it)
        }
        state.get<Map<Int, String>>(COMMEMORATIVE_PHOTOS)?.let {
            this.progress.commemorativePhotosByTreasuresDescriptionIds.clear()
            this.progress.commemorativePhotosByTreasuresDescriptionIds.putAll(it)
        }
        state.get<Int?>(TREASURE_DESCRIPTION_ID)?.let {
            this.treasureDescriptionSelectedForPhoto = it
        }
    }

    fun selectTreasureById(treasureId: Int) {
        route.treasures.asSequence()
            .find { it.id == treasureId }
            ?.let { progress.selectedTreasure = it }
    }

    fun isCollected(treasure: TreasureDescription): Boolean =
        progress.collectedTreasuresDescriptionId.contains(treasure.id)

    fun uncollect(treasure: TreasureDescription) {
        progress.collectedTreasuresDescriptionId.remove(treasure.id)
        state[IDS_OF_COLLECTED] = progress.collectedTreasuresDescriptionId
    }

    fun collect(treasure: TreasureDescription) {
        progress.collectedTreasuresDescriptionId.add(treasure.id)
        state[IDS_OF_COLLECTED] = progress.collectedTreasuresDescriptionId
    }

    fun selectForCommemorativePhoto(treasureDescription: TreasureDescription) {
        treasureDescriptionSelectedForPhoto = treasureDescription.id
        state[TREASURE_DESCRIPTION_ID] = treasureDescriptionSelectedForPhoto
    }

    fun setCommemorativePhotoOnSelectedTreasureDescription(photoLocation: String) {
        if (progress.commemorativePhotosByTreasuresDescriptionIds.contains(treasureDescriptionSelectedForPhoto)) {
            File(progress.commemorativePhotosByTreasuresDescriptionIds[treasureDescriptionSelectedForPhoto]).delete()
            progress.commemorativePhotosByTreasuresDescriptionIds[treasureDescriptionSelectedForPhoto]
        }
        progress.commemorativePhotosByTreasuresDescriptionIds[treasureDescriptionSelectedForPhoto!!] = photoLocation
        state[COMMEMORATIVE_PHOTOS] = progress.commemorativePhotosByTreasuresDescriptionIds
    }

    fun getTreasureDescriptionByPosition(position: Int): TreasureDescription =
        route.treasures[position]

    fun getNumberOfTreasures(): Int =
        route.treasures.size

    fun progressToString(xmlHelper: XmlHelper): String =
        xmlHelper.writeToString(progress)

    fun getSelectedTreasure(): TreasureDescription? =
        progress.selectedTreasure

    fun getIdsOfCollectedTreasures(): Set<Int> =
        progress.collectedTreasuresDescriptionId.toSet()

    fun getCommemorativePhoto(treasureDescription: TreasureDescription) =
        progress.getCommemorativePhoto(treasureDescription)

    fun generateTreasureDesription(treasure: TreasureDescription, treasureDescriptionTemplate: TreasureDescriptionTemplateProvider): String {
        return if (userLocation != null) {
            val distance: Int = locationCalculator.distanceInSteps(treasure, userLocation!!)
            val id: Int = treasure.id
            treasureDescriptionTemplate.provide(id, distance)
        } else {
            treasure.prettyName()
        }
    }

    fun getUserLocation(): Coordinates? =
        userLocation?.copy()

    fun getJustFound(): Treasure? =
        justFoundTreasure

    fun treasureIsNotFarAwayFromUser(): Boolean =
        if (getSelectedTreasure() != null && getUserLocation() != null) {
            val distance = LocationCalculator().distanceInSteps(getSelectedTreasure()!!, getUserLocation()!!)
            Log.i(TAG, "Distance is $distance")
            distance < 60;
        } else {
            false
        }
}