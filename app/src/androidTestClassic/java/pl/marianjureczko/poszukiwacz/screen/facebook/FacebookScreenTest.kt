package pl.marianjureczko.poszukiwacz.screen.facebook

import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import pl.marianjureczko.poszukiwacz.TestPortsModule
import pl.marianjureczko.poszukiwacz.UiTest
import pl.marianjureczko.poszukiwacz.shared.di.PortsModule
import pl.marianjureczko.poszukiwacz.shared.port.TestExternalStoragePort
import pl.marianjureczko.poszukiwacz.ui.components.TOPBAR_MENU_BUTTON
import pl.marianjureczko.poszukiwacz.ui.components.TOPBAR_MENU_GALLERY
import javax.inject.Inject


//Prepared for Pixel 6a API 34
@UninstallModules(PortsModule::class)
@HiltAndroidTest
class FacebookScreenTest : UiTest() {

    @Inject
    lateinit var reportStoragePort: ReportStoragePort

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun shouldExportReportToAnImageInGallery() {
        //given
        composeRule.waitForIdle()
        TestPortsModule.ioDispatcher.scheduler.runCurrent()
        goToSearchingScreen(route!!)
        performTap(TOPBAR_MENU_BUTTON)
        performTap(TOPBAR_MENU_GALLERY)
        composeRule.waitForIdle()
        composeRule.onNodeWithContentDescription(FACEBOOK_SCREEN_BODY).assertExists()

        //when
        // map generation is async and not ready yet when assertions are executed
        composeRule.onNodeWithContentDescription(INCLUDE_MAP)
            .assertExists()
            .performClick()
        composeRule.onNodeWithContentDescription(FACEBOOK_SHARE_BUTTON)
            .assertExists()
            .performClick()
        composeRule.waitForIdle()

        //then
        val testExternalStoragePort = reportStoragePort as TestExternalStoragePort
        assertThat(testExternalStoragePort.savedBitmap).isNotNull
        assertThat(testExternalStoragePort.savedFileName).isEqualTo(route!!.name)
    }
}