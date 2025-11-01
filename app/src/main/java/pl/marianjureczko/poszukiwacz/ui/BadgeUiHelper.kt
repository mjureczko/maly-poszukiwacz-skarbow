package pl.marianjureczko.poszukiwacz.ui

import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.usecase.badges.Badge
import pl.marianjureczko.poszukiwacz.usecase.badges.BadgeType

object BadgeUiHelper {
    fun badgeImage(badge: Badge): Int =
        when (badge.type) {
            BadgeType.GoldHunter -> R.drawable.badge_gold
            BadgeType.DiamondExplorer -> R.drawable.badge_diamond
            BadgeType.RubyCollector -> R.drawable.badge_ruby
            BadgeType.KnowledgeHero -> R.drawable.badge_knowledge
            BadgeType.Treasurer -> R.drawable.badge_treasurer
            BadgeType.TreasureSeeker -> R.drawable.badge_seeker
            BadgeType.EnduringTraveler -> R.drawable.badge_traveler
            BadgeType.Pathfinder -> R.drawable.badge_pathfinder
        }

    fun badgeUnitImage(badge: Badge): Int? =
        when (badge.type) {
            BadgeType.GoldHunter -> R.drawable.gold
            BadgeType.DiamondExplorer -> R.drawable.diamond
            BadgeType.RubyCollector -> R.drawable.ruby
            BadgeType.KnowledgeHero -> R.drawable.chest_small
            BadgeType.Treasurer, BadgeType.TreasureSeeker, BadgeType.EnduringTraveler, BadgeType.Pathfinder -> null
        }

    fun badgeUnitText(badge: Badge): Int =
        when (badge.type) {
            BadgeType.GoldHunter, BadgeType.DiamondExplorer, BadgeType.RubyCollector, BadgeType.KnowledgeHero -> R.string.badge_unit_text_base
            BadgeType.Treasurer -> R.string.badge_of_jewelries
            BadgeType.TreasureSeeker -> R.string.badge_of_treasures
            BadgeType.EnduringTraveler -> R.string.badge_of_completed_routes
            BadgeType.Pathfinder -> R.string.badge_of_longest_route
        }
}