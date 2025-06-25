package pl.marianjureczko.poszukiwacz

import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Test
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
import pl.marianjureczko.poszukiwacz.screen.result.PLAY_MOVIE_BUTTON
import pl.marianjureczko.poszukiwacz.screen.treasureselector.SHOW_MOVIE_BUTTON
import pl.marianjureczko.poszukiwacz.shared.di.PortsModule

@UninstallModules(PortsModule::class)
@HiltAndroidTest
class SelectorScreenTest : UiTest() {

    @Test
    fun shouldShowMovie() {
        //given
        composeRule.waitForIdle()
        TestPortsModule.ioDispatcher.scheduler.runCurrent()
        waitForInitialization()
        val route = getRouteFromStorage()
        val progress = TreasuresProgress().copy(
            routeName = route.name,
            collectedTreasuresDescriptionId = mutableSetOf(route.treasures[0].id)
        )
        injectableStorage.save(progress)
        goToSearching()
        goToSelector()

        //when
        performTap(SHOW_MOVIE_BUTTON)
        composeRule.waitForIdle()

        //then
        composeRule.waitForIdle()
        getNode(PLAY_MOVIE_BUTTON).assertExists("Button to play the movie should be displayed")
    }
}