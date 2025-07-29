package pl.marianjureczko.poszukiwacz.shared.port.storage

import pl.marianjureczko.poszukiwacz.model.AveragedLocation
import pl.marianjureczko.poszukiwacz.model.HunterPath
import pl.marianjureczko.poszukiwacz.shared.port.LocationWrapper

object XmlMapper {

    fun toXml(hunterPath: HunterPath): HunterPathXml {
        return HunterPathXml().apply {
            routeName = hunterPath.routeName
            locations = hunterPath.locations.map { AndroidLocationXml(it) }.toMutableList()
            start = hunterPath.start
            end = hunterPath.end
            chunkStart = hunterPath.chunkStart
            chunkedCoordinates = hunterPath.chunkedCoordinates
                .map { AveragedLocationXml(latitude = it.latitude, longitude = it.longitude) }
                .toMutableList()
        }
    }

    fun toEntity(xml: HunterPathXml): HunterPath {
        return HunterPath(
            routeName = xml.routeName,
            locations = xml.locations
                .map {
                    LocationWrapper(
                        latitude = it.latitude,
                        longitude = it.longitude,
                        accuracy = it.accuracy,
                        observedAt = it.observedAt
                    )
                }
                .toMutableList(),
            start = xml.start,
            end = xml.end,
            chunkStart = xml.chunkStart,
            chunkedCoordinates = xml.chunkedCoordinates
                .map { AveragedLocation(latitude = it.latitude, longitude = it.longitude) }
                .toMutableList()
        )
    }
}