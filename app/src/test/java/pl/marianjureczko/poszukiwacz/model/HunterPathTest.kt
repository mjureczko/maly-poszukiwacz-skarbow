package pl.marianjureczko.poszukiwacz.model

import com.ocadotechnology.gembus.test.some
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import pl.marianjureczko.poszukiwacz.activity.treasureselector.Coordinates
import java.util.Date

class HunterPathTest {

    @Test
    fun shouldReturnNullAsStartTime_whenNoMeasurementsCollected() {
        //given
        val hunterPath = HunterPath()

        //when
        val actual = hunterPath.getStartTime()

        //then
        assertThat(actual).isNull()
    }

    @Test
    fun shouldReturnNullAsEndTime_whenNoMeasurementsCollected() {
        //given
        val hunterPath = HunterPath()

        //when
        val actual = hunterPath.getEndTime()

        //then
        assertThat(actual).isNull()
    }

    @Test
    fun shouldReturnDateOfFirstMeasurement_whenSomeMeasurementsAreCollected() {
        //given
        val hunterPath = HunterPath()
        val firstMeasurement = Date()
        hunterPath.addLocation(some(), firstMeasurement)
        hunterPath.addLocation(some())

        //when
        val actual = hunterPath.getStartTime()

        //then
        assertThat(actual).isEqualTo(firstMeasurement)
    }

    @Test
    fun shouldReturnDateOfLastMeasurement_whenSomeMeasurementsAreCollected() {
        //given
        val hunterPath = HunterPath()
        hunterPath.addLocation(some())
        val lastMeasurement = Date()
        hunterPath.addLocation(some(), lastMeasurement)

        //when
        val actual = hunterPath.getEndTime()

        //then
        assertThat(actual).isEqualTo(lastMeasurement)
    }

    @Test
    fun shouldReturnEmptyList_whenNoHunterLocationsWereCollected() {
        //given
        val hunterPath = HunterPath()

        //when
        val actual = hunterPath.pathAsCoordinates()

        //then
        assertThat(actual).isEmpty()
    }

    @Test
    fun shouldReturnEmptyList_whenCollectedHunterLocationsAreFromTimeSpanSmallerThan20s() {
        //given
        val hunterPath = HunterPath()
        hunterPath.addLocation(some())
        hunterPath.addLocation(some())

        //when
        val actual = hunterPath.pathAsCoordinates().toList()

        //then
        assertThat(actual).isEmpty()
    }

    @Test
    fun shouldReturnListWithSingleCoordinate_whenCollectedHunterLocationsAreFrom30sTimeSpan() {
        //given
        val hunterPath = HunterPath()
        var time = System.currentTimeMillis()
        hunterPath.addLocation(Coordinates(1.0, 10.0), Date(time))
        hunterPath.addLocation(Coordinates(2.0, 20.0), Date(time + 9_000))
        hunterPath.addLocation(Coordinates(3.0, 30.0), Date(time + 19_000))
        hunterPath.addLocation(Coordinates(4.0, 3.0), Date(time + 25_000))
        hunterPath.addLocation(Coordinates(4.0, 3.0), Date(time + 30_000))

        //when
        val actual = hunterPath.pathAsCoordinates()

        //then
        assertThat(actual).containsExactly(Coordinates(2.0, 20.0))
    }

    @Test
    fun shouldReturnListWith2Coordinates_whenCollectedHunterLocationsAreFrom50sTimeSpan() {
        //given
        val hunterPath = HunterPath()
        var time = System.currentTimeMillis()
        hunterPath.addLocation(Coordinates(1.0, 1.0), Date(time))
        hunterPath.addLocation(Coordinates(2.0, 2.0), Date(time + 9_000))
        hunterPath.addLocation(Coordinates(3.0, 3.0), Date(time + 19_000))

        hunterPath.addLocation(Coordinates(4.0, 3.0), Date(time + 25_000))
        hunterPath.addLocation(Coordinates(4.0, 3.0), Date(time + 30_000))
        hunterPath.addLocation(Coordinates(5.0, 3.0), Date(time + 35_000))
        hunterPath.addLocation(Coordinates(5.0, 4.0), Date(time + 39_000))

        hunterPath.addLocation(Coordinates(6.0, 6.0), Date(time + 50_000))

        //when
        val actual = hunterPath.pathAsCoordinates()

        //then
        assertThat(actual).containsExactly(Coordinates(2.0, 2.0), Coordinates(4.5, 3.0))
    }

}