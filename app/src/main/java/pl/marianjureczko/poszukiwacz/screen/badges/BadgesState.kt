package pl.marianjureczko.poszukiwacz.screen.badges

import pl.marianjureczko.poszukiwacz.usecase.badges.Badge

class BadgesState(
    val golds: Int = 0,
    val diamonds: Int = 0,
    val rubies: Int = 0,
    val knowledge: Int = 0,
    val treasures: Int = 0,
    val completedRoutes: Int = 0,
    val greatestNumberOfTreasuresOnRoute: Int = 0,
    badges: List<Badge> = listOf()
) {
    val badges: List<Badge> = badges.sortedByDescending { it.timestamp }

    fun totalLoot(): Int {
        return golds + diamonds + rubies
    }

    fun showBadges(): Boolean = badges.isNotEmpty()
}