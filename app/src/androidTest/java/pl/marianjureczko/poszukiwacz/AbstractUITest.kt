package pl.marianjureczko.poszukiwacz

import android.Manifest
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
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

abstract class AbstractUITest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @get:Rule(order = 3)
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO
    )
    val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun init() {
        hiltRule.inject()
    }

    fun performTap(contentDescription: String) {
        composeRule
            .onNodeWithContentDescription(contentDescription)
            .assertExists()
            .performClick()
        composeRule.waitForIdle()
    }

    fun getNode(contentDescription: String): SemanticsNodeInteraction {
        return composeRule.onNodeWithContentDescription(contentDescription)
    }

    fun assertImageIsDisplayed(drawableId: Int) {
        composeRule.onNodeWithTag(drawableId.toString())
            .assertIsDisplayed()
    }
}