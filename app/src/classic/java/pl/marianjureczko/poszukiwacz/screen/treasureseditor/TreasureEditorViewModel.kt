package pl.marianjureczko.poszukiwacz.screen.treasureseditor

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.activity.searching.n.PARAMETER_ROUTE_NAME
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.shared.Coordinates
import pl.marianjureczko.poszukiwacz.shared.DoPhoto
import pl.marianjureczko.poszukiwacz.shared.PhotoHelper
import pl.marianjureczko.poszukiwacz.shared.port.CameraPort
import pl.marianjureczko.poszukiwacz.shared.port.LocationPort
import pl.marianjureczko.poszukiwacz.shared.port.StorageHelper
import pl.marianjureczko.poszukiwacz.usecase.AddTreasureDescriptionToRoute
import pl.marianjureczko.poszukiwacz.usecase.RemoveTreasureDescriptionFromRoute
import java.io.FileNotFoundException
import javax.inject.Inject

interface GetDoTipPhoto {
    @Composable
    fun getDoPhoto(
        cameraPermissionGranted: Boolean,
        treasure: TreasureDescription
    ): DoPhoto
}

@HiltViewModel
class TreasureEditorViewModel @Inject constructor(
    private val storage: StorageHelper,
    private val stateHandle: SavedStateHandle,
    private val locationPort: LocationPort,
    private val cameraPort: CameraPort,
    private val photoHelper: PhotoHelper,
    private val addTreasureUseCase: AddTreasureDescriptionToRoute,
    private val removeTreasureDescriptionFromRoute: RemoveTreasureDescriptionFromRoute
) : ViewModel(), GetDoTipPhoto {
    private val TAG = javaClass.simpleName
    private var _state: MutableState<TreasureEditorState> = mutableStateOf(createState())

    val state: State<TreasureEditorState>
        get() = _state

    init {
        locationPort.startFetching(viewModelScope) { location ->
            _state.value = _state.value.copy(currentLocation = Coordinates(location.latitude, location.longitude))
        }
    }

    override fun onCleared() {
        Log.i(TAG, "ViewModel cleared")
        locationPort.stopFetching()
        super.onCleared()
    }

    fun addTreasure() {
        state.value.currentLocation?.let { location ->
            val updateRoute: Route = addTreasureUseCase(state.value.route, location)
            _state.value = _state.value.copy(route = updateRoute)
        }
    }

    fun removeTreasure(treasureDescriptionId: Int) {
        val updatedRoute = removeTreasureDescriptionFromRoute(state.value.route, treasureDescriptionId)
        _state.value = state.value.copy(route = updatedRoute)
    }

    @Composable
    override fun getDoPhoto(
        cameraPermissionGranted: Boolean,
        treasure: TreasureDescription
    ): DoPhoto {
        return this.cameraPort.doPhoto(
            cameraPermissionGranted,
            R.string.photo_saved,
            R.string.photo_failed,
            { createUriToPhotoFile(treasure) },
            {}
        )
    }

    fun hideOverridePhotoDialog() {
        _state.value = _state.value.copy(showOverridePhotoDialog = false)
    }

    fun showOverridePhotoDialog(td: TreasureDescription) {
        _state.value = _state.value.copy(
            showOverridePhotoDialog = true,
            treasureWithPhotoTipToOverride = td
        )
    }

    fun hideOverrideSoundTipDialog() {
        _state.value = _state.value.copy(showOverrideSoundTipDialog = false)
    }

    fun showOverrideSoundTipDialog(td: TreasureDescription) {
        _state.value = _state.value.copy(
            showOverrideSoundTipDialog = true,
            treasureWithSoundTipToOverride = td
        )
    }

    // visibility for testing
    fun createUriToPhotoFile(treasureDescription: TreasureDescription): Uri {
        return photoHelper.createPhotoUri(treasureDescription.photoFileName ?: newPhotoTip(treasureDescription))
    }

    fun showSoundRecordingDialog(target: TreasureDescription) {
        val route = assureSoundTipFilePresent(target)
        _state.value = _state.value.copy(
            route = route,
            showSoundRecordingDialog = true,
            fileForTipRecording = route.getTreasureDescriptionById(target.id)!!.tipFileName
        )
    }

    fun hideSoundRecordingDialog() {
        _state.value = _state.value.copy(
            showSoundRecordingDialog = false,
            fileForTipRecording = null
        )
    }

    private fun assureSoundTipFilePresent(treasureDescription: TreasureDescription): Route {
        if (treasureDescription.tipFileName != null) {
            return _state.value.route
        }

        val updatedTreasure = treasureDescription.copy(tipFileName = storage.newSoundFile())
        val treasures = _state.value.route.treasures
            .map { existingTreasure ->
                if (existingTreasure.id == treasureDescription.id) {
                    updatedTreasure
                } else {
                    existingTreasure
                }
            }
        val route = _state.value.route.copy(treasures = treasures.toMutableList())
        storage.save(route)
        return route
    }

    private fun newPhotoTip(treasureDescription: TreasureDescription): String {
        val newPhotoFile = storage.newPhotoFile()
        val updatedTreasure = treasureDescription.copy(photoFileName = newPhotoFile)
        val updatedTreasures = state.value.route.treasures
            .map { treasure -> if (treasure.id == treasureDescription.id) updatedTreasure else treasure }
        val updatedRoute = state.value.route.copy(treasures = updatedTreasures.toMutableList())
        _state.value = _state.value.copy(route = updatedRoute)
        storage.save(_state.value.route)
        return newPhotoFile
    }

    private fun createState(): TreasureEditorState {
        val routeName = stateHandle.get<String>(PARAMETER_ROUTE_NAME)!!
        val routeToEdit = try {
            storage.loadRoute(routeName)
        } catch (e: FileNotFoundException) {
            Route(routeName)
        }

        return TreasureEditorState(
            route = routeToEdit,
            currentLocation = null,
            overridePhotoQuestionProvider = { td: TreasureDescription -> storage.fileNotEmpty(td.photoFileName) },
            overrideSoundTipQuestionProvider = { td: TreasureDescription ->
                storage.fileNotEmpty(td.tipFileName)
            }
        )
    }
}