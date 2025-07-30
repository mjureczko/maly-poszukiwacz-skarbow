package pl.marianjureczko.poszukiwacz.usecase

import com.ocadotechnology.gembus.test.CustomArranger
import com.ocadotechnology.gembus.test.some
import com.ocadotechnology.gembus.test.someDouble
import com.ocadotechnology.gembus.test.someFloat
import com.ocadotechnology.gembus.test.somePositiveLong

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
        fun accuracyBelow50m(observedAt: Long = System.currentTimeMillis()): AndroidLocation {
            return some<TestLocation>().copy(
                accuracy = someFloat(0f, 49.9f),
                observedAt = observedAt
            )
        }

        fun accuracyAbove50m(observedAt: Long = System.currentTimeMillis()): AndroidLocation {
            return some<TestLocation>().copy(
                accuracy = someFloat(50f, 149f),
                observedAt = observedAt
            )
        }
    }
}