package pl.marianjureczko.poszukiwacz.shared.port.storage

import com.ocadotechnology.gembus.test.CustomArranger
import com.ocadotechnology.gembus.test.some
import com.ocadotechnology.gembus.test.someString
import java.util.Date

class HunterPathXmlArranger : CustomArranger<HunterPathXml>() {

    override fun instance(): HunterPathXml {
        return HunterPathXml(
            routeName = someString(),
            locations = mutableListOf(some<AndroidLocationXml>()),
            chunkedCoordinates = mutableListOf(some<AveragedLocationXml>()),
            start = some<Date>(),
            end = some<Date>(),
            chunkStart = some<Date>()
        )
    }
}