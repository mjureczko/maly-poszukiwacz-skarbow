package pl.marianjureczko.poszukiwacz.compass

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.kotlin.eq
import pl.marianjureczko.poszukiwacz.compass.api.AndroidLocation
import pl.marianjureczko.poszukiwacz.compass.api.LocationCalculator
import pl.marianjureczko.poszukiwacz.compass.api.LocationPort
import pl.marianjureczko.poszukiwacz.compass.api.LocationUpdateCallback
import pl.marianjureczko.poszukiwacz.compass.data.LocationHolder
import pl.marianjureczko.poszukiwacz.compass.domain.UpdateLocationUC
import pl.marianjureczko.poszukiwacz.compass.viewmodel.CompassState
import pl.marianjureczko.poszukiwacz.compass.viewmodel.CompassViewModel
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class CompassViewModelTest {

    @Test
    fun `SHOULD update location and call callback`() {
        // given
        val locationCalculator = mock(LocationCalculator::class.java)
        val updateLocationUC = UpdateLocationUC(locationCalculator)

        val targetLocation = AndroidLocation.create(latitude = 50.0, longitude = 20.0)
        val newLocation = AndroidLocation.create(
            latitude = targetLocation.latitude,
            longitude = targetLocation.longitude - 1.0, // to assure east direction at compass
            accuracy = 15f,
            observedAt = System.currentTimeMillis()
        )

        val expectedDistance = 1000
        given(locationCalculator.distanceInSteps(targetLocation, newLocation))
            .willReturn(expectedDistance)

        val state = mutableStateOf(CompassState())
        val capturedLocations = mutableListOf<AndroidLocation>()
        val locationUpdateCallback = LocationUpdateCallback { location -> capturedLocations.add(location) }

        // when
        updateLocationUC.invoke(newLocation, targetLocation, state, locationUpdateCallback)

        // then
        assertThat(capturedLocations).hasSize(1)
        assertThat(capturedLocations[0]).isEqualTo(newLocation)

        val goEastDirection = 90.0f
        assertThat(state.value.currentLocation).isNotNull
        assertThat(state.value.stepsToTreasure).isEqualTo(expectedDistance)
        assertThat(state.value.needleRotation).isEqualTo(goEastDirection, Offset.offset(0.01f))
        assertThat(state.value.gpsAccuracy).isEqualTo(GpsAccuracy.Fine)
    }

    @Test
    fun `SHOULD handle location updates without target`() {
        // given
        val locationCalculator = mock(LocationCalculator::class.java)
        val updateLocationUC = UpdateLocationUC(locationCalculator)

        val newLocation = AndroidLocation.create(
            latitude = 50.0,
            longitude = 20.0,
            accuracy = 10f,
            observedAt = System.currentTimeMillis()
        )

        val capturedLocations = mutableListOf<AndroidLocation>()
        val locationUpdateCallback = LocationUpdateCallback { location ->
            capturedLocations.add(location)
        }
        val state = mutableStateOf(CompassState())

        // when
        updateLocationUC.invoke(newLocation, null, state, locationUpdateCallback)

        // then
        // Verify that the location was captured by the callback even without a target
        assertThat(capturedLocations).hasSize(1)
        assertThat(capturedLocations[0]).isEqualTo(newLocation)

        assertThat(state.value.currentLocation).isNotNull
        assertThat(state.value.stepsToTreasure).isNull()
        assertThat(state.value.needleRotation).isEqualTo(0f)
        assertThat(state.value.gpsAccuracy).isEqualTo(GpsAccuracy.Fine)
    }

    @ParameterizedTest
    @MethodSource("gpsAccuracyTestCases")
    fun `SHOULD handle different GPS accuracy levels`(
        accuracy: Float, expectedGpsAccuracy: GpsAccuracy
    ) {
        // given
        val locationCalculator = mock(LocationCalculator::class.java)
        val updateLocationUC = UpdateLocationUC(locationCalculator)
        val newLocation = AndroidLocation.create(
            latitude = 50.0,
            longitude = 20.0,
            accuracy = accuracy,
            observedAt = System.currentTimeMillis()
        )

        // when
        val state = mutableStateOf(CompassState())
        updateLocationUC.invoke(newLocation, null, state) { }

        // then
        assertThat(state.value.gpsAccuracy).isEqualTo(expectedGpsAccuracy)
    }

    @Test
    fun `SHOULD recalculate steps and needle WHEN setSelectedTreasure is set after a location is known`() {
        // given
        val target = AndroidLocation.create(latitude = 50.0, longitude = 20.0)
        val firstLocation = AndroidLocation.create(latitude = 50.1, longitude = 20.0)

        val updateLocationUC = UpdateLocationUC(LocationCalculator())
        val sut = createCompassViewModel(updateLocationUC = updateLocationUC)
        updateLocationUC.invoke(firstLocation, null, sut.getMutableStateForTest(), null)
        assertThat(sut.state.value.currentLocation.getCurrentUserLocation()).isEqualTo(firstLocation)
        assertThat(sut.state.value.stepsToTreasure).isNull()
        assertThat(sut.state.value.needleRotation).isEqualTo(0f)

        // when
        sut.setSelectedTreasure(target)

        // then
        val oneTenthDegreeInSteps = 15884
        // recalculate() ran immediately against the cached currentLocation
        assertThat(sut.state.value.stepsToTreasure).isEqualTo(oneTenthDegreeInSteps)
        assertThat(sut.state.value.needleRotation).isEqualTo(180f, Offset.offset(0.01f))
    }

    @Test
    fun `SHOULD flip gpsAccuracy to NoSignal WHEN last location update is older than the threshold`() {
        // given
        val dispatcher = StandardTestDispatcher()
        val sut = createCompassViewModel(ioDispatcher = dispatcher)
        val staleUpdateTime = Date(System.currentTimeMillis() - LocationHolder.GPS_NO_SIGNAL_THRESHOLD_IN_MILIS - 1000L)
        sut.getMutableStateForTest().value =
            sut.getMutableStateForTest().value.copy(lastLocationUpdateTime = staleUpdateTime)

        // when
        // scheduleGpsCheck waits GPS_NO_SIGNAL_THRESHOLD_IN_MILIS before checking staleness;
        // advance past that and run any pending work.
        dispatcher.scheduler.advanceTimeBy(LocationHolder.GPS_NO_SIGNAL_THRESHOLD_IN_MILIS + 1000L)
        dispatcher.scheduler.runCurrent()

        // then
        assertThat(sut.state.value.gpsAccuracy).isEqualTo(GpsAccuracy.NoSignal)
    }

    @Test
    fun `SHOULD keep gpsAccuracy non-NoSignal WHEN last location update is fresh`() {
        // given
        val dispatcher = StandardTestDispatcher()
        val sut = createCompassViewModel(ioDispatcher = dispatcher)
        // seed a fresh lastLocationUpdateTime directly into the SUT state
        sut.getMutableStateForTest().value = sut.getMutableStateForTest().value.copy(
            lastLocationUpdateTime = Date()
        )

        // when
        dispatcher.scheduler.advanceTimeBy(LocationHolder.GPS_NO_SIGNAL_THRESHOLD_IN_MILIS + 1000L)
        dispatcher.scheduler.runCurrent()

        // then
        assertThat(sut.state.value.gpsAccuracy).isNotEqualTo(GpsAccuracy.NoSignal)
        assertThat(sut.state.value.gpsAccuracy).isEqualTo(GpsAccuracy.Fine)
    }

    @Test
    fun `SHOULD invoke distanceInSteps exactly once per GPS fix WHEN a target is selected`() {
        // given
        val target = AndroidLocation.create(latitude = 50.0, longitude = 20.0)
        val newLocation = AndroidLocation.create(
            latitude = 50.0,
            longitude = 19.0,
            accuracy = 10f,
            observedAt = System.currentTimeMillis()
        )

        val locationCalculator = mock(LocationCalculator::class.java)
        val expectedDistance = 1234
        given(locationCalculator.distanceInSteps(target, newLocation)).willReturn(expectedDistance)

        // capture the lambda the SUT registers with LocationPort.startFetching in init {}.
        // We use a plain object so we can also access the lambda without mockito matchers
        // (which are awkward with Kotlin function types).
        val locationPort = object : LocationPort {
            private var capturedCallback: ((AndroidLocation) -> Unit)? = null
            override fun startFetching(
                coroutineScope: kotlinx.coroutines.CoroutineScope,
                updateLocationCallback: (AndroidLocation) -> Unit
            ) {
                capturedCallback = updateLocationCallback
            }

            override fun stopFetching() {}
            fun fire(location: AndroidLocation) {
                capturedCallback?.invoke(location)
                    ?: error("LocationPort.startFetching was never called by the SUT")
            }
        }

        val sut = CompassViewModel(
            locationPort,
            locationCalculator,
            UpdateLocationUC(locationCalculator),
            StandardTestDispatcher()
        )
        sut.setSelectedTreasure(target)

        // when: simulate a single GPS fix
        locationPort.fire(newLocation)

        // then
        // UpdateLocationUC sets stepsToTreasure on every GPS fix. The previous (buggy) impl
        // also called recalculateIfNeeded() in the same callback, which would have invoked the
        // calculator a second time. We assert the calculator was called exactly once.
        verify(locationCalculator, times(1)).distanceInSteps(eq(target), eq(newLocation))
    }

    private fun createCompassViewModel(
        locationPort: LocationPort = mock(LocationPort::class.java),
        locationCalculator: LocationCalculator = LocationCalculator(),
        updateLocationUC: UpdateLocationUC = UpdateLocationUC(LocationCalculator()),
        ioDispatcher: CoroutineDispatcher = StandardTestDispatcher(),
    ): CompassViewModel = CompassViewModel(locationPort, locationCalculator, updateLocationUC, ioDispatcher)

    companion object {
        @JvmStatic
        fun gpsAccuracyTestCases() = listOf(
            Arguments.of(10f, GpsAccuracy.Fine),
            Arguments.of(50f, GpsAccuracy.Medium),
            Arguments.of(150f, GpsAccuracy.Low)
        )
    }
}
