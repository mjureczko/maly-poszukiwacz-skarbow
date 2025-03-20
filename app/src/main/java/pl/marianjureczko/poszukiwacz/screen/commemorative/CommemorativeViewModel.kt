package pl.marianjureczko.poszukiwacz.screen.commemorative

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import pl.marianjureczko.poszukiwacz.screen.Screens
import pl.marianjureczko.poszukiwacz.shared.PhotoHelper
import pl.marianjureczko.poszukiwacz.shared.di.IoDispatcher
import javax.inject.Inject

@HiltViewModel
class CommemorativeViewModel @Inject constructor(
    private val stateHandle: SavedStateHandle,
    private val photoHelper: PhotoHelper,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val TAG = javaClass.simpleName
    private var _state: MutableState<CommemorativeState> = mutableStateOf(createState())

    val state: State<CommemorativeState>
        get() = _state

    fun setPhotoPath(photoPath: String) {
        if (photoPath != state.value.photoPath) {
            _state.value = _state.value.copy(
                photoPath = photoPath
            )
        }
    }

    fun rotatePhoto() {
        if (state.value.photoPath != null) {
            viewModelScope.launch {
                PhotoHelper.rotateGraphicClockwise(ioDispatcher, state.value.photoPath!!) {
                    // refresh view
                    updatePhotoVersionForRefresh()
                }
            }
        }
    }

    fun updatePhotoVersionForRefresh() {
        _state.value = _state.value.copy(photoVersion = _state.value.photoVersion + 1)
    }

    private fun createState(): CommemorativeState {
        return CommemorativeState(
            treasureDesId = stateHandle.get<Int>(Screens.Commemorative.PARAMETER_TREASURE_DESCRIPTION_ID)!!,
            tempPhotoFileLocation = photoHelper.getCommemorativePhotoTempUri(),
            photoPath = stateHandle.get<String>(Screens.Commemorative.PARAMETER_PHOTO_PATH),
        )
    }
}