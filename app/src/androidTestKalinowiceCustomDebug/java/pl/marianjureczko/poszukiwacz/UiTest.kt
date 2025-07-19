package pl.marianjureczko.poszukiwacz

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import pl.marianjureczko.poszukiwacz.screen.main.CustomInitializerForRoute
import pl.marianjureczko.poszukiwacz.screen.main.START_BUTTON
import pl.marianjureczko.poszukiwacz.screen.searching.CHANGE_TREASURE_BUTTON
import pl.marianjureczko.poszukiwacz.ui.components.TOPBAR_MENU_BUTTON
import pl.marianjureczko.poszukiwacz.ui.components.TOPBAR_MENU_RESTART
import javax.inject.Inject

open class UiTest: AbstractUITest() {

    @Inject
    internal lateinit var customInitializerForRoute: CustomInitializerForRoute

    protected fun goToSearching() {
        waitForInitialization()
        performTap(START_BUTTON)
        composeRule.waitForIdle()
    }

    protected fun waitForInitialization() {
        var buttonDisabled = true
        while (buttonDisabled) {
            try {
                composeRule
                    .onNodeWithContentDescription(START_BUTTON)
                    .assertIsEnabled()
                buttonDisabled = false
            } catch (ex: AssertionError) {
                Thread.sleep(100)
            }
        }
    }

    /**
     * Assumes that searching screen is open.
     */
    protected fun goToSelector() {
        performTap(CHANGE_TREASURE_BUTTON)
        composeRule.waitForIdle()
    }

    protected fun openTopBarMenu() {
        composeRule.onNodeWithContentDescription(TOPBAR_MENU_BUTTON).performClick()
        composeRule.waitForIdle()
    }

    protected fun clickRestartMenuEntry() {
        composeRule.onNodeWithContentDescription(TOPBAR_MENU_RESTART).performClick()
        composeRule.waitForIdle()
    }
}
