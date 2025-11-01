package pl.marianjureczko.poszukiwacz.shared.port.external

import kotlinx.serialization.Serializable
import pl.marianjureczko.poszukiwacz.usecase.badges.Badge
import pl.marianjureczko.poszukiwacz.usecase.badges.BadgeType

@Serializable
data class BadgeJson(
    val type: BadgeType,
    val level: Int,
    val achievementValue: Int,
    val timestamp: Long,
) {
    companion object {
        fun from(badge: Badge): BadgeJson {
            return BadgeJson(
                type = badge.type,
                level = badge.level,
                achievementValue = badge.achievementValue,
                timestamp = badge.timestamp,
            )
        }
    }

    fun toDomain(): Badge {
        return Badge(
            type = type,
            level = level,
            achievementValue = achievementValue,
            timestamp = timestamp,
        )
    }
}