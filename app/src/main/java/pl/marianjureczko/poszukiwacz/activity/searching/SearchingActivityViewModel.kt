package pl.marianjureczko.poszukiwacz.activity.searching

import android.location.Location
import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import pl.marianjureczko.poszukiwacz.activity.treasureselector.Coordinates
import pl.marianjureczko.poszukiwacz.activity.treasureselector.SelectTreasureInputData
import pl.marianjureczko.poszukiwacz.model.HunterPath
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.Treasure
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
import pl.marianjureczko.poszukiwacz.shared.StorageHelper
import pl.marianjureczko.poszukiwacz.shared.XmlHelper

class SearchingActivityViewModel(private val state: SavedStateHandle) : ViewModel(), DataStorageWrapper, TreasuresStorage, TipResourcesProvider {
    companion object {
        const val TREASURE_SELECTION_INITIALIZED = "initialized"
        const val CURRENT_COORDINATES = "coordinates"
        const val PATH = "path"
    }

    private val TAG = javaClass.simpleName
    private val xmlHelper = XmlHelper()
    private lateinit var route: Route
    //TODO: state should be employed as property is mutable
    private lateinit var treasuresProgress: TreasuresProgress
    private var currentLocation: Location? = null
    private var currentCoordinates: Coordinates? = state.get<Coordinates?>(CURRENT_COORDINATES)
    private var treasureSelectionInitialized: Boolean = state.get<Boolean>(TREASURE_SELECTION_INITIALIZED) ?: false
        private set(value) {
            field = value
            state[TREASURE_SELECTION_INITIALIZED] = value
        }

    private var hunterPath: HunterPath? = deserializeHunterPath(state.get<String>(PATH))
        private set(value) {
            if (value != null) {
                field = value
                state[PATH] = xmlHelper.writeToString(value)
            }
        }
    private var mediaPlayer: MediaPlayer? = null

    fun initialize(routeXml: String, storageHelper: StorageHelper) {
        route = xmlHelper.loadFromString<Route>(routeXml)
        loadProgressFromStorage(storageHelper)
    }

    override fun getSelectedForHuntTreasure(): TreasureDescription? {
        return treasuresProgress.selectedTreasure
    }

    override fun getTreasureSelectorInputData(
        treasureCollected: Boolean,
        justFoundTreasureDesc: TreasureDescription?
    ): SelectTreasureInputData {
        treasureSelectionInitialized = true
        return SelectTreasureInputData(treasureCollected, route, treasuresProgress, currentCoordinates, justFoundTreasureDesc)
    }

    fun tryToFindTreasureDescription(justFoundTreasure: Treasure?) =
        JustFoundFinder(justFoundTreasure, getSelectedForHuntTreasure(), currentCoordinates)
            .findTreasureDescription()

    override fun setCurrentLocation(location: Location?, storageHelper: StorageHelper) {
        currentLocation = location
        location?.let {
            currentCoordinates = Coordinates(it.latitude, it.longitude)
            state[CURRENT_COORDINATES] = currentCoordinates
            val hp = hunterPath!!
            if (hp.addLocation(currentCoordinates!!)) {
                storageHelper.save(hp)
            }
            //to call setter
            hunterPath = hp
        }
    }

    override fun getTreasuresProgress(): TreasuresProgress = treasuresProgress

    override fun tipName(): String? =
        treasuresProgress.selectedTreasure?.tipFileName

    override fun mediaPlayer(): MediaPlayer {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
            mediaPlayer!!.isLooping = false
            mediaPlayer!!.setOnErrorListener { mp, what, extra -> handleMediaPlayerError(what, extra) }
        }
        return mediaPlayer!!
    }

    fun releaseMediaPlayer() =
        mediaPlayer?.release()

    fun treasureSelectionInitialized() =
        treasureSelectionInitialized || treasuresProgress.selectedTreasure != null

    fun collectTreasure(treasure: Treasure, storageHelper: StorageHelper) {
        treasuresProgress.collect(treasure)
        storageHelper.save(this.treasuresProgress)
    }

    fun getGolds(): String =
        treasuresProgress.golds.toString()

    fun getRubies(): String =
        treasuresProgress.rubies.toString()

    fun getDiamonds(): String =
        treasuresProgress.diamonds.toString()

    fun replaceProgress(treasuresProgress: TreasuresProgress, storageHelper: StorageHelper) {
        this.treasuresProgress = treasuresProgress
        storageHelper.save(this.treasuresProgress)
    }

    fun loadProgressFromStorage(storageHelper: StorageHelper) {
        var loadedProgress = storageHelper.loadProgress(route.name)
//        if (loadedProgress == null) {
//            loadedProgress = TreasuresProgress(route.name)
//            storageHelper.save(loadedProgress)
//        }
        treasuresProgress = loadedProgress!!
        hunterPath = storageHelper.loadHunterPath(route.name)
        if(hunterPath == null) {
            hunterPath = HunterPath(route.name)
            storageHelper.save(hunterPath!!)
        }
    }

    //visibility for tests
    internal fun getRoute() = route
    internal fun getProgress() = treasuresProgress

    private fun handleMediaPlayerError(what: Int, extra: Int): Boolean {
        when (what) {
            MediaPlayer.MEDIA_ERROR_UNKNOWN -> Log.e(TAG, "An unknown error occurred: $extra")
            MediaPlayer.MEDIA_ERROR_IO -> Log.e(TAG, "I/O error occurred: $extra")
            MediaPlayer.MEDIA_ERROR_MALFORMED -> Log.e(TAG, "Media file is malformed: $extra")
            MediaPlayer.MEDIA_ERROR_UNSUPPORTED -> Log.e(TAG, "Unsupported media type: $extra")
            MediaPlayer.MEDIA_ERROR_TIMED_OUT -> Log.e(TAG, "Operation timed out: $extra")
            else -> Log.e(TAG, "An unknown error occurred: $extra")
        }
        return true
    }

    private fun deserializeHunterPath(xml: String?): HunterPath? {
        xml?.let {
            return xmlHelper.loadFromString<HunterPath>(it)
        }
        return null
    }
}