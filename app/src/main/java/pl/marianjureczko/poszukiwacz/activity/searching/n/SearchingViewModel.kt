package pl.marianjureczko.poszukiwacz.activity.searching.n

import android.content.res.Resources
import android.location.Location
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
import pl.marianjureczko.poszukiwacz.activity.result.n.ResultType
import pl.marianjureczko.poszukiwacz.activity.searching.ArcCalculator
import pl.marianjureczko.poszukiwacz.activity.searching.LocationCalculator
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.Treasure
import pl.marianjureczko.poszukiwacz.model.TreasureType
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
import pl.marianjureczko.poszukiwacz.shared.StorageHelper
import javax.inject.Inject

//TODO: consider shared ViewModel to avoid reloading content from disc across screens, use debuger to check when ViewModel is reated

const val PARAMETER_ROUTE_NAME = "route_name"

@HiltViewModel
class SearchingViewModel @Inject constructor(
    private val storageHelper: StorageHelper,
    private val locationFetcher: LocationFetcher,
    private val locationCalculator: LocationCalculator,
    private val stateHandle: SavedStateHandle,
    private val resources: Resources
) : ViewModel() {
    private val TAG = javaClass.simpleName
    private var _state: MutableState<SearchingState> = mutableStateOf(createState())
    private val arcCalculator = ArcCalculator()

    val state: State<SearchingState>
        get() = _state

    init {
        locationFetcher.startFetching(viewModelScope) {
            updateLocation(it)
        }
    }

    //TODO: test the logic
    fun scannedTreasureCallback(goToResults: (ResultType) -> Unit): (ScanIntentResult?) -> Unit {
        return { scanResult ->
            var result: ResultType? = null
            if (scanResult != null && !scanResult.contents.isNullOrEmpty()) {
                val newCode = scanResult.contents
                val foundTreasure = state.value.route.treasures.find { treasure ->
                    newCode == treasure.qrCode
                }
                if (foundTreasure == null) {
                    //TODO message it's not a treasure
                    result = ResultType.NOT_A_TREASURE
                } else {
                    val treasuresProgress = state.value.treasuresProgress
                    if (treasuresProgress.collectedTreasuresDescriptionId.contains(foundTreasure.id)) {
                        //TODO message it's already collected
                        result = ResultType.ALREADY_TAKEN
                    } else {
                        //TODO t: factory method for KnowledgeTreasure
                        treasuresProgress.collect(Treasure(newCode, 1, TreasureType.KNOWLEDGE))
                        treasuresProgress.collect(foundTreasure)
                        storageHelper.save(treasuresProgress)
                        //TODO message show collected treasure
                        result = ResultType.TREASURE
                    }
                }
            }
            result?.let {
                goToResults(it)
            }
        }
    }

    override fun onCleared() {
        locationFetcher.stopFetching()
        state.value.mediaPlayer.release()
        super.onCleared()
    }

    private fun updateLocation(location: Location) {
        _state.value = _state.value.copy(
            currentLocation = location,
            stepsToTreasure = locationCalculator.distanceInSteps(state.value.currentTreasure, location),
            needleRotation = arcCalculator.degree(
                state.value.currentTreasure.longitude,
                state.value.currentTreasure.latitude,
                location.longitude,
                location.latitude
            ).toFloat()
        )
    }

    private fun createState(): SearchingState {
        val route = loadRoute()
        val treasuresProgress = loadProgress(route.name)
        val mediaPlayer = MediaPlayer()
        mediaPlayer!!.isLooping = false
        mediaPlayer!!.setOnErrorListener { mp, what, extra -> handleMediaPlayerError(what, extra) }
        return SearchingState(mediaPlayer, route, treasuresProgress)
    }

    private fun loadRoute(): Route {
//        return Route(
//            "Kalinowice", mutableListOf(
//                TreasureDescription(
//                    1, 25.1, 26.1, "g01abc", null, null
//                )
//            )
//        )
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
//    fun loadProgressFromStorage(storageHelper: StorageHelper) {
//        var loadedProgress = storageHelper.loadProgress(route.name)
//        if (loadedProgress == null) {
//            loadedProgress = TreasuresProgress(route.name)
//            storageHelper.save(loadedProgress)
//        }
//        treasuresProgress = loadedProgress
//        hunterPath = storageHelper.loadHunterPath(route.name)
//        if(hunterPath == null) {
//            hunterPath = HunterPath(route.name)
//            storageHelper.save(hunterPath!!)
//        }
//    }

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