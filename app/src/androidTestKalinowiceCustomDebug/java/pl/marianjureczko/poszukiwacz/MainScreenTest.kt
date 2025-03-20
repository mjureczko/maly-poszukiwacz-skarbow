package pl.marianjureczko.poszukiwacz

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.After
import org.junit.Before
import org.junit.Test
import pl.marianjureczko.poszukiwacz.screen.main.GUIDE_TEXT
import pl.marianjureczko.poszukiwacz.screen.main.NEXT_GUIDE_BUTTON
import pl.marianjureczko.poszukiwacz.screen.searching.COMPASS

@UninstallModules(TestPortsModule::class, BuildVariantSpecificTestPortsModule::class)
@HiltAndroidTest
class MainScreenTest : UiTest() {

    @Before
    override fun init() {
        hiltRule.inject()
        // do nothing more, this test doesn't use pl.marianjureczko.poszukiwacz.TestPortsModule
    }

    @After
    override fun restoreRoute() {
        // do nothing, this test doesn't use pl.marianjureczko.poszukiwacz.TestPortsModule
    }

    @Test
    fun shouldShowGuideViewsWithImages_whenArrowIsClicked() {
        //given
//        composeRule.waitForIdle()
        val text: SemanticsNodeInteraction = getNode(GUIDE_TEXT)

        //then
        text.assertTextEquals(context.getString(R.string.custom_lead1))
        assertImageIsDisplayed(R.drawable.al)

        performTap(NEXT_GUIDE_BUTTON)
        text.assertTextEquals(context.getString(R.string.custom_lead2))
        assertImageIsDisplayed(R.drawable.kalinowice_wita)

        performTap(NEXT_GUIDE_BUTTON)
        text.assertTextEquals(context.getString(R.string.custom_lead3))
        assertImageIsDisplayed(R.drawable.qr)

        performTap(NEXT_GUIDE_BUTTON)
        text.assertTextEquals(context.getString(R.string.custom_lead4))
        assertImageIsDisplayed(R.drawable.chest)

        performTap(NEXT_GUIDE_BUTTON)
        text.assertTextEquals(context.getString(R.string.custom_lead5))
        assertImageIsDisplayed(R.drawable.lead5)

        performTap(NEXT_GUIDE_BUTTON)
        text.assertTextEquals(context.getString(R.string.custom_lead6))
        assertImageIsDisplayed(R.drawable.change_chest)
    }

    @Test
    fun shouldStartTheSearching() {
        // when
        goToSearching()

        //then
        val node = getNode(COMPASS)
        node.assertIsDisplayed()
    }
}