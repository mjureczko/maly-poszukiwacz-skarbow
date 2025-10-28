package pl.marianjureczko.poszukiwacz.screen.result

import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureType
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
import pl.marianjureczko.poszukiwacz.usecase.badges.Badge

data class ResultState(
    val resultType: ResultType,
    val treasureType: TreasureType?,
    val amount: Int?,
    val moviePath: String?,
    val subtitlesLine: String?,
    val subtitlesPath: String?,
    val route: Route?,
    val progress: TreasuresProgress?,
    val localesWithSubtitles: Boolean = false,
    val badgesToShow: List<Badge> = listOf(),
    val isPlayVisible: Boolean = true,
    val isBadgesVisible: Boolean = false,
)

enum class ResultType {
    NOT_A_TREASURE,
    ALREADY_TAKEN,
    KNOWLEDGE,
    GOLD,
    RUBY,
    DIAMOND;

    companion object {
        fun from(treasureType: TreasureType): ResultType =
            when (treasureType) {
                TreasureType.GOLD -> GOLD
                TreasureType.RUBY -> RUBY
                TreasureType.DIAMOND -> DIAMOND
                TreasureType.KNOWLEDGE -> KNOWLEDGE
            }
    }

    fun toTreasureType(): TreasureType? =
        when (this) {
            GOLD -> TreasureType.GOLD
            RUBY -> TreasureType.RUBY
            DIAMOND -> TreasureType.DIAMOND
            KNOWLEDGE -> TreasureType.KNOWLEDGE
            else -> null
        }
}