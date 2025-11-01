package pl.marianjureczko.poszukiwacz.screen.treasureselector

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
import kotlinx.coroutines.withContext
import pl.marianjureczko.poszukiwacz.screen.Screens
import pl.marianjureczko.poszukiwacz.screen.result.NOTHING_FOUND_TREASURE_ID
import pl.marianjureczko.poszukiwacz.shared.PhotoHelper
import pl.marianjureczko.poszukiwacz.shared.di.IoDispatcher
import javax.inject.Inject

@HiltViewModel
class SelectorViewModel @Inject constructor(
    private val stateHandle: SavedStateHandle,
    private val photoHelper: PhotoHelper,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val TAG = javaClass.simpleName
    private var _state: MutableState<SelectorState> = mutableStateOf(createState())

    val state: State<SelectorState>
        get() = _state

    fun delayedUpdateOfJustFound() {
        if (state.value.justFoundTreasureId != NOTHING_FOUND_TREASURE_ID) {
            viewModelScope.launch(ioDispatcher) {
                delay(500)
                withContext(ioDispatcher) {
                    _state.value = _state.value.copy(justFoundTreasureId = NOTHING_FOUND_TREASURE_ID)
                }
            }
        }
    }

    fun wellDoneShown() {
        _state.value = _state.value.copy(wellDoneShown = true)
    }

    private fun createState(): SelectorState {
        val justFoundTreasureId = stateHandle.get<Int>(Screens.Selector.PARAMETER_JUST_FOUND_TREASURE)!!
        return SelectorState(
            justFoundTreasureId,
            photoHelper.getCommemorativePhotoTempUri()
        )
    }
}