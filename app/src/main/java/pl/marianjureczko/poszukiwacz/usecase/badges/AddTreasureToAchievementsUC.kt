package pl.marianjureczko.poszukiwacz.usecase.badges

import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.Treasure
import pl.marianjureczko.poszukiwacz.model.TreasureType
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress

class AddTreasureToAchievementsUC(
    private val storage: AchievementsStoragePort
) {
    /**
     * @currentProgress - progress AFTER adding the treasure from 2nd parameter
     */
    operator fun invoke(route: Route, treasure: Treasure, currentProgress: TreasuresProgress): List<Badge> {
        var achievements = storage.load() ?: Achievements()

        when (treasure.type) {
            TreasureType.GOLD -> achievements = achievements.copy(golds = achievements.golds + treasure.quantity)
            TreasureType.RUBY -> achievements = achievements.copy(rubies = achievements.rubies + treasure.quantity)
            TreasureType.DIAMOND -> achievements =
                achievements.copy(diamonds = achievements.diamonds + treasure.quantity)

            TreasureType.KNOWLEDGE -> {
                require(treasure.quantity == 1) { "Knowledge treasure quantity must be 1" }
                achievements = achievements.copy(knowledge = achievements.knowledge + 1)
            }
        }
        achievements = achievements.copy(treasures = achievements.treasures + 1)
        if (areAllTreasuresFromRouteFound(route, currentProgress)) {
            achievements = achievements.copy(completedRoutes = achievements.completedRoutes + 1)
            if (hasCompletedNewLongestRoute(route, achievements)) {
                achievements = achievements.copy(greatestNumberOfTreasuresOnRoute = route.treasures.size)
            }
        }
        storage.save(achievements)
        return listOf()
    }

    private fun hasCompletedNewLongestRoute(route: Route, achievements: Achievements): Boolean {
        return route.treasures.size > achievements.greatestNumberOfTreasuresOnRoute
    }

    private fun areAllTreasuresFromRouteFound(route: Route, currentProgress: TreasuresProgress): Boolean {
        return currentProgress.collectedQrCodes.size >= route.treasures.size
    }
}