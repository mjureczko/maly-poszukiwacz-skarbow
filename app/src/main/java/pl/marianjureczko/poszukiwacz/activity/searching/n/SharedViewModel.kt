package pl.marianjureczko.poszukiwacz.activity.searching.n

import android.media.MediaPlayer
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.activity.searching.ArcCalculator
import pl.marianjureczko.poszukiwacz.activity.searching.LocationCalculator
import pl.marianjureczko.poszukiwacz.model.HunterPath
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.Treasure
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.model.TreasureParser
import pl.marianjureczko.poszukiwacz.model.TreasureType
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
import pl.marianjureczko.poszukiwacz.screen.result.NOTHING_FOUND_TREASURE_ID
import pl.marianjureczko.poszukiwacz.screen.result.ResultType
import pl.marianjureczko.poszukiwacz.screen.searching.QrScannerPort
import pl.marianjureczko.poszukiwacz.shared.Coordinates
import pl.marianjureczko.poszukiwacz.shared.DoPhoto
import pl.marianjureczko.poszukiwacz.shared.GoToResults
import pl.marianjureczko.poszukiwacz.shared.PhotoHelper
import pl.marianjureczko.poszukiwacz.shared.ScanTreasureCallback
import pl.marianjureczko.poszukiwacz.shared.di.IoDispatcher
import pl.marianjureczko.poszukiwacz.shared.port.CameraPort
import pl.marianjureczko.poszukiwacz.shared.port.LocationPort
import pl.marianjureczko.poszukiwacz.shared.port.StorageHelper
import javax.inject.Inject

const val PARAMETER_ROUTE_NAME = "route_name"

interface DoCommemorative {
    @Composable
    fun getDoPhoto(cameraPermissionGranted: Boolean, treasureDesId: Int, refreshOnSuccess: () -> Unit): DoPhoto
}

interface ResultSharedViewModel {
    fun resultPresented()
    fun getRouteName(): String
}

interface SearchingViewModel : DoCommemorative {
    val state: State<SharedState>
    val qrScannerPort: QrScannerPort
    fun scannedTreasureCallback(goToResults: GoToResults): ScanTreasureCallback
}

interface SelectorSharedViewModel : DoCommemorative {
    val state: State<SharedState>
    fun updateJustFoundFromSelector()
    fun selectorPresented()
    fun updateSelectedTreasure(treasure: TreasureDescription)
    fun toggleTreasureDescriptionCollected(treasureDescriptionId: Int)
}

interface CommemorativeSharedViewModel : DoCommemorative {
    val state: State<SharedState>
}

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val storageHelper: StorageHelper,
    private val locationPort: LocationPort,
    private val locationCalculator: LocationCalculator,
    private val photoHelper: PhotoHelper,
    private val stateHandle: SavedStateHandle,
    private val cameraPort: CameraPort,
    override val qrScannerPort: QrScannerPort,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : SearchingViewModel, ResultSharedViewModel, SelectorSharedViewModel, CommemorativeSharedViewModel, ViewModel() {
    private val TAG = javaClass.simpleName
    private var _state: MutableState<SharedState> = mutableStateOf(createState())
    private val arcCalculator = ArcCalculator()

    //for test
    var respawn: Boolean = true

    init {
        locationPort.startFetching(viewModelScope, updatedLocationCallback())
    }

    override val state: State<SharedState>
        get() = _state

    override fun scannedTreasureCallback(goToResults: GoToResults): ScanTreasureCallback {
        return { scanedContent ->
            /*
            * newCode will contain code like k01001
            * result will be NOT_A_TREASURE or ALREADY_TAKEN or TREASURE
             */
            var result: ResultType? = null
            var treasureId = NOTHING_FOUND_TREASURE_ID
            var scannedTreasure: Treasure? = null
            if (!scanedContent.isNullOrEmpty()) {
                val newCode = scanedContent
                try {
                    scannedTreasure = TreasureParser().parse(newCode)
                    val tdFinder = JustFoundTreasureDescriptionFinder(state.value.route.treasures)
                    val foundTd: TreasureDescription? = tdFinder.findTreasureDescription(
                        justFoundTreasure = scannedTreasure,
                        selectedTreasureDescription = state.value.selectedTreasureDescription(),
                        userLocation = state.value.currentLocation?.let { Coordinates.of(it) }
                    )
                    var treasuresProgress: TreasuresProgress = state.value.treasuresProgress
                    if (treasuresProgress.contains(scannedTreasure)) {
                        result = ResultType.ALREADY_TAKEN
                    } else {
                        if (scannedTreasure.type == TreasureType.KNOWLEDGE && state.value.route.treasures.find { t -> newCode == t.qrCode } == null) {
                            throw IllegalArgumentException("Unknown knowledge treasure")
                        }
                        foundTd?.let { treasureId = it.id }
                        treasuresProgress = updateTreasureProgress(treasuresProgress, scannedTreasure, foundTd)
                        _state.value = state.value.copy(treasuresProgress = treasuresProgress)
                        result = ResultType.from(scannedTreasure.type)
                    }
                } catch (e: IllegalArgumentException) {
                    result = ResultType.NOT_A_TREASURE
                }
            }
            result?.let {
                goToResults(state.value.route.name, it, treasureId, scannedTreasure?.quantity)
            }
        }
    }

    private fun updateTreasureProgress(
        treasuresProgress: TreasuresProgress,
        foundTreasure: Treasure,
        foundTd: TreasureDescription?
    ): TreasuresProgress {
        var result: TreasuresProgress = treasuresProgress.collect(foundTreasure, foundTd)
        result = result.copy(
            resultRequiresPresentation = true,
            justFoundTreasureId = foundTd?.id,
            treasureFoundGoToSelector = true
        )
        storageHelper.save(result)
        return result
    }

    override fun resultPresented() {
        viewModelScope.launch(ioDispatcher) {
            delay(500)
            if (state.value.treasuresProgress.resultRequiresPresentation == true) {
                _state.value = state.value.copy(
                    treasuresProgress = state.value.treasuresProgress.copy(resultRequiresPresentation = false)
                )
                storageHelper.save(_state.value.treasuresProgress)
            }
        }
    }

    override fun getRouteName(): String {
        return state.value.route.name
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

    override fun updateSelectedTreasure(td: TreasureDescription) {
        _state.value = state.value.copy(
            treasuresProgress = state.value.treasuresProgress.copy(
                selectedTreasureDescriptionId = td.id
            )
        )
    }

    override fun toggleTreasureDescriptionCollected(treasureDescriptionId: Int) {
        _state.value = state.value.copy(
            treasuresProgress = state.value.treasuresProgress.toggleTreasureDescriptionCollected(treasureDescriptionId)
        )
        storageHelper.save(state.value.treasuresProgress)
    }

    override fun onCleared() {
        Log.i(TAG, "ViewModel cleared")
        locationPort.stopFetching()
        state.value.mediaPlayer.release()
        super.onCleared()
    }

    @Composable
    override fun getDoPhoto(
        cameraPermissionGranted: Boolean,
        treasureDesId: Int,
        refreshOnSuccess: () -> Unit,
    ): DoPhoto {
        return this.cameraPort.doPhoto(
            cameraPermissionGranted,
            R.string.photo_saved,
            R.string.photo_not_replaced,
            { photoHelper.getCommemorativePhotoTempUri() },
            handleSuccess = {
                handleDoCommemorativePhotoResult(state.value.route.treasures.find { it.id == treasureDesId }!!)
                refreshOnSuccess()
            }
        )
    }

    private fun handleDoCommemorativePhotoResult(treasureDescription: TreasureDescription) {
        val target = _state.value.treasuresProgress.getCommemorativePhoto(treasureDescription)
            ?: storageHelper.newCommemorativePhotoFile()
        photoHelper.moveCommemorativePhotoToPermanentLocation(target)
        val updatedMapOfPhotos = state.value.treasuresProgress.commemorativePhotosByTreasuresDescriptionIds
            .plus(treasureDescription.id to target)
            .toMap()
        _state.value = _state.value.copy(
            treasuresProgress = state.value.treasuresProgress.copy(
                commemorativePhotosByTreasuresDescriptionIds = updatedMapOfPhotos
            )
        )
        storageHelper.save(state.value.treasuresProgress)
    }

    private fun updatedLocationCallback(): UpdateLocationCallback = { location ->
        Log.i(TAG, "location updated")
        val selectedTreasure = state.value.selectedTreasureDescription()
        _state.value = _state.value.copy(
            currentLocation = location,
            stepsToTreasure = if (selectedTreasure != null) {
                locationCalculator.distanceInSteps(selectedTreasure, location)
            } else 0,
            needleRotation = if (selectedTreasure != null) {
                arcCalculator.degree(
                    selectedTreasure.longitude,
                    selectedTreasure.latitude,
                    location.longitude,
                    location.latitude
                ).toFloat()
            } else 0f,
            distancesInSteps = _state.value.route.treasures
                .associate { it.id to locationCalculator.distanceInSteps(it, location) }
                .toMap()
        )
        val currentCoordinates = Coordinates(location.latitude, location.longitude)
        if (state.value.hunterPath.addLocation(currentCoordinates)) {
            storageHelper.save(state.value.hunterPath)
        }
    }

    private fun createState(): SharedState {
        Log.i(TAG, "Create state")
        val route = loadRoute()
        val treasuresProgress = loadProgress(route)
        val hunterPath = loadHunterPath(route.name)
        val mediaPlayer = MediaPlayer()
        mediaPlayer.isLooping = false
        mediaPlayer.setOnErrorListener { mp, what, extra -> handleMediaPlayerError(what, extra) }
        return SharedState(
            mediaPlayer,
            route,
            treasuresProgress,
            hunterPath
        )
    }

    private fun loadRoute(): Route {
        return storageHelper.loadRoute(stateHandle.get<String>(PARAMETER_ROUTE_NAME)!!)
    }

    private fun loadProgress(route: Route): TreasuresProgress {
        var loadedProgress = storageHelper.loadProgress(route.name)
        if (loadedProgress == null) {
            loadedProgress = TreasuresProgress(route.name, route.treasures[0].id)
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