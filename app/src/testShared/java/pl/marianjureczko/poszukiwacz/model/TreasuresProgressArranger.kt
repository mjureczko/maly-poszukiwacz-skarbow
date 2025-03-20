package pl.marianjureczko.poszukiwacz.model

import com.ocadotechnology.gembus.test.CustomArranger
import com.ocadotechnology.gembus.test.some
import com.ocadotechnology.gembus.test.someString

class TreasuresProgressArranger : CustomArranger<TreasuresProgress>() {
    override fun instance(): TreasuresProgress {
        var instance = super.instance()
        val treasureDescription = some<TreasureDescription>()
        instance.routeName = someString()
        instance.collect(some<Treasure>(), treasureDescription)
        instance =
            instance.copy(commemorativePhotosByTreasuresDescriptionIds = mutableMapOf(treasureDescription.id to someString()))
        return instance
    }
}