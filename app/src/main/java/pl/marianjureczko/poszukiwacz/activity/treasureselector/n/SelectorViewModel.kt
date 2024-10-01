package pl.marianjureczko.poszukiwacz.activity.treasureselector.n

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pl.marianjureczko.poszukiwacz.activity.result.n.NOTHING_FOUND_TREASURE_ID
import pl.marianjureczko.poszukiwacz.shared.PhotoHelper
import pl.marianjureczko.poszukiwacz.shared.di.IoDispatcher
import javax.inject.Inject

const val PARAMETER_JUST_FOUND_TREASURE = "just_found_treasure_id"

@HiltViewModel
class SelectorViewModel @Inject constructor(
    private val stateHandle: SavedStateHandle,
    private val photoHelper: PhotoHelper,
    @ApplicationContext private val appContext: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val TAG = javaClass.simpleName
    private var _state: MutableState<SelectorState> = mutableStateOf(createState(appContext))

    val state: State<SelectorState>
        get() = _state

    fun delayedUpdateOfJustFound() {
        if (state.value.justFoundTreasureId != NOTHING_FOUND_TREASURE_ID) {
            viewModelScope.launch(ioDispatcher) {
                delay(500)
                state.value.justFoundTreasureId = NOTHING_FOUND_TREASURE_ID
            }
        }
    }

    private fun createState(appContext: Context): SelectorState {
        val justFoundTreasureId = stateHandle.get<Int>(PARAMETER_JUST_FOUND_TREASURE)!!
        return SelectorState(
            justFoundTreasureId,
            photoHelper.getCommemorativePhotoTempUri()
        )
    }
}