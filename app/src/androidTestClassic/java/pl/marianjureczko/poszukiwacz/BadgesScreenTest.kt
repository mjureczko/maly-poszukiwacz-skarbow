package pl.marianjureczko.poszukiwacz

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onAllNodesWithContentDescription
import com.ocadotechnology.gembus.test.some
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import junit.framework.TestCase.assertEquals
import org.junit.Test
import pl.marianjureczko.poszukiwacz.screen.badges.COMPLETED_ROUTES_ACHIEVEMENT
import pl.marianjureczko.poszukiwacz.screen.badges.DISCOVERED_TREASURES_ACHIEVEMENT
import pl.marianjureczko.poszukiwacz.screen.badges.LONGEST_COMPLETED_ROUTE_ACHIEVEMENT
import pl.marianjureczko.poszukiwacz.screen.badges.TOTAL_LOOT_ACHIEVEMENT
import pl.marianjureczko.poszukiwacz.shared.di.PortsModule
import pl.marianjureczko.poszukiwacz.ui.components.BADGE_ICON
import pl.marianjureczko.poszukiwacz.usecase.badges.Achievements

//Prepared for Pixel 6a API 34
@UninstallModules(PortsModule::class)
@HiltAndroidTest
class BadgesScreenTest : UiTest() {
    @Test
    fun shouldShowScreenWithBadges_when_badgesWereAchieved() {
        // given
        val achievements = some<Achievements>()
        TestPortsModule.achievementsStoragePort.save(achievements)
        composeRule.waitForIdle()

        // when
        goToAchievementsScreen()

        // then
        composeRule.waitForIdle()
        val expectedTotalLoot = "${context.getString(R.string.total_loot_collected)} ${achievements.allJewelries()}"
        getNode(TOTAL_LOOT_ACHIEVEMENT).assertTextEquals(expectedTotalLoot)
        val expectedDiscoveredTreasures =
            "${context.getString(R.string.discovered_treasures)} ${achievements.treasures}"
        getNode(DISCOVERED_TREASURES_ACHIEVEMENT).assertTextEquals(expectedDiscoveredTreasures)
        val expectedCompletedRoutes = "${context.getString(R.string.completed_routes)} ${achievements.completedRoutes}"
        getNode(COMPLETED_ROUTES_ACHIEVEMENT).assertTextEquals(expectedCompletedRoutes)
        val expectedLongestCompletedRoute =
            "${context.getString(R.string.longest_completed_route)} ${achievements.greatestNumberOfTreasuresOnRoute}"
        getNode(LONGEST_COMPLETED_ROUTE_ACHIEVEMENT).assertTextEquals(expectedLongestCompletedRoute)

        val numberOfPresentedBadges =
            composeRule.onAllNodesWithContentDescription(BADGE_ICON).fetchSemanticsNodes().size
        assertEquals(achievements.badges.size, numberOfPresentedBadges)
    }
}