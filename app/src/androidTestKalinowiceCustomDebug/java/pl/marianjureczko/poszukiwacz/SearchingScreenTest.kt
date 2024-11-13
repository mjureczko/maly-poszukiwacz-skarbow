package pl.marianjureczko.poszukiwacz

import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.location.provider.ProviderProperties
import android.os.Build
import android.os.SystemClock
import androidx.annotation.RequiresApi
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertTextEquals
import androidx.test.InstrumentationRegistry
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test
import pl.marianjureczko.poszukiwacz.activity.searching.n.STEPS_TO_TREASURE
import pl.marianjureczko.poszukiwacz.screen.main.CustomInitializerForRoute
import pl.marianjureczko.poszukiwacz.shared.port.StorageHelper

@HiltAndroidTest
class SearchingScreenTest : UiTest() {

    //TODO: introduce ports that are installed in different hilt module and replace them in tests
    @RequiresApi(Build.VERSION_CODES.S)
    @Test
    fun shouldUpdateNavigationWidgets_whenLocationIsUpdated() {
        //given
        val context = ApplicationProvider.getApplicationContext<Context>()
        val locationManager: LocationManager = createLocationManager(context)
        goToSearching()
        val route = StorageHelper(context).loadRoute(CustomInitializerForRoute.routeName)

        //when
        setLocation(
            locationManager,
            route.treasures[0].latitude + 0.01,
            route.treasures[0].longitude + 0.01
        )

        //then
        val stepsToTreasure: SemanticsNodeInteraction = getNode(STEPS_TO_TREASURE)
        stepsToTreasure.assertTextEquals("2374")
    }

    private fun setLocation(locationManager: LocationManager, newLatitude: Double, newLongitude: Double) {
        val mockLocation = Location(LocationManager.GPS_PROVIDER).apply {
            latitude = newLatitude
            longitude = newLongitude
            altitude = 0.0
            accuracy = 1.0f
            time = System.currentTimeMillis()
            elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos() // Important for new Android versions
        }
        locationManager.setTestProviderLocation(LocationManager.GPS_PROVIDER, mockLocation)
        Thread.sleep(100)
        composeRule.waitForIdle()
    }

    private fun createLocationManager(context: Context): LocationManager {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            with(getInstrumentation().uiAutomation) {
                executeShellCommand("appops set " + InstrumentationRegistry.getTargetContext().packageName + " android:mock_location allow")
                Thread.sleep(100)
            }
        }
        val locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // Enable mock locations for the LocationManager
        locationManager.addTestProvider(
            LocationManager.GPS_PROVIDER,
            false,
            false,
            false,
            false,
            true,
            true,
            true,
            ProviderProperties.POWER_USAGE_MEDIUM,
            ProviderProperties.ACCURACY_FINE
        )
        locationManager.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true)
        return locationManager
    }

}