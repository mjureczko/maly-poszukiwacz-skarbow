package pl.marianjureczko.poszukiwacz

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.onNodeWithContentDescription
import pl.marianjureczko.poszukiwacz.screen.main.START_BUTTON

open class UiTest: AbstractUITest() {

    protected fun goToSearching() {
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
        performTap(START_BUTTON)
        composeRule.waitForIdle()
    }
}