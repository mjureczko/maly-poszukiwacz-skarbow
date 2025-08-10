package pl.marianjureczko.poszukiwacz.model

import com.ocadotechnology.gembus.test.some
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import pl.marianjureczko.poszukiwacz.usecase.TestLocation
import java.util.Date

class HunterPathTest {

    @Test
    fun shouldReturnNullAsStartTime_whenNoMeasurementsCollected() {
        //given
        val hunterPath = HunterPath(some<String>())

        //when
        val actual = hunterPath.start

        //then
        assertThat(actual).isNull()
    }

    @Test
    fun shouldReturnNullAsEndTime_whenNoMeasurementsCollected() {
        //given
        val hunterPath = HunterPath(some<String>())

        //when
        val actual = hunterPath.end

        //then
        assertThat(actual).isNull()
    }

    @Test
    fun shouldReturnDateOfFirstMeasurement_whenSomeMeasurementsAreCollected() {
        //given
        val firstDate = System.currentTimeMillis()
        val firstLocation = some<TestLocation>().copy(
            observedAt = firstDate
        )
        val secondLocation = some<TestLocation>().copy(
            observedAt = firstDate + 1
        )
        val hunterPath = HunterPath(some<String>())
            .addLocation(firstLocation)
            .addLocation(secondLocation)

        //when
        val actual = hunterPath.start

        //then
        assertThat(actual).isEqualTo(Date(firstDate))
    }

    @Test
    fun shouldReturnDateOfLastMeasurement_whenSomeMeasurementsAreCollected() {
        //given
        val lastMeasurement = System.currentTimeMillis()
        val hunterPath = HunterPath(some<String>())
            .addLocation(some<TestLocation>())
            .addLocation(
                some<TestLocation>().copy(
                    observedAt = lastMeasurement
                )
            )

        //when
        val actual = hunterPath.end

        //then
        assertThat(actual).isEqualTo(Date(lastMeasurement))
    }

    @Test
    fun shouldReturnEmptyList_whenNoHunterLocationsWereCollected() {
        //given
        val hunterPath = HunterPath(some<String>())

        //when
        val actual = hunterPath.path()

        //then
        assertThat(actual).isEmpty()
    }

    @Test
    fun shouldReturnEmptyList_whenCollectedHunterLocationsAreFromTimeSpanSmallerThan20s() {
        //given
        val hunterPath = HunterPath(some<String>())
            .addLocation(some<TestLocation>().copy(observedAt = 1))
            .addLocation(some<TestLocation>().copy(observedAt = 19_999))

        //when
        val actual = hunterPath.path().toList()

        //then
        assertThat(actual).isEmpty()
    }

    @Test
    fun shouldReturnListWithSingleCoordinate_whenCollectedHunterLocationsAreFrom30sTimeSpan() {
        //given
        var time = System.currentTimeMillis()
        val hunterPath = HunterPath(some<String>())
            .addLocation(TestLocation(1.0, 10.0, accuracy = 1f, observedAt = time))
            .addLocation(TestLocation(2.0, 20.0, accuracy = 1f, observedAt = time + 9_000))
            .addLocation(TestLocation(3.0, 30.0, accuracy = 1f, observedAt = time + 19_000))
            .addLocation(TestLocation(4.0, 3.0, accuracy = 1f, observedAt = time + 25_000))
            .addLocation(TestLocation(4.0, 3.0, accuracy = 1f, observedAt = time + 30_000))

        //when
        val actual = hunterPath.path()

        //then
        assertThat(actual).containsExactly(AveragedLocation(latitude = 2.0, longitude = 20.0))
    }

    @Test
    fun shouldReturnListWith2Coordinates_whenCollectedHunterLocationsAreFrom50sTimeSpan() {
        //given
        var time = System.currentTimeMillis()
        val hunterPath = HunterPath(some<String>())
            .addLocation(TestLocation(1.0, 1.0, accuracy = 1f, observedAt = time))
            .addLocation(TestLocation(2.0, 2.0, accuracy = 1f, observedAt = time + 9_000))
            .addLocation(TestLocation(3.0, 3.0, accuracy = 1f, observedAt = time + 19_000))

            .addLocation(TestLocation(4.0, 3.0, accuracy = 1f, observedAt = time + 25_000))
            .addLocation(TestLocation(4.0, 3.0, accuracy = 1f, observedAt = time + 30_000))
            .addLocation(TestLocation(5.0, 3.0, accuracy = 1f, observedAt = time + 35_000))
            .addLocation(TestLocation(5.0, 4.0, accuracy = 1f, observedAt = time + 39_000))

            .addLocation(TestLocation(6.0, 6.0, accuracy = 1f, observedAt = time + 50_000))

        //when
        val actual = hunterPath.path()

        //then
        assertThat(actual).containsExactly(
            AveragedLocation(2.0, 2.0), AveragedLocation(latitude = 4.5, longitude = 3.0)
        )
    }

}