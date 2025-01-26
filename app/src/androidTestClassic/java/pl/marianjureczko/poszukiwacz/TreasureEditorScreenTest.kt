package pl.marianjureczko.poszukiwacz

import androidx.compose.ui.test.assertTextEquals
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Assert.assertEquals
import org.junit.Test
import pl.marianjureczko.poszukiwacz.screen.treasureseditor.ADD_TREASURE_BUTTON
import pl.marianjureczko.poszukiwacz.screen.treasureseditor.DO_PHOTO_TIP_BUTTON
import pl.marianjureczko.poszukiwacz.screen.treasureseditor.RECORD_SOUND_TIP_BUTTON
import pl.marianjureczko.poszukiwacz.screen.treasureseditor.STOP_RECORDING_BUTTON
import pl.marianjureczko.poszukiwacz.screen.treasureseditor.TREASURE_ITEM_TEXT
import pl.marianjureczko.poszukiwacz.shared.di.PortsModule
import pl.marianjureczko.poszukiwacz.ui.components.NO_BUTTON
import pl.marianjureczko.poszukiwacz.ui.components.YES_BUTTON

//Prepared for Pixel 6a API 34
@UninstallModules(PortsModule::class)
@HiltAndroidTest
class TreasureEditorScreenTest : UiTest() {

    @Test
    fun shouldShowAllTreasuresFromRoute_whenEditingTheRoute() {
        //given
        composeRule.waitForIdle()

        //when
        goToTreasuresEditorScreen(route!!.name)

        //then
        route!!.treasures.forEach { td ->
            getNode("$TREASURE_ITEM_TEXT ${td.id}")
                .assertTextEquals("[${td.id}] ${td.latitude} ${td.longitude}")
        }
    }

    @Test
    fun shouldAddTreasure_whenTapOnPlus() {
        //given
        val newTreasureId = route!!.nextId()
        goToTreasuresEditorScreen(route!!.name)
        val lat = 1.0
        val lng = 2.0
        TestPortsModule.location.updateLocation(lat, lng)
        composeRule.waitForIdle()

        //when
        performTap(ADD_TREASURE_BUTTON)

        //then
        val treasureNode = getNode("$TREASURE_ITEM_TEXT $newTreasureId")
        treasureNode.assertTextEquals("[$newTreasureId] $lat $lng")

        val newTreasure = getRouteFromStorage().treasures.find { it.id == newTreasureId }!!
        assertEquals(lat, newTreasure.latitude, 0.001)
        assertEquals(lng, newTreasure.longitude, 0.001)
        assertEquals(null, newTreasure.tipFileName)
        assertEquals(null, newTreasure.photoFileName)
    }

    @Test
    fun shouldRecordAudioTip_whenTapOnMicrophone() {
        //given
        composeRule.waitForIdle()
        goToTreasuresEditorScreen(route!!.name)

        //when
        performTap("$RECORD_SOUND_TIP_BUTTON ${route!!.treasures[0].id}")

        //then
        Thread.sleep(100)
        performTap(STOP_RECORDING_BUTTON)
        BuildVariantSpecificTestPortsModule.storage.fileNotEmpty = true
        performTap("$RECORD_SOUND_TIP_BUTTON ${route!!.treasures[0].id}")
        getNode(YES_BUTTON).assertExists()
        performTap(NO_BUTTON)
        getNode(YES_BUTTON).assertDoesNotExist()
    }

    @Test
    fun shouldSavePhotoTip_whenTapOnCamera() {
        //given
        BuildVariantSpecificTestPortsModule.storage.fileNotEmpty = false
        composeRule.waitForIdle()
        goToTreasuresEditorScreen(route!!.name)
        TestPortsModule.camera.counter = 0

        //when
        performTap("$DO_PHOTO_TIP_BUTTON ${route!!.treasures[0].id}")

        //then
        BuildVariantSpecificTestPortsModule.storage.fileNotEmpty = true
        assertEquals(1, TestPortsModule.camera.counter)
        composeRule.waitForIdle()
        performTap("$DO_PHOTO_TIP_BUTTON ${route!!.treasures[0].id}")
        getNode(YES_BUTTON).assertExists()
        performTap(NO_BUTTON)
        getNode(YES_BUTTON).assertDoesNotExist()
    }
}