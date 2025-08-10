package pl.marianjureczko.poszukiwacz.model

import com.ocadotechnology.gembus.test.CustomArranger
import com.ocadotechnology.gembus.test.someFrom
import com.ocadotechnology.gembus.test.someInt
import com.ocadotechnology.gembus.test.somePositiveInt

class TreasureDescriptionArranger : CustomArranger<TreasureDescription>() {

    companion object {
        fun validQrCode(treasureType: String = someFrom(setOf("g", "r", "d", "k"))): String {
            val quantity = someInt(10, 99)
            val id = somePositiveInt(99)
            return "$treasureType$quantity$id"
        }
    }

    override fun instance(): TreasureDescription {
        return super.instance().copy(
            qrCode = validQrCode(),
            //nextDouble(DD)D not available in class Lorg/jeasy/random/EasyRandom on Android
            latitude = someInt(-900, 900) / 10.0,
            longitude = someInt(-1800, 1800) / 10.0
        )
    }
}