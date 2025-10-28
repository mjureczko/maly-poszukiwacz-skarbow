package pl.marianjureczko.poszukiwacz.screen.result

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
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.Treasure
import pl.marianjureczko.poszukiwacz.model.TreasureType
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
import pl.marianjureczko.poszukiwacz.screen.Screens
import pl.marianjureczko.poszukiwacz.shared.di.IoDispatcher
import pl.marianjureczko.poszukiwacz.shared.port.storage.StoragePort
import pl.marianjureczko.poszukiwacz.usecase.badges.AddTreasureToAchievementsUC
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
    private val storagePort: StoragePort,
    private val addTreasureToAchievementsUC: AddTreasureToAchievementsUC,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel(), MovieController {

    private val realTreasuresTypes =
        setOf(TreasureType.KNOWLEDGE, TreasureType.GOLD, TreasureType.RUBY, TreasureType.DIAMOND)

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
        var route: Route? = null
        var progress: TreasuresProgress? = null
        val treasureDescId = stateHandle.get<Int>(Screens.Results.PARAMETER_TREASURE_ID) ?: NOTHING_FOUND_TREASURE_ID
        val routeName = stateHandle.get<String>(Screens.Results.PARAMETER_ROUTE_NAME)
        if (treasureDescriptionHasBeenIdentified(treasureDescId)) {
            routeName?.let { name ->
                route = storagePort.loadRoute(name)
                val treasureDescription = route!!.treasures.find { it.id == treasureDescId }
                moviePath = treasureDescription?.movieFileName
                subtitlesPath = treasureDescription?.subtitlesFileName
            }
        }
        val localesWithSubtitles = !"pl".equals(Locale.getDefault().language, true)
        val resultType = stateHandle.get<ResultType>(Screens.Results.PARAMETER_RESULT_TYPE) ?: ResultType.NOT_A_TREASURE
        val treasureType = resultType.toTreasureType()
        val amount = stateHandle.get<Int>(Screens.Results.PARAMETER_TREASURE_AMOUNT) ?: 0
        stateHandle.get<Boolean>(Screens.Results.PARAMETER_IS_JUST_FOUND)?.let { isJustFound ->
            if (isJustFound && realTreasuresTypes.contains(treasureType)) {
                if (route == null) {
                    route = storagePort.loadRoute(routeName!!)
                }
                progress = storagePort.loadProgress(routeName!!)
                delayedAchievementsProcessing()
            }
        }
        return ResultState(
            resultType = resultType,
            treasureType = treasureType,
            amount = amount,
            moviePath = moviePath,
            subtitlesLine = null,
            subtitlesPath = subtitlesPath,
            route = route,
            progress = progress,
            localesWithSubtitles = localesWithSubtitles,
        )
    }

    /**
     * It's possible to find a treasure whose TreasureDescription cannot be identified (for knowledge treasures it's
     * always possible to identify the TreasureDescription), check JustFoundTreasureDescriptionFinder for details.
     */
    private fun treasureDescriptionHasBeenIdentified(treasureDescId: Int) = NOTHING_FOUND_TREASURE_ID != treasureDescId

    private fun delayedAchievementsProcessing() {
        viewModelScope.launch(ioDispatcher) {
            val badges = addTreasureToAchievementsUC(
                route = state.value.route!!,
                treasure = Treasure("_", state.value.amount!!, state.value.treasureType!!),
                currentProgress = state.value.progress!!,
            )
            if (badges.isNotEmpty()) {
                delay(400)
                _state.value = _state.value.copy(
                    badgesToShow = badges,
                    isBadgesVisible = true,
                )
            }
        }
    }
}