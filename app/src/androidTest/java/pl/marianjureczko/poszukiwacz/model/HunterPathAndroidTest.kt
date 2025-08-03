package pl.marianjureczko.poszukiwacz.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import pl.marianjureczko.poszukiwacz.usecase.TestLocation

@RunWith(AndroidJUnit4::class)
class HunterPathAndroidTest {

    @Test
    fun shouldCalculateDistance() {
        //given
        val path = HunterPath()
        path.addLocation(TestLocation(51.1428, 16.5254, observedAt = 0))
        path.addLocation(TestLocation(51.1534, 16.54076, observedAt = 100_000))
        path.addLocation(TestLocation(51.14499, 16.55419, observedAt = 200_000))
        path.addLocation(TestLocation(51.14499, 16.55419, observedAt = 300_000))

        //when
        val actual = path.pathLengthInKm()

        //then
        Assert.assertEquals(2.92, actual, 0.01)
    }
}