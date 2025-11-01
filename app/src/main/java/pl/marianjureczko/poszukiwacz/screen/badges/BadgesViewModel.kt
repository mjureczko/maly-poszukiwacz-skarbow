package pl.marianjureczko.poszukiwacz.screen.badges

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.marianjureczko.poszukiwacz.usecase.badges.AchievementsStoragePort
import pl.marianjureczko.poszukiwacz.usecase.badges.Badge
import pl.marianjureczko.poszukiwacz.usecase.badges.BadgeType
import javax.inject.Inject

@HiltViewModel
class BadgesViewModel @Inject constructor(
    val achievementsStoragePort: AchievementsStoragePort
) : ViewModel() {

    private var _state: MutableState<BadgesState> = mutableStateOf(createState())

    val state: State<BadgesState>
        get() = _state

    private fun createState(): BadgesState {
        val achievements = achievementsStoragePort.load()
        return BadgesState(
            golds = achievements?.golds ?: 0,
            diamonds = achievements?.diamonds ?: 0,
            rubies = achievements?.rubies ?: 0,
            knowledge = achievements?.knowledge ?: 0,
            treasures = achievements?.treasures ?: 0,
            completedRoutes = achievements?.completedRoutes ?: 0,
            greatestNumberOfTreasuresOnRoute = achievements?.greatestNumberOfTreasuresOnRoute ?: 0,
            badges = achievements?.badges ?: emptyList()
        )
    }

    private fun testData() = BadgesState(
        golds = 10,
        diamonds = 20,
        rubies = 330,
        knowledge = 40,
        treasures = 90,
        completedRoutes = 10,
        greatestNumberOfTreasuresOnRoute = 110,
        badges = listOf(
            Badge(type = BadgeType.GoldHunter, level = 1, achievementValue = 10),
            Badge(type = BadgeType.RubyCollector, level = 1, achievementValue = 62),
            Badge(type = BadgeType.RubyCollector, level = 2, achievementValue = 102),
            Badge(type = BadgeType.RubyCollector, level = 3, achievementValue = 262),
            Badge(type = BadgeType.DiamondExplorer, level = 1, achievementValue = 19),
            Badge(type = BadgeType.KnowledgeHero, level = 1, achievementValue = 5),
            Badge(type = BadgeType.KnowledgeHero, level = 2, achievementValue = 33),
            Badge(type = BadgeType.Treasurer, level = 1, achievementValue = 57),
            Badge(type = BadgeType.Treasurer, level = 2, achievementValue = 127),
            Badge(type = BadgeType.Treasurer, level = 3, achievementValue = 207),
            Badge(type = BadgeType.Treasurer, level = 4, achievementValue = 330),
            Badge(type = BadgeType.TreasureSeeker, level = 1, achievementValue = 10),
            Badge(type = BadgeType.EnduringTraveler, level = 1, achievementValue = 5),
            Badge(type = BadgeType.Pathfinder, level = 1, achievementValue = 9),
        )
    )
}