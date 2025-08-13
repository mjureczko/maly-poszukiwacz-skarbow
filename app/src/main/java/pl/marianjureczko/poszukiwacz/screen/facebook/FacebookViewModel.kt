package pl.marianjureczko.poszukiwacz.screen.facebook

import android.content.res.Resources
import android.graphics.BitmapFactory
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
import pl.marianjureczko.poszukiwacz.screen.searching.LocationCalculator
import pl.marianjureczko.poszukiwacz.shared.PhotoHelper
import pl.marianjureczko.poszukiwacz.shared.PhotoScalingHelper
import pl.marianjureczko.poszukiwacz.shared.RotatePhoto
import pl.marianjureczko.poszukiwacz.shared.di.DefaultDispatcher
import pl.marianjureczko.poszukiwacz.shared.di.IoDispatcher
import pl.marianjureczko.poszukiwacz.shared.port.storage.StoragePort
import javax.inject.Inject

const val PARAMETER_ROUTE_NAME = "route_name"

@HiltViewModel
class FacebookViewModel @Inject constructor(
    private val stateHandle: SavedStateHandle,
    private val storagePort: StoragePort,
    val locationCalculator: LocationCalculator,
    private val resources: Resources,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private var _state: MutableState<FacebookState> = mutableStateOf(createState())

    val state: State<FacebookState>
        get() = _state

    fun changeSelectionAt(index: Int) {
        val updated = state.value.elements.toMutableList()
        updated[index] = updated[index].copy(isSelected = !updated[index].isSelected)
        _state.value = state.value.copy(elements = updated.toList())
    }

    fun rotatePhoto(): RotatePhoto = { photoIdx: Int ->
        viewModelScope.launch(defaultDispatcher) {
            state.value.elements[photoIdx].photo?.let {
                PhotoHelper.rotateGraphicClockwise(ioDispatcher, it) {
                    preparePhoto(photoIdx, it)
                }
            }
        }
    }

    private fun createState(): FacebookState {
        var index = 0
        val routeName = stateHandle.get<String>(PARAMETER_ROUTE_NAME)!!
        val route = storagePort.loadRoute(routeName)
        val progress = storagePort.loadProgress(routeName) ?: TreasuresProgress(routeName, route.treasures[0].id)
        val path = storagePort.loadHunterPath(routeName)

        val elements = mutableListOf<ElementDescription>()
        elements.add(
            ElementDescription(
                index++,
                Type.TREASURES_SUMMARY,
                true,
                resources.getString(R.string.collected_treasures)
            )
        )
        val treasure = resources.getString(R.string.treasure)
        progress.commemorativePhotosByTreasuresDescriptionIds.forEach { (id, photo) ->
            elements.add(
                ElementDescription(
                    index++,
                    Type.COMMEMORATIVE_PHOTO,
                    true,
                    "$treasure $id",
                    orderNumber = id,
                    photo = photo
                )
            )
            preparePhoto(index - 1, photo)
        }
        elements.add(
            ElementDescription(
                index++,
                Type.MAP,
                true,
                resources.getString(R.string.treasures_map)
            )
        )
        elements.add(
            ElementDescription(
                index++,
                Type.MAP_ROUTE,
                true,
                resources.getString(R.string.route_on_map)
            )
        )
        elements.add(
            ElementDescription(
                index++,
                Type.MAP_SUMMARY,
                true,
                resources.getString(R.string.treasures_map_summary)
            )
        )

        return FacebookState(path, progress, route, elements)
    }

    private fun preparePhoto(index: Int, photoFile: String) {
        viewModelScope.launch(defaultDispatcher) {
            val photo = BitmapFactory.decodeFile(photoFile)
            val readyPhoto = PhotoScalingHelper.scalePhotoKeepRatio(photo, 250f, 300f)
            val elements = state.value.elements.toMutableList()
            elements[index] = elements[index].copy(scaledPhoto = readyPhoto)
            _state.value = state.value.copy(elements = elements.toList())
        }
    }
}