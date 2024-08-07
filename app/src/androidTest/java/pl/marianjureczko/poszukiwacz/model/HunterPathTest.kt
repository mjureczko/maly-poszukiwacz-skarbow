package pl.marianjureczko.poszukiwacz.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import pl.marianjureczko.poszukiwacz.shared.Coordinates
import java.util.Date

@RunWith(AndroidJUnit4::class)
class HunterPathTest {

    @Test
    fun shouldCalculateDistance() {
        //given
        val path = HunterPath()
        path.addLocation(Coordinates(51.1428, 16.5254), Date(0))
        path.addLocation(Coordinates(51.1534, 16.54076), Date(100_000))
        path.addLocation(Coordinates(51.14499, 16.55419), Date(200_000))
        path.addLocation(Coordinates(51.14499, 16.55419), Date(300_000))

        //when
        val actual = path.pathLengthInKm()

        //then
        Assert.assertEquals(2.92, actual, 0.01)
    }
}