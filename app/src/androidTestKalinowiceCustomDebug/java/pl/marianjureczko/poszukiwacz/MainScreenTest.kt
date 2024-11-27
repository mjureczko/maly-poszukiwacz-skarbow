package pl.marianjureczko.poszukiwacz

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test
import pl.marianjureczko.poszukiwacz.activity.searching.n.COMPASS
import pl.marianjureczko.poszukiwacz.screen.main.GUIDE_TEXT
import pl.marianjureczko.poszukiwacz.screen.main.NEXT_GUIDE_BUTTON

//@UninstallModules(SingletonModule::class)
@HiltAndroidTest
//@Config(application = HiltTestApplication::class)
class MainScreenTest : UiTest() {


    @Test
    fun shouldGuideViewsWithImages_whenArrowIsClicked() {
        //given
//        composeRule.waitForIdle()
        val text: SemanticsNodeInteraction = getNode(GUIDE_TEXT)

        //then
        text.assertTextEquals(context.getString(R.string.custom_lead1))
        assertImageIsDisplayed(R.drawable.al)

        performClick(NEXT_GUIDE_BUTTON)
        text.assertTextEquals(context.getString(R.string.custom_lead2))
        assertImageIsDisplayed(R.drawable.kalinowice_wita)

        performClick(NEXT_GUIDE_BUTTON)
        text.assertTextEquals(context.getString(R.string.custom_lead3))
        assertImageIsDisplayed(R.drawable.qr)

        performClick(NEXT_GUIDE_BUTTON)
        text.assertTextEquals(context.getString(R.string.custom_lead4))
        assertImageIsDisplayed(R.drawable.chest)

        performClick(NEXT_GUIDE_BUTTON)
        text.assertTextEquals(context.getString(R.string.custom_lead5))
        assertImageIsDisplayed(R.drawable.lead5)

        performClick(NEXT_GUIDE_BUTTON)
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