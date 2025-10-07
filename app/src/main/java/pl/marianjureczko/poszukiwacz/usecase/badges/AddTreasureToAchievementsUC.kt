package pl.marianjureczko.poszukiwacz.usecase.badges

import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.Treasure
import pl.marianjureczko.poszukiwacz.model.TreasureType
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress

class AddTreasureToAchievementsUC(
    private val storage: AchievementsStoragePort
) {
    operator fun invoke(route: Route, treasure: Treasure, currentProgress: TreasuresProgress): Badge? {
        var achievs = storage.load() ?: Achievements()

        when (treasure.type) {
            TreasureType.GOLD -> achievs = achievs.copy(golds = achievs.golds + treasure.quantity)
            TreasureType.RUBY -> achievs = achievs.copy(rubies = achievs.rubies + treasure.quantity)
            TreasureType.DIAMOND -> achievs = achievs.copy(diamonds = achievs.diamonds + treasure.quantity)

            TreasureType.KNOWLEDGE -> {/*no-op*/
            }
        }
        achievs = achievs.copy(treasures = achievs.treasures + 1)
        if (areAllTreasuresFromRouteFound(route, currentProgress)) {
            achievs = achievs.copy(completedRoutes = achievs.completedRoutes + 1)
            if (hasCompletedNewLongestRoute(route, achievs)) {
                achievs = achievs.copy(greatestNumberOfTreasuresOnRoute = route.treasures.size)
            }
        }
        storage.save(achievs)
        return null
    }

    private fun hasCompletedNewLongestRoute(route: Route, achievements: Achievements): Boolean {
        return route.treasures.size > achievements.greatestNumberOfTreasuresOnRoute
    }

    private fun areAllTreasuresFromRouteFound(route: Route, currentProgress: TreasuresProgress): Boolean {
        return currentProgress.collectedQrCodes.size >= route.treasures.size
    }
}