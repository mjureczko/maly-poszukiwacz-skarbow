package pl.marianjureczko.poszukiwacz.model

import com.ocadotechnology.gembus.test.CustomArranger
import com.ocadotechnology.gembus.test.some
import com.ocadotechnology.gembus.test.somePositiveInt
import pl.marianjureczko.poszukiwacz.usecase.TestLocation
import java.util.Date

class HunterPathArranger : CustomArranger<HunterPath>() {
    override fun instance(): HunterPath {
        val start = some<Date>()
        val instance = HunterPath(
            routeName = some<String>(),
            locations = listOf(some<TestLocation>()),
            start = start,
            end = Date(start.time + somePositiveInt(2 * 60 * 60 * 1000)),
            chunkStart = Date(start.time + somePositiveInt(60 * 60 * 1000)),
            chunkedCoordinates = listOf()
        )
        return instance.addLocation(some<TestLocation>())
    }
}