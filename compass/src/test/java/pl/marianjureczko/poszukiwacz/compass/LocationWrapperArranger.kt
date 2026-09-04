package pl.marianjureczko.poszukiwacz.compass

import com.ocadotechnology.gembus.test.CustomArranger
import com.ocadotechnology.gembus.test.someDouble
import com.ocadotechnology.gembus.test.someFloat
import pl.marianjureczko.poszukiwacz.compass.api.AndroidLocation
import pl.marianjureczko.poszukiwacz.compass.data.LocationWrapper

class LocationWrapperArranger : CustomArranger<LocationWrapper>() {

    companion object {
        fun accuracyBelow50m(observedAt: Long = System.currentTimeMillis()): AndroidLocation {
            return LocationWrapper(
                latitude = someLat(),
                longitude = someLong(),
                accuracy = someFloat(0f, 49.9f),
                observedAt = observedAt
            )
        }

        fun accuracyAbove50m(observedAt: Long = System.currentTimeMillis()): AndroidLocation {
            return LocationWrapper(
                latitude = someLat(),
                longitude = someLong(),
                accuracy = someFloat(50f, 149f),
                observedAt = observedAt
            )
        }

        fun givenAccuracy(accuracy: Float, observedAt: Long = System.currentTimeMillis()): AndroidLocation {
            return LocationWrapper(
                latitude = someLat(),
                longitude = someLong(),
                accuracy = accuracy,
                observedAt = observedAt
            )
        }

        private fun someLat(): Double = someDouble(-60.0, 60.0)

        private fun someLong(): Double = someDouble(-60.0, 60.0)
    }
}