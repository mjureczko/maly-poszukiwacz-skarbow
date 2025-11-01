package pl.marianjureczko.poszukiwacz.shared.port.external

import kotlinx.serialization.Serializable
import pl.marianjureczko.poszukiwacz.usecase.badges.Achievements

@Serializable
data class AchievementsJson(
    val golds: Int,
    val diamonds: Int,
    val rubies: Int,
    val knowledge: Int,
    val treasures: Int,
    val completedRoutes: Int,
    val greatestNumberOfTreasuresOnRoute: Int,
    val badges: List<BadgeJson>?,
) {
    companion object {
        fun from(achievements: Achievements): AchievementsJson {
            return AchievementsJson(
                golds = achievements.golds,
                diamonds = achievements.diamonds,
                rubies = achievements.rubies,
                knowledge = achievements.knowledge,
                treasures = achievements.treasures,
                completedRoutes = achievements.completedRoutes,
                greatestNumberOfTreasuresOnRoute = achievements.greatestNumberOfTreasuresOnRoute,
                badges = achievements.badges.map { BadgeJson.from(it) },
            )
        }
    }

    fun toDomain(): Achievements {
        return Achievements(
            golds = golds,
            diamonds = diamonds,
            rubies = rubies,
            knowledge = knowledge,
            treasures = treasures,
            completedRoutes = completedRoutes,
            greatestNumberOfTreasuresOnRoute = greatestNumberOfTreasuresOnRoute,
            badges = badges?.map { it.toDomain() } ?: emptyList(),
        )
    }
}