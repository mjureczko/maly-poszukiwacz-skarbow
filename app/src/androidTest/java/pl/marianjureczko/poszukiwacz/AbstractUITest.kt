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
import org.junit.After
import org.junit.Before
import org.junit.Rule
import pl.marianjureczko.poszukiwacz.activity.main.MainActivity
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.screen.main.CustomInitializerForRoute
import pl.marianjureczko.poszukiwacz.shared.port.StorageHelper
import javax.inject.Inject

abstract class AbstractUITest {

    /** For custom (e.g. Kalinowice) tests it is the not test storage */
    @Inject
    internal lateinit var injectableStorage: StorageHelper
    var storage: TestStoragePort? = null
    var route: Route? = null
        get() {
            if (field == null) {
                field = getRouteFromStorage()
            }
            return field
        }

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
    open fun init() {
        hiltRule.inject()
        if (injectableStorage is TestStoragePort) {
            storage = injectableStorage as TestStoragePort
        }
    }

    @After
    open fun restoreRoute() {
        BuildVariantSpecificTestPortsModule.assureRouteIsPresentInStorage()
        route?.let {
            injectableStorage.removeProgress(it.name)
        }
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

    protected fun getRouteFromStorage(): Route {
        return if (storage == null) {
            injectableStorage.loadRoute(CustomInitializerForRoute.routeName)
        } else {
            storage!!.routes.values.first()
        }
    }
}