package pl.marianjureczko.poszukiwacz.usecase.badges


class GainNewBadgesUC {

    val JEWELRY_THRESHOLD = 50

    /**
     * @param achievements all achievements except badges should be already updated
     */
    fun invoke(achievements: Achievements): List<Badge> {
        val gainedBadges = mutableListOf<Badge>()
        val goldBadgeLevel = getBestBadgeOfType(BadgeType.GoldHunter, achievements.badges)?.level ?: 0
        val goldBadgeExpectedLevel = achievements.golds / JEWELRY_THRESHOLD
        if (goldBadgeExpectedLevel > goldBadgeLevel) {
            gainedBadges.add(Badge(BadgeType.GoldHunter, goldBadgeLevel + 1, achievements.golds))
        }
        return gainedBadges
    }

    private fun getBestBadgeOfType(type: BadgeType, badges: List<Badge>): Badge? {
        return badges
            .asSequence()
            .filter { it.type == type }
            .maxByOrNull { it.level }
    }
}