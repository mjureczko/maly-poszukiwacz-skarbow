package pl.marianjureczko.poszukiwacz

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertTextEquals
import androidx.test.espresso.Espresso.pressBack
import com.ocadotechnology.gembus.test.somePositiveInt
import com.ocadotechnology.gembus.test.someString
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import pl.marianjureczko.poszukiwacz.screen.result.DO_NOT_SHOW_TREASURE_MSG
import pl.marianjureczko.poszukiwacz.screen.result.PLAY_MOVIE_BUTTON
import pl.marianjureczko.poszukiwacz.screen.searching.LocationCalculator
import pl.marianjureczko.poszukiwacz.screen.searching.SCAN_TREASURE_BUTTON
import pl.marianjureczko.poszukiwacz.screen.searching.STEPS_TO_TREASURE
import pl.marianjureczko.poszukiwacz.screen.treasureselector.TREASURE_COLLECTED_CHECKBOX
import pl.marianjureczko.poszukiwacz.shared.di.PortsModule

@UninstallModules(PortsModule::class)
@HiltAndroidTest
class SearchingScreenTest : UiTest() {

    @Test
    fun shouldUpdateNavigationWidgets_whenLocationIsUpdated() {
        //given
        composeRule.waitForIdle()
        TestPortsModule.ioDispatcher.scheduler.runCurrent()
        goToSearching()
        val distanceToTreasure = somePositiveInt(999)

        //when
        TestPortsModule.location.updateLocation(
            route!!.treasures[0].latitude + 0.01,
            route!!.treasures[0].longitude + 0.01,
            distanceToTreasure = distanceToTreasure.toFloat()
        )

        //then
        composeRule.waitForIdle()
        val stepsToTreasure: SemanticsNodeInteraction = getNode(STEPS_TO_TREASURE)
        val expected = (distanceToTreasure * LocationCalculator.METERS_TOS_STEP_FACTOR).toInt()
        stepsToTreasure.assertTextEquals(expected.toString())
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun shouldMarkCollectedTreasureAndPromptForNewOne() {
        //given
        composeRule.waitForIdle()
        TestPortsModule.ioDispatcher.scheduler.runCurrent()
        goToSearching()
        val selectedTreasureDef = route!!.treasures.first()
        TestPortsModule.location.updateLocation(selectedTreasureDef.latitude, selectedTreasureDef.longitude)
        TestPortsModule.qrScannerPort.setContents(selectedTreasureDef.qrCode!!)

        //when
        performTap(SCAN_TREASURE_BUTTON)

        //then
        composeRule.waitForIdle()
        getNode(PLAY_MOVIE_BUTTON).assertExists("Button to play the movie should be displayed")

        //when
        TestPortsModule.ioDispatcher.scheduler.advanceTimeBy(600)
        pressBack()
        composeRule.waitForIdle()
        TestPortsModule.ioDispatcher.scheduler.advanceTimeBy(600)

        //then
        composeRule.waitForIdle()
        getNode(TREASURE_COLLECTED_CHECKBOX).assertExists()
    }

    @Test
    fun shouldErrorNotTreasure_whenScanningRandomQrCode() {
        //given
        composeRule.waitForIdle()
        TestPortsModule.ioDispatcher.scheduler.runCurrent()
        goToSearching()
        TestPortsModule.qrScannerPort.setContents(someString())

        //when
        performTap(SCAN_TREASURE_BUTTON)

        //then
        composeRule.waitForIdle()
        getNode(DO_NOT_SHOW_TREASURE_MSG).assertExists()
    }
}