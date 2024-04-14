package pl.marianjureczko.poszukiwacz.activity.commemorative.n

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pl.marianjureczko.poszukiwacz.shared.PhotoHelper
import javax.inject.Inject

const val PARAMETER_TREASURE_DESCRIPTION_ID = "treasure_description_id"

@HiltViewModel
class CommemorativeViewModel @Inject constructor(
    private val stateHandle: SavedStateHandle,
    private val photoHelper: PhotoHelper
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
        if(state.value.photoPath != null) {
            viewModelScope.launch {
                PhotoHelper.rotateGraphicClockwise(state.value.photoPath!!) {
                    // refresh view
                    _state.value = _state.value.copy()
                }
            }
        }
    }

    private fun createState(): CommemorativeState {
        return CommemorativeState(stateHandle.get<Int>(
            PARAMETER_TREASURE_DESCRIPTION_ID)!!,
            photoHelper.getCommemorativePhotoTempUri(),
            null)
    }
}