package pl.marianjureczko.poszukiwacz

import androidx.compose.ui.test.assertTextEquals
import androidx.test.espresso.Espresso.pressBack
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import pl.marianjureczko.poszukiwacz.model.Treasure
import pl.marianjureczko.poszukiwacz.model.TreasureParser
import pl.marianjureczko.poszukiwacz.screen.result.TREASURE_QUANTITY
import pl.marianjureczko.poszukiwacz.screen.searching.SCAN_TREASURE_BUTTON
import pl.marianjureczko.poszukiwacz.screen.treasureselector.TREASURE_COLLECTED_CHECKBOX
import pl.marianjureczko.poszukiwacz.shared.di.PortsModule

//Prepared for Pixel 6a API 34
@UninstallModules(PortsModule::class)
@HiltAndroidTest
class SearchingScreenTest : UiTest() {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun shouldMarkCollectedTreasureAndPromptForNewOne_whenCollectingTreasureCloseToTheSelectedOne() {
        //given
        composeRule.waitForIdle()
        goToSearchingScreen(route!!)
        val selectedTreasureDef = route!!.treasures.first()
        TestPortsModule.location.updateLocation(selectedTreasureDef.latitude, selectedTreasureDef.longitude)
        TestPortsModule.qrScannerPort.setContents("g01001")
        val treasure: Treasure = TreasureParser().parse(TestPortsModule.qrScannerPort.getContents())

        //when
        performTap(SCAN_TREASURE_BUTTON)

        //then
        getNode(TREASURE_QUANTITY).assertTextEquals(treasure.quantity.toString())
        assertImageIsDisplayed(treasure.type.image())

        //when
        TestPortsModule.ioDispatcher.scheduler.advanceTimeBy(600)
        pressBack()
        TestPortsModule.ioDispatcher.scheduler.advanceTimeBy(600)

        //then
        composeRule.waitForIdle()
        getNode(TREASURE_COLLECTED_CHECKBOX).assertExists()
    }
}