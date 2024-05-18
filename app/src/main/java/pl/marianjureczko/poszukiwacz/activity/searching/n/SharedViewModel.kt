package pl.marianjureczko.poszukiwacz.activity.searching.n

import android.media.MediaPlayer
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.journeyapps.barcodescanner.ScanIntentResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pl.marianjureczko.poszukiwacz.activity.result.n.NOTHING_FOUND_TREASURE_ID
import pl.marianjureczko.poszukiwacz.activity.result.n.ResultType
import pl.marianjureczko.poszukiwacz.activity.searching.ArcCalculator
import pl.marianjureczko.poszukiwacz.activity.searching.LocationCalculator
import pl.marianjureczko.poszukiwacz.activity.treasureselector.Coordinates
import pl.marianjureczko.poszukiwacz.model.HunterPath
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.Treasure
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
import pl.marianjureczko.poszukiwacz.shared.PhotoHelper
import pl.marianjureczko.poszukiwacz.shared.StorageHelper
import javax.inject.Inject

const val PARAMETER_ROUTE_NAME = "route_name"

interface SearchingViewModel {
    val state: State<SharedState>
    fun scannedTreasureCallback(goToResults: (ResultType, Int) -> Unit): (ScanIntentResult?) -> Unit
}

interface ResultSharedViewModel {
    fun resultPresented()
}

interface SelectorSharedViewModel {
    val state: State<SharedState>
    fun updateJustFoundFromSelector()
    fun selectorPresented()
    fun updateCurrentTreasure(treasure: TreasureDescription)
    fun handleDoCommemorativePhotoResult(treasure: TreasureDescription): () -> Unit
}

interface CommemorativeSharedViewModel {
    val state: State<SharedState>

    fun handleDoCommemorativePhotoResult(treasure: TreasureDescription): () -> Unit
}

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val storageHelper: StorageHelper,
    private val locationFetcher: LocationFetcher,
    private val locationCalculator: LocationCalculator,
    private val photoHelper: PhotoHelper,
    private val stateHandle: SavedStateHandle,
    private val dispatcher: CoroutineDispatcher
) : SearchingViewModel, ResultSharedViewModel, SelectorSharedViewModel, CommemorativeSharedViewModel, ViewModel() {
    private val TAG = javaClass.simpleName
    private var _state: MutableState<SharedState> = mutableStateOf(createState())
    private val arcCalculator = ArcCalculator()

    init {
        startFetchingLocation()
        // after between screen navigation location updating may stop, hence needs to be retriggered periodically
        viewModelScope.launch(dispatcher) {
            while (true) {
                locationFetcher.stopFetching()
                startFetchingLocation()
                delay(20_000)
            }
        }
    }

    override val state: State<SharedState>
        get() = _state

    //TODO: introduce types for callbacks used as parameters
    override fun scannedTreasureCallback(goToResults: (ResultType, Int) -> Unit): (ScanIntentResult?) -> Unit {
        return { scanResult ->
            var result: ResultType? = null
            var treasureId = NOTHING_FOUND_TREASURE_ID
            if (scanResult != null && !scanResult.contents.isNullOrEmpty()) {
                val newCode = scanResult.contents
                val foundTreasure = state.value.route.treasures.find { treasure ->
                    newCode == treasure.qrCode
                }
                if (foundTreasure == null) {
                    result = ResultType.NOT_A_TREASURE
                } else {
                    val treasuresProgress = state.value.treasuresProgress
                    if (treasuresProgress.collectedTreasuresDescriptionId.contains(foundTreasure.id)) {
                        result = ResultType.ALREADY_TAKEN
                    } else {
                        treasuresProgress.collect(Treasure.knowledgeTreasure(newCode))
                        treasuresProgress.collect(foundTreasure)
                        treasuresProgress.resultRequiresPresentation = true
                        treasuresProgress.justFoundTreasureId = foundTreasure.id
                        treasuresProgress.treasureFoundGoToSelector = true
                        treasureId = foundTreasure.id
                        storageHelper.save(treasuresProgress)
                        //TODO message show collected treasure
                        result = ResultType.TREASURE
                    }
                }
            }
            result?.let {
                goToResults(it, treasureId)
            }
        }
    }

    override fun resultPresented() {
        viewModelScope.launch(dispatcher) {
            delay(500)
            if (state.value.treasuresProgress.resultRequiresPresentation == true) {
                state.value.treasuresProgress.resultRequiresPresentation = false
                storageHelper.save(state.value.treasuresProgress)
            }
        }
    }

    override fun updateJustFoundFromSelector() {
        if (state.value.treasuresProgress.justFoundTreasureId != null) {
            state.value.treasuresProgress.justFoundTreasureId = null
            storageHelper.save(state.value.treasuresProgress)
        }
    }

    override fun selectorPresented() {
        state.value.treasuresProgress.treasureFoundGoToSelector = false
        storageHelper.save(state.value.treasuresProgress)
    }

    override fun updateCurrentTreasure(treasure: TreasureDescription) {
        _state.value = _state.value.copy(currentTreasure = treasure)
    }

    override fun handleDoCommemorativePhotoResult(treasure: TreasureDescription): () -> Unit {
        return {
            val target = _state.value.treasuresProgress.getCommemorativePhoto(treasure)
                ?: storageHelper.newCommemorativePhotoFile()
            photoHelper.moveCommemorativePhotoToPermanentLocation(target)
            _state.value.treasuresProgress.addCommemorativePhoto(treasure, target)
        }
    }

    override fun onCleared() {
        Log.i(TAG, "ViewModel cleared")
        locationFetcher.stopFetching()
        state.value.mediaPlayer.release()
        super.onCleared()
    }

    private fun startFetchingLocation() {
        locationFetcher.startFetching(1_000, viewModelScope) { location ->
            Log.i(TAG, "location updated")
            _state.value = _state.value.copy(
                currentLocation = location,
                stepsToTreasure = locationCalculator.distanceInSteps(state.value.currentTreasure, location),
                needleRotation = arcCalculator.degree(
                    state.value.currentTreasure.longitude,
                    state.value.currentTreasure.latitude,
                    location.longitude,
                    location.latitude
                ).toFloat(),
                distancesInSteps = _state.value.route.treasures
                    .associate { it.id to locationCalculator.distanceInSteps(it, location) }
                    .toMap()
            )
            val currentCoordinates = Coordinates(location.latitude, location.longitude)
            if (state.value.hunterPath.addLocation(currentCoordinates)) {
                storageHelper.save(state.value.hunterPath)
            }
        }
    }

    private fun createState(): SharedState {
        Log.i(TAG, "Create state")
        val route = loadRoute()
        val treasuresProgress = loadProgress(route.name)
        val hunterPath = loadHunterPath(route.name)
        val mediaPlayer = MediaPlayer()
        mediaPlayer.isLooping = false
        mediaPlayer.setOnErrorListener { mp, what, extra -> handleMediaPlayerError(what, extra) }
        return SharedState(mediaPlayer, route, treasuresProgress, hunterPath)
    }

    private fun loadRoute(): Route {
        return storageHelper.loadRoute(stateHandle.get<String>(PARAMETER_ROUTE_NAME)!!)
    }

    private fun loadProgress(routeName: String): TreasuresProgress {
        var loadedProgress = storageHelper.loadProgress(routeName)
        if (loadedProgress == null) {
            loadedProgress = TreasuresProgress(routeName)
            storageHelper.save(loadedProgress)
        }
        return loadedProgress
    }

    private fun loadHunterPath(routeName: String): HunterPath {
        var hunterPath = storageHelper.loadHunterPath(routeName)
        if (hunterPath == null) {
            hunterPath = HunterPath(routeName)
            storageHelper.save(hunterPath)
        }
        return hunterPath
    }

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
}