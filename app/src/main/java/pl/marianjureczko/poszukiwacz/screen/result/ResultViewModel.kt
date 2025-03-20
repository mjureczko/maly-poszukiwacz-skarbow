package pl.marianjureczko.poszukiwacz.screen.result

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.marianjureczko.poszukiwacz.screen.Screens
import pl.marianjureczko.poszukiwacz.shared.port.StorageHelper
import java.util.Locale
import javax.inject.Inject

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
        val treasureDescId = stateHandle.get<Int>(Screens.Results.PARAMETER_TREASURE_ID) ?: NOTHING_FOUND_TREASURE_ID
        if (treasureDescriptionHasBeenIdentified(treasureDescId)) {
            stateHandle.get<String>(Screens.Results.PARAMETER_ROUTE_NAME)?.let { routeName ->
                val treasureDescription = storageHelper.loadRoute(routeName).treasures
                    .find { it.id == treasureDescId }
                moviePath = treasureDescription?.movieFileName
                subtitlesPath = treasureDescription?.subtitlesFileName
            }
        }
        val localesWithSubtitles = !"pl".equals(Locale.getDefault().language, true)
        val resultType = stateHandle.get<ResultType>(Screens.Results.PARAMETER_RESULT_TYPE) ?: ResultType.NOT_A_TREASURE
        val treasureType = resultType.toTreasureType()
        val amount = stateHandle.get<Int>(Screens.Results.PARAMETER_TREASURE_AMOUNT) ?: 0
        return ResultState(resultType, treasureType, amount, moviePath, null, subtitlesPath, localesWithSubtitles)
    }

    /**
     * It's possible to find a treasure whose TreasureDescription cannot be identified (for knowledge treasures it's
     * always possible to identify the TreasureDescription), check JustFoundTreasureDescriptionFinder for details.
     */
    private fun treasureDescriptionHasBeenIdentified(treasureDescId: Int) = NOTHING_FOUND_TREASURE_ID != treasureDescId

}