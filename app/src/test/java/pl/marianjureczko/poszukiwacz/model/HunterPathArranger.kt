package pl.marianjureczko.poszukiwacz.model

import com.ocadotechnology.gembus.test.CustomArranger
import com.ocadotechnology.gembus.test.some

class HunterPathArranger : CustomArranger<HunterPath>() {
    override fun instance(): HunterPath {
        val instance = super.instance()
        instance.addLocation(some())
        return instance
    }
}