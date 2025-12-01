package pl.marianjureczko.poszukiwacz

import androidx.compose.ui.test.assertTextEquals
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import pl.marianjureczko.poszukiwacz.screen.result.BADGE_GRANTED_HEADER
import pl.marianjureczko.poszukiwacz.screen.searching.SCAN_TREASURE_BUTTON
import pl.marianjureczko.poszukiwacz.shared.di.PortsModule
import pl.marianjureczko.poszukiwacz.ui.components.BADGE_DESCRIPTION
import pl.marianjureczko.poszukiwacz.ui.components.BADGE_ICON
import pl.marianjureczko.poszukiwacz.usecase.badges.GainNewBadgesUC

//Prepared for Pixel 6a API 34
@UninstallModules(PortsModule::class)
@HiltAndroidTest
class SelectorScreenTest : UiTest() {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun shouldShowGrantedBadge_when_aTreasureLeadingToGrantingBadgeIsTaken() {
        // given
        composeRule.waitForIdle()
        goToSearchingScreen(route!!)
        val selectedTreasureDef = route!!.treasures.first()
        TestPortsModule.location.updateLocation(selectedTreasureDef.latitude, selectedTreasureDef.longitude)

        //the gold amount must be above GainNewBadgesUC.JEWELRY_THRESHOLD
        val goldAmount = GainNewBadgesUC.JEWELRY_THRESHOLD + 1
        TestPortsModule.qrScannerPort.setContents("g${goldAmount}001")
        TestPortsModule.qrScannerPort.getContents()

        // when
        performTap(SCAN_TREASURE_BUTTON)
        TestPortsModule.ioDispatcher.scheduler.advanceTimeBy(600)

        // then
        composeRule.waitForIdle()
        getNode(BADGE_GRANTED_HEADER).assertExists()
        getNode(BADGE_ICON).assertExists()
        getNode(BADGE_DESCRIPTION).assertTextEquals("granted for $goldAmount of")
    }
}