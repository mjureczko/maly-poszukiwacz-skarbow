package pl.marianjureczko.poszukiwacz

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import dagger.hilt.android.testing.HiltAndroidRule
import org.junit.Before
import org.junit.Rule
import pl.marianjureczko.poszukiwacz.activity.main.MainActivity
import pl.marianjureczko.poszukiwacz.screen.main.START_BUTTON

open class UiTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @get:Rule(order = 3)
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.CAMERA
    )
    val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun init() {
        hiltRule.inject()
    }

    fun performClick(contentDescription: String) {
        composeRule
            .onNodeWithContentDescription(contentDescription)
            .assertExists()
            .performClick()
    }

    fun getNode(contentDescription: String): SemanticsNodeInteraction {
        return composeRule.onNodeWithContentDescription(contentDescription)
            .assertExists()
    }

    fun assertImageIsDisplayed(drawableId: Int) {
        composeRule.onNodeWithTag(drawableId.toString())
            .assertIsDisplayed()
    }

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
        performClick(START_BUTTON)
        composeRule.waitForIdle()
    }
}