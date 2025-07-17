package pl.marianjureczko.poszukiwacz.screen.searching

import androidx.compose.ui.test.assertTextEquals
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Assert.assertEquals
import org.junit.Test
import pl.marianjureczko.poszukiwacz.TestPortsModule
import pl.marianjureczko.poszukiwacz.UiTest
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
import pl.marianjureczko.poszukiwacz.shared.di.PortsModule
import pl.marianjureczko.poszukiwacz.ui.components.YES_BUTTON

@UninstallModules(PortsModule::class)
@HiltAndroidTest
class RestartTest : UiTest() {
    @Test
    fun shouldRestartProgress_whenRestartingFromMenu() {
        //given
        composeRule.waitForIdle()
        TestPortsModule.ioDispatcher.scheduler.runCurrent()
        waitForInitialization()
        val route = getRouteFromStorage()
        val progress = TreasuresProgress().copy(
            routeName = route.name,
            collectedTreasuresDescriptionId = mutableSetOf(route.treasures[0].id),
            knowledge = 1
        )
        injectableStorage.save(progress)
        goToSearching()

        //when
        openTopBarMenu()
        clickRestartMenuEntry()
        performTap(YES_BUTTON)

        //then
        val actualProgress = injectableStorage.loadProgress(route.name)!!
        assertEquals(mutableSetOf<Int>(), actualProgress.collectedTreasuresDescriptionId)
        assertEquals(0, actualProgress.golds)
        val displayedScore = getNode(KNOWLEDGE_SCORE_TEXT)
        displayedScore.assertTextEquals("0")
    }
}