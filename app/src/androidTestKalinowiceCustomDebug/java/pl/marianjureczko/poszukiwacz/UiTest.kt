package pl.marianjureczko.poszukiwacz

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.onNodeWithContentDescription
import pl.marianjureczko.poszukiwacz.screen.main.START_BUTTON
import pl.marianjureczko.poszukiwacz.screen.searching.CHANGE_TREASURE_BUTTON

open class UiTest: AbstractUITest() {

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
}