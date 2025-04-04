package pl.marianjureczko.poszukiwacz.screen.phototip

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.marianjureczko.poszukiwacz.screen.Screens
import javax.inject.Inject

@HiltViewModel
class TipPhotoViewModel @Inject constructor(
    private val stateHandle: SavedStateHandle
) : ViewModel() {
    private var _state: MutableState<TipPhotoState> = mutableStateOf(createState())

    val state: State<TipPhotoState>
        get() = _state

    private fun createState(): TipPhotoState {
        val routeName = stateHandle.get<String>(Screens.TipPhoto.PARAMETER_ROUTE_NAME)!!
        val photoPath = stateHandle.get<String>(Screens.TipPhoto.PARAMETER_TIP_PHOTO)!!
        return TipPhotoState(photoPath, routeName)
    }
}