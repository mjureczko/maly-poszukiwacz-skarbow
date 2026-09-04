package pl.marianjureczko.poszukiwacz.usecase

import com.ocadotechnology.gembus.test.CustomArranger
import com.ocadotechnology.gembus.test.some
import com.ocadotechnology.gembus.test.someDouble
import com.ocadotechnology.gembus.test.someFloat
import com.ocadotechnology.gembus.test.somePositiveLong
import pl.marianjureczko.poszukiwacz.compass.api.AndroidLocation

class AndroidLocationArranger : CustomArranger<AndroidLocation>() {
    override fun instance(): AndroidLocation {
        return TestLocation(
            latitude = someDouble(),
            longitude = someDouble(),
            accuracy = someFloat(0f, 100f),
            observedAt = somePositiveLong(System.currentTimeMillis())
        )
    }

    companion object {
        fun withAccuracy(accuracy: Float): AndroidLocation {
            return some<TestLocation>().copy(accuracy = accuracy)
        }
    }
}
