package pl.marianjureczko.poszukiwacz

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.test.rule.GrantPermissionRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pl.marianjureczko.poszukiwacz.activity.main.MainActivity

//@UninstallModules(SingletonModule::class)
@HiltAndroidTest
//@Config(application = HiltTestApplication::class)
class MyInstrumentedTest {


    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @get:Rule(order = 3)
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.CAMERA
    )

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun test() {
        //given
//        composeRule.waitForIdle()
        val text = composeRule.onAllNodesWithContentDescription("TEST", false, false, true)
            .assertCountEquals(1)
            .get(0)

        //then
        text.assertTextContains("WARNING", substring = true)


    }

}