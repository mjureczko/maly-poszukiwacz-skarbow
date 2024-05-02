package pl.marianjureczko.poszukiwacz.activity.result.n

import pl.marianjureczko.poszukiwacz.model.TreasureType

data class ResultState(
    val resultType: ResultType,
    val treasureType: TreasureType?,
    val amount: Int?,
    val moviePath: String?,
    val subtitlesLine: String?,
    val subtitlesPath: String?,
    val localesWithSubtitles: Boolean = false,
    val isPlayVisible: Boolean = true
)

enum class ResultType {
    NOT_A_TREASURE,
    ALREADY_TAKEN,
    TREASURE,
}