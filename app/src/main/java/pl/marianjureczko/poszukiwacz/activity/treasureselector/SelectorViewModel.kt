package pl.marianjureczko.poszukiwacz.activity.treasureselector

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import pl.marianjureczko.poszukiwacz.activity.searching.LocationCalculator
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureBag
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.shared.XmlHelper

interface TreasureDescriptionTemplateProvider {
    fun provide(treasureId: Int, distanceInSteps: Int): String
}

class SelectorViewModel(private val state: SavedStateHandle) : ViewModel() {
    companion object {
        const val IDS_OF_COLLECTED = "ids"
    }

    private lateinit var route: Route
    private lateinit var progress: TreasureBag
    private var userLocation: Coordinates? = null
    private val locationCalculator = LocationCalculator()

    fun initialize(route: Route, progress: TreasureBag, userLocation: Coordinates?) {
        this.route = route
        this.progress = progress
        this.userLocation = userLocation
        state.get<Set<Int>>(IDS_OF_COLLECTED)?.let {
            this.progress.collectedTreasuresDescriptionId.clear()
            this.progress.collectedTreasuresDescriptionId.addAll(it)
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
}