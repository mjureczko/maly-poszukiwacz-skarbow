package pl.marianjureczko.poszukiwacz.model

import com.ocadotechnology.gembus.test.CustomArranger
import com.ocadotechnology.gembus.test.some
import com.ocadotechnology.gembus.test.someString

class TreasuresProgressArranger : CustomArranger<TreasuresProgress>() {
    override fun instance(): TreasuresProgress {
        val instance = super.instance()
        val treasureDescription = some<TreasureDescription>()
        instance.collect(treasureDescription)
        instance.collect(some<Treasure>())
        instance.addCommemorativePhoto(treasureDescription, someString())
        return instance
    }
}