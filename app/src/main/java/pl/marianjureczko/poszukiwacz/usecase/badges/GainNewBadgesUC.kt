package pl.marianjureczko.poszukiwacz.usecase.badges


class GainNewBadgesUC {

    companion object {
        // visibility for tests
        const val JEWELRY_THRESHOLD = 50
        private val ALL_JEVELRY_THRESHOLD = 100
        private val TREASURES_THRESHOLD = 10
        private val ROUTES_THRESHOLD = 5
        private val KNOWLEDGE_THRESHOLD = 5

    }

    /**
     * @param achievements all achievements except badges should be already updated
     */
    operator fun invoke(achievements: Achievements): List<Badge> {
        val gained = mutableListOf<Badge>()
        grantBadgesByAddingToGainedBadges(
            gained,
            BadgeType.GoldHunter,
            achievements.golds,
            achievements.badges,
            JEWELRY_THRESHOLD
        )
        grantBadgesByAddingToGainedBadges(
            gained,
            BadgeType.RubyCollector,
            achievements.rubies,
            achievements.badges,
            JEWELRY_THRESHOLD
        )
        grantBadgesByAddingToGainedBadges(
            gained,
            BadgeType.DiamondExplorer,
            achievements.diamonds,
            achievements.badges,
            JEWELRY_THRESHOLD
        )
        grantBadgesByAddingToGainedBadges(
            gained, BadgeType.Treasurer, achievements.allJewelries(), achievements.badges,
            ALL_JEVELRY_THRESHOLD
        )
        grantBadgesByAddingToGainedBadges(
            gained,
            BadgeType.TreasureSeeker,
            achievements.treasures,
            achievements.badges,
            TREASURES_THRESHOLD
        )
        grantBadgesByAddingToGainedBadges(
            gained, BadgeType.EnduringTraveler, achievements.completedRoutes, achievements.badges,
            ROUTES_THRESHOLD
        )
        grantPathfinderBadgeByAddingToGainedBadges(
            gained,
            achievements.greatestNumberOfTreasuresOnRoute,
            achievements.badges
        )
        grantBadgesByAddingToGainedBadges(
            gained,
            BadgeType.KnowledgeHero,
            achievements.knowledge,
            achievements.badges,
            KNOWLEDGE_THRESHOLD
        )
        return gained
    }

    private fun grantPathfinderBadgeByAddingToGainedBadges(
        gained: MutableList<Badge>,
        greatestNumberOfTreasuresOnRoute: Int,
        achievedBadges: List<Badge>,
    ) {
        val possesedBadge = getBestBadgeOfType(BadgeType.Pathfinder, achievedBadges)
        val alreadyRewarded = possesedBadge?.achievementValue ?: 0
        val currentLevel = possesedBadge?.level ?: 0

        if (greatestNumberOfTreasuresOnRoute > alreadyRewarded) {
            gained.add(Badge(BadgeType.Pathfinder, currentLevel + 1, greatestNumberOfTreasuresOnRoute))
        }
    }

    private fun grantBadgesByAddingToGainedBadges(
        gainedBadges: MutableList<Badge>,
        type: BadgeType,
        collectedQuantity: Int,
        achievedBadges: List<Badge>,
        threshold: Int
    ) {
        val badgeLevel = getBestBadgeOfType(type, achievedBadges)?.level ?: 0
        val goldBadgeExpectedLevel = collectedQuantity / threshold
        if (goldBadgeExpectedLevel > badgeLevel) {
            gainedBadges.add(Badge(type, badgeLevel + 1, collectedQuantity))
        }
    }

    private fun getBestBadgeOfType(type: BadgeType, badges: List<Badge>): Badge? {
        return badges
            .asSequence()
            .filter { it.type == type }
            .maxByOrNull { it.level }
    }
}