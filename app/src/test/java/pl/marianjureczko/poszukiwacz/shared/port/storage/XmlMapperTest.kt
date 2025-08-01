package pl.marianjureczko.poszukiwacz.shared.port.storage

import com.ocadotechnology.gembus.test.some
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import pl.marianjureczko.poszukiwacz.model.HunterPath

class XmlMapperTest {
    @Test
    fun shouldMapHunterPathToXml() {
        // given
        val hunterPath = some<HunterPath>()

        // when
        val actual = XmlMapper.toXml(hunterPath)

        // then
        assertThat(actual.routeName).isEqualTo(hunterPath.routeName)
        assertThat(actual.locations).containsExactlyElementsOf(hunterPath.locations.map {
            AndroidLocationXml(
                latitude = it.latitude,
                longitude = it.longitude,
                accuracy = it.accuracy,
                observedAt = it.observedAt
            )
        })
        assertThat(actual.start).isEqualTo(hunterPath.start)
        assertThat(actual.end).isEqualTo(hunterPath.end)
        assertThat(actual.chunkStart).isEqualTo(hunterPath.chunkStart)
        assertThat(actual.chunkedCoordinates.map { it.toString() })
            .containsExactlyElementsOf(hunterPath.chunkedCoordinates.map { it.toString() })
    }
}