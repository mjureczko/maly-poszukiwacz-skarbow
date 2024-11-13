package pl.marianjureczko.poszukiwacz.activity.result.n

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.marianjureczko.poszukiwacz.screen.main.CustomInitializerForRoute
import pl.marianjureczko.poszukiwacz.shared.port.StorageHelper
import java.util.Locale
import javax.inject.Inject

const val PARAMETER_RESULT_TYPE = "result_type"
const val PARAMETER_TREASURE_ID = "treasure_id"
const val NOTHING_FOUND_TREASURE_ID = -1

interface MovieController {
    fun onPlay()
    fun onPause()
    fun onMovieFinished()
}

@HiltViewModel
class ResultViewModel @Inject constructor(
    private val stateHandle: SavedStateHandle,
    private val storageHelper: StorageHelper
) : ViewModel(), MovieController {

    private var _state: MutableState<ResultState> = mutableStateOf(createState())

    val state: State<ResultState>
        get() = _state

    override fun onPlay() {
        _state.value = _state.value.copy(isPlayVisible = false)
    }

    override fun onPause() {
        _state.value = _state.value.copy(isPlayVisible = true)
    }

    override fun onMovieFinished() {
        _state.value = _state.value.copy(isPlayVisible = true)
    }

    fun setSubtitlesLine(line: String?) {
        _state.value = _state.value.copy(subtitlesLine = line)
    }

    private fun createState(): ResultState {
        var moviePath: String? = null
        var subtitlesPath: String? = null
        //TODO: conditional for custom version
        val treasureDescId = stateHandle.get<Int>(PARAMETER_TREASURE_ID) ?: NOTHING_FOUND_TREASURE_ID
        if (NOTHING_FOUND_TREASURE_ID != treasureDescId) {
            val treasureDescription = storageHelper.loadRoute(CustomInitializerForRoute.routeName).treasures
                .find { it.id == treasureDescId }
            moviePath = treasureDescription?.movieFileName
            subtitlesPath = treasureDescription?.subtitlesFileName
        }
        val localesWithSubtitles = !"pl".equals(Locale.getDefault().language, true)
        val resultType = stateHandle.get<ResultType>(PARAMETER_RESULT_TYPE) ?: ResultType.NOT_A_TREASURE
        return ResultState(resultType, null, null, moviePath, null, subtitlesPath, localesWithSubtitles)
    }

}