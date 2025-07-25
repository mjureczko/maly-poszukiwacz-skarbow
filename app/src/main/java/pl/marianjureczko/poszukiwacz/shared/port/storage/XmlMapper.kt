package pl.marianjureczko.poszukiwacz.shared.port.storage

import pl.marianjureczko.poszukiwacz.model.HunterPath

object XmlMapper {

    fun toXml(hunterPath: HunterPath): HunterPathXml {
        return HunterPathXml().apply {
            routeName = hunterPath.routeName
            locations = hunterPath.locations.toMutableList()
            start = hunterPath.start
            end = hunterPath.end
            chunkStart = hunterPath.chunkStart
            chunkedCoordinates = hunterPath.chunkedCoordinates.toMutableList()
        }
    }

    fun toEntity(xml: HunterPathXml): HunterPath {
        return HunterPath(
            routeName = xml.routeName,
            locations = xml.locations.toMutableList(),
            start = xml.start,
            end = xml.end,
            chunkStart = xml.chunkStart,
            chunkedCoordinates = xml.chunkedCoordinates.toMutableList()
        )
    }
}