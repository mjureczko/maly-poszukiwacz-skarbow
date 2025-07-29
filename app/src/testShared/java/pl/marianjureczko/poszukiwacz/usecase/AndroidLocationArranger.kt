package pl.marianjureczko.poszukiwacz.usecase

import com.ocadotechnology.gembus.test.CustomArranger
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
}