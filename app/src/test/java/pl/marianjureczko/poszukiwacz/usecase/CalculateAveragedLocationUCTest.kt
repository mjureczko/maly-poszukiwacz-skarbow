package pl.marianjureczko.poszukiwacz.usecase

import com.ocadotechnology.gembus.test.some
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CalculateAveragedLocationUCTest {
    @Test
    fun `SHOULD return long and lat from the only location WHEN one location available`() {
        //given
        val location = some<AndroidLocation>()

        //when
        val actual = CalculateAveragedLocationUC().invoke(listOf(location))!!

        //then
        assertThat(actual.latitude).isEqualTo(location.latitude)
        assertThat(actual.longitude).isEqualTo(location.longitude)
    }

    @Test
    fun `SHOULD return long and lat from the only location WHEN other locations has way worse accuracy`() {
        //given
        val location = AndroidLocationArranger.withAccuracy(1f)
        val poorAccuracyLocation = AndroidLocationArranger.withAccuracy(100f)

        //when
        val actual = CalculateAveragedLocationUC().invoke(listOf(poorAccuracyLocation, location))!!

        //then
        assertThat(actual.latitude).isEqualTo(location.latitude)
        assertThat(actual.longitude).isEqualTo(location.longitude)
    }

    @Test
    fun `SHOULD use median of lat and long WHEN several locations with similar accuracy available`() {
        //given
        val poorAccuracy = AndroidLocationArranger.withAccuracy(150f)
        val location1 = TestLocation(
            latitude = 1.0,
            longitude = 2.0,
            accuracy = 100f
        )
        val location2 = TestLocation(
            latitude = 3.0,
            longitude = 3.0,
            accuracy = 101f
        )
        val location3 = TestLocation(
            latitude = 2.0,
            longitude = 1.0,
            accuracy = 111f
        )

        //when
        val actual = CalculateAveragedLocationUC().invoke(listOf(poorAccuracy, location1, location2, location3))!!

        //then
        assertThat(actual.latitude).isEqualTo(2.0)
        assertThat(actual.longitude).isEqualTo(2.0)
    }

    @Test
    fun `SHOULD return null WHEN no location with accuracy below 200m`() {
        //given
        val poorAccuracy = AndroidLocationArranger.withAccuracy(210f)

        //when
        val actual = CalculateAveragedLocationUC().invoke(listOf(poorAccuracy))

        //then
        assertThat(actual).isNull()
    }
}