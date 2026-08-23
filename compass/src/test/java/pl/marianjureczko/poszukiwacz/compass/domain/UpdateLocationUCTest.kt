package pl.marianjureczko.poszukiwacz.compass.domain

import androidx.compose.runtime.mutableStateOf
import com.ocadotechnology.gembus.test.Arranger
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import pl.marianjureczko.poszukiwacz.compass.GpsAccuracy
import pl.marianjureczko.poszukiwacz.compass.api.AndroidLocation
import pl.marianjureczko.poszukiwacz.compass.api.LocationCalculator
import pl.marianjureczko.poszukiwacz.compass.viewmodel.CompassState
import java.util.stream.Stream

class UpdateLocationUCTest {

    companion object {
        @JvmStatic
        fun accuracyCases(): Stream<TestCase> {
            val highAccuracyLocation = mock<AndroidLocation> {
                on { accuracy } doReturn Arranger.someFloat(0f, 30f)
            }
            val mediumAccuracyLocation = mock<AndroidLocation> {
                on { accuracy } doReturn Arranger.someFloat(30.01f, 100f)
            }
            val lowAccuracyLocation = mock<AndroidLocation> {
                on { accuracy } doReturn Arranger.someFloat(100.01f, 200f)
            }
            return Stream.of(
                TestCase(highAccuracyLocation, GpsAccuracy.Fine),
                TestCase(mediumAccuracyLocation, GpsAccuracy.Medium),
                TestCase(lowAccuracyLocation, GpsAccuracy.Low)
            )
        }
    }

    data class TestCase(val location: AndroidLocation, val expected: GpsAccuracy)

    @ParameterizedTest
    @MethodSource("accuracyCases")
    fun `should set gpsAccuracy based on location accuracy`(testCase: TestCase) {
        // given
        val location = testCase.location
        val state = mutableStateOf(CompassState())
        val sut = UpdateLocationUC(LocationCalculator())

        // when
        sut.invoke(location, target = null, state = state)

        // then
        assertThat(state.value.gpsAccuracy)
            .isEqualTo(testCase.expected)
    }
}