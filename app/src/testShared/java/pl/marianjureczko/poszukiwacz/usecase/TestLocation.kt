package pl.marianjureczko.poszukiwacz.usecase

import com.ocadotechnology.gembus.test.someFloat
import com.ocadotechnology.gembus.test.someLong

data class TestLocation(
    override var latitude: Double,
    override var longitude: Double,
    override val accuracy: Float = someFloat(),
    override val observedAt: Long = someLong()
) : AndroidLocation {

    private var distance = 0f

    @Synchronized
    fun getDistance(): Float = distance

    @Synchronized
    fun setDistance(value: Float) {
        distance = value
    }

    override fun distanceTo(dest: AndroidLocation): Float {
        return getDistance()
    }
}