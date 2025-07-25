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
        assertThat(actual.locations).containsExactlyElementsOf(hunterPath.locations)
        assertThat(actual.start).isEqualTo(hunterPath.start)
        assertThat(actual.end).isEqualTo(hunterPath.end)
        assertThat(actual.chunkStart).isEqualTo(hunterPath.chunkStart)
        assertThat(actual.chunkedCoordinates).containsExactlyElementsOf(hunterPath.chunkedCoordinates)
    }

    @Test
    fun shouldMapXmlToHunterPath() {
        // given
        val xml = some<HunterPathXml>()

        //when
        val actual = XmlMapper.toEntity(xml)

        // then
        assertThat(actual.routeName).isEqualTo(xml.routeName)
        assertThat(actual.locations).containsExactlyElementsOf(xml.locations)
        assertThat(actual.start).isEqualTo(xml.start)
        assertThat(actual.end).isEqualTo(xml.end)
        assertThat(actual.chunkStart).isEqualTo(xml.chunkStart)
        assertThat(actual.chunkedCoordinates).containsExactlyElementsOf(xml.chunkedCoordinates)
    }
}