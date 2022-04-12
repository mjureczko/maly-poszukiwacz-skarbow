package pl.marianjureczko.poszukiwacz.activity.searching

import android.location.Location
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import pl.marianjureczko.poszukiwacz.activity.treasureselector.SelectTreasureInputData
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.Treasure
import pl.marianjureczko.poszukiwacz.model.TreasureBag
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.shared.StorageHelper
import pl.marianjureczko.poszukiwacz.shared.XmlHelper

class SearchingActivityViewModel(private val state: SavedStateHandle) : ViewModel(), DataStorageWrapper, TreasuresStorage, TipNameProvider {
    companion object {
        const val TREASURE_SELECTION_INITIALIZED = "initialized"
    }

    private val TAG = javaClass.simpleName
    private val xmlHelper = XmlHelper()
    private lateinit var route: Route
    private lateinit var treasureBag: TreasureBag
    private var currentLocation: Location? = null
    private var treasureSelectionInitialized: Boolean = state.get<Boolean>(TREASURE_SELECTION_INITIALIZED) ?: false
        private set(value) {
            field = value
            state[TREASURE_SELECTION_INITIALIZED] = value
        }

    override fun getSelectedForHuntTreasure(): TreasureDescription? {
        return treasureBag.selectedTreasure
    }

    override fun getTreasureSelectorActivityInputData(): SelectTreasureInputData {
        treasureSelectionInitialized = true
        return SelectTreasureInputData(route, treasureBag, currentLocation)
    }

    override fun setCurrentLocation(location: Location?) {
        currentLocation = location
    }

    fun initialize(routeXml: String, storageHelper: StorageHelper) {
        route = xmlHelper.loadFromString<Route>(routeXml)
        treasureBag = storageHelper.load(route.name) ?: TreasureBag(route.name)
    }

    override fun tipName(): String? =
        treasureBag.selectedTreasure?.tipFileName

    fun treasureSelectionInitialized() =
        treasureSelectionInitialized || treasureBag.selectedTreasure != null

    fun treasureIsAlreadyCollected(treasure: Treasure): Boolean =
        treasureBag.contains(treasure)

    fun collectTreasure(treasure: Treasure, storageHelper: StorageHelper) {
        treasureBag.collect(treasure)
        storageHelper.save(this.treasureBag)
    }

    fun getGolds(): String =
        treasureBag.golds.toString()

    fun getRubies(): String =
        treasureBag.rubies.toString()

    fun getDiamonds(): String =
        treasureBag.diamonds.toString()

    fun replaceTreasureBag(treasureBag: TreasureBag, storageHelper: StorageHelper) {
        this.treasureBag = treasureBag
        storageHelper.save(this.treasureBag)
    }

    //visibility for tests
    internal fun getRoute() = route
    internal fun getTreasureBag() = treasureBag
}