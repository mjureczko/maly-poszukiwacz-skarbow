package pl.marianjureczko.poszukiwacz.usecase

import com.ocadotechnology.gembus.test.some
import com.ocadotechnology.gembus.test.someFloat
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class LocationHolderTest {

    val sut = LocationHolder()

    @Test
    fun `SHOULD use new location as current WHEN is of high accuracy`() {
        //given
        val newLocation = AndroidLocationArranger.accuracyBelow50m()

        //when
        val actual = sut.updateLocation(newLocation)

        //then
        assertThat(actual.getCurrentUserLocation()).isEqualTo(newLocation)
    }

    @Test
    fun `SHOULD not use new location as current when is of low accuracy`() {
        //given
        val accurateLocation = AndroidLocationArranger.accuracyBelow50m()
        var actual = sut.updateLocation(accurateLocation)
        val inaccurateLocation = AndroidLocationArranger.accuracyAbove50m()

        //when
        actual = actual.updateLocation(inaccurateLocation)

        //then
        assertThat(actual.getCurrentUserLocation()).isEqualTo(accurateLocation)
    }

    @Test
    fun `SHOULD use new location as current WHEN is of low accuracy but for long time period there were no good accuracies`() {
        //given
        val goodLocationTimeAnchor = AndroidLocationArranger.accuracyBelow50m(0L)
        var actual = sut.updateLocation(goodLocationTimeAnchor)
        val lowAccuracyLocation = AndroidLocationArranger.accuracyAbove50m(goodLocationTimeAnchor.observedAt + 4001L)

        //when
        actual = actual.updateLocation(lowAccuracyLocation)

        //then
        assertThat(actual.getCurrentUserLocation()).isEqualTo(lowAccuracyLocation)
    }

    @Test
    fun `SHOULD use inaccurate location as current WHEN it is the first reading`() {
        //given
        val lowAccuracyLocation = AndroidLocationArranger.accuracyAbove50m()

        //when
        val actual = sut.updateLocation(lowAccuracyLocation)

        //then
        assertThat(actual.getCurrentUserLocation()).isEqualTo(lowAccuracyLocation)
    }

    @Test
    fun `SHOULD use the most accurate location from the inaccurate ones WHEN for long time period there were no good accuracies`() {
        //given
        val goodLocationTimeAnchor = AndroidLocationArranger.accuracyBelow50m(0L)
        var actual = sut.updateLocation(goodLocationTimeAnchor)
        val lowAccuracyShortPeriod = some<TestLocation>().copy(
            accuracy = someFloat(100f, 200f),
            observedAt = goodLocationTimeAnchor.observedAt + 1000L
        )
        actual = actual.updateLocation(lowAccuracyShortPeriod)
        val lowButBestAccuracyShortPeriod = some<TestLocation>().copy(
            accuracy = lowAccuracyShortPeriod.accuracy - 1f,
            observedAt = goodLocationTimeAnchor.observedAt + 1001L
        )
        actual = actual.updateLocation(lowButBestAccuracyShortPeriod)
        assertThat(actual.getCurrentUserLocation()).isEqualTo(goodLocationTimeAnchor)
        val lowAccuracyLongPeriod = some<TestLocation>().copy(
            accuracy = lowAccuracyShortPeriod.accuracy + 1f,
            observedAt = goodLocationTimeAnchor.observedAt + 4001L
        )

        //when
        actual = actual.updateLocation(lowAccuracyLongPeriod)

        //then
        assertThat(actual.getCurrentUserLocation()).isEqualTo(lowButBestAccuracyShortPeriod)
    }
}