package pl.marianjureczko.poszukiwacz.model

import com.ocadotechnology.gembus.test.CustomArranger
import com.ocadotechnology.gembus.test.some
import com.ocadotechnology.gembus.test.someString

class TreasuresProgressArranger : CustomArranger<TreasuresProgress>() {
    override fun instance(): TreasuresProgress {
        val instance = super.instance()
        val treasureDescription = some<TreasureDescription>()
        instance.routeName = someString()
        instance.collect(some<Treasure>(), treasureDescription)
        instance.addCommemorativePhoto(treasureDescription, someString())
//        instance.hunterPath.addLocation(Coordinates(some<Double>() % 180, some<Double>() % 90))
        return instance
    }
}