package pl.marianjureczko.poszukiwacz.model

import com.ocadotechnology.gembus.test.CustomArranger
import com.ocadotechnology.gembus.test.some
import com.ocadotechnology.gembus.test.someString
import pl.marianjureczko.poszukiwacz.activity.treasureselector.Coordinates

class TreasuresProgressArranger : CustomArranger<TreasuresProgress>() {
    override fun instance(): TreasuresProgress {
        val instance = super.instance()
        val treasureDescription = some<TreasureDescription>()
        instance.collect(treasureDescription)
        instance.collect(some<Treasure>())
        instance.addCommemorativePhoto(treasureDescription, someString())
        instance.hunterPath.addLocation(Coordinates(some<Double>() % 180, some<Double>() % 90))
        return instance
    }
}