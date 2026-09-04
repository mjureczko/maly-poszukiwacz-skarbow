package pl.marianjureczko.poszukiwacz.compass

import com.ocadotechnology.gembus.test.someFloat
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import pl.marianjureczko.poszukiwacz.compass.data.LocationHolder

class LocationHolderTest {

    val sut = LocationHolder()

    @Test
    fun `SHOULD use new location as current WHEN is of high accuracy`() {
        //given
        val newLocation = LocationWrapperArranger.accuracyBelow50m()

        //when
        val actual = sut.updateLocation(newLocation)

        //then
        Assertions.assertThat(actual.getCurrentUserLocation()).isEqualTo(newLocation)
    }

    @Test
    fun `SHOULD not use new location as current when is of low accuracy`() {
        //given
        val accurateLocation = LocationWrapperArranger.accuracyBelow50m()
        var actual = sut.updateLocation(accurateLocation)
        val inaccurateLocation = LocationWrapperArranger.accuracyAbove50m()

        //when
        actual = actual.updateLocation(inaccurateLocation)

        //then
        Assertions.assertThat(actual.getCurrentUserLocation()).isEqualTo(accurateLocation)
    }

    @Test
    fun `SHOULD use new location as current WHEN is of low accuracy but for long time period there were no good accuracies`() {
        //given
        val goodLocationTimeAnchor = LocationWrapperArranger.accuracyBelow50m(0L)
        var actual = sut.updateLocation(goodLocationTimeAnchor)
        val lowAccuracyLocation = LocationWrapperArranger.accuracyAbove50m(goodLocationTimeAnchor.observedAt + 4001L)

        //when
        actual = actual.updateLocation(lowAccuracyLocation)

        //then
        Assertions.assertThat(actual.getCurrentUserLocation()).isEqualTo(lowAccuracyLocation)
    }

    @Test
    fun `SHOULD use inaccurate location as current WHEN it is the first reading`() {
        //given
        val lowAccuracyLocation = LocationWrapperArranger.accuracyAbove50m()

        //when
        val actual = sut.updateLocation(lowAccuracyLocation)

        //then
        Assertions.assertThat(actual.getCurrentUserLocation()).isEqualTo(lowAccuracyLocation)
    }

    @Test
    fun `SHOULD use the most accurate location from the inaccurate ones WHEN for long time period there were no good accuracies`() {
        //given
        val goodLocationTimeAnchor = LocationWrapperArranger.accuracyBelow50m(0L)
        var actual = sut.updateLocation(goodLocationTimeAnchor)
        val lowAccuracyShortPeriod = LocationWrapperArranger.givenAccuracy(
            accuracy = someFloat(100f, 200f),
            observedAt = goodLocationTimeAnchor.observedAt + 1000L
        )
        actual = actual.updateLocation(lowAccuracyShortPeriod)
        val lowButBestAccuracyShortPeriod = LocationWrapperArranger.givenAccuracy(
            accuracy = lowAccuracyShortPeriod.accuracy - 1f,
            observedAt = goodLocationTimeAnchor.observedAt + 1001L
        )
        actual = actual.updateLocation(lowButBestAccuracyShortPeriod)
        Assertions.assertThat(actual.getCurrentUserLocation()).isEqualTo(goodLocationTimeAnchor)
        val lowAccuracyLongPeriod = LocationWrapperArranger.givenAccuracy(
            accuracy = lowAccuracyShortPeriod.accuracy + 1f,
            observedAt = goodLocationTimeAnchor.observedAt + 4001L
        )

        //when
        actual = actual.updateLocation(lowAccuracyLongPeriod)

        //then
        Assertions.assertThat(actual.getCurrentUserLocation()).isEqualTo(lowButBestAccuracyShortPeriod)
    }
}
