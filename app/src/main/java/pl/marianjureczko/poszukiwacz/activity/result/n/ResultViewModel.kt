package pl.marianjureczko.poszukiwacz.activity.result.n

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

const val PARAMETER_RESULT_TYPE = "result_type"

@HiltViewModel
class ResultViewModel @Inject constructor(
    private val stateHandle: SavedStateHandle
) : ViewModel() {

    private var _state: MutableState<ResultState> = mutableStateOf(createState())

    val state: State<ResultState>
        get() = _state

    private fun createState(): ResultState {
        val resultType = stateHandle.get<ResultType>(PARAMETER_RESULT_TYPE) ?: ResultType.NOT_A_TREASURE
        return ResultState(resultType, null, null, null)
    }
}