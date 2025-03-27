package pl.marianjureczko.poszukiwacz.model

import com.ocadotechnology.gembus.test.CustomArranger
import com.ocadotechnology.gembus.test.someDouble
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
            latitude = someDouble(-90.0, 90.0),
            longitude = someDouble(-180.0, 180.0)
        )
    }
}