package pl.marianjureczko.poszukiwacz.shared.port.storage

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ocadotechnology.gembus.test.some
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import pl.marianjureczko.poszukiwacz.model.AveragedLocation

@RunWith(AndroidJUnit4::class)
class XmlMapperTest {
    @Test
    fun should_mapXmlToHunterPath() {
        // given
        val xml = some<HunterPathXml>()
            .copy(chunkedCoordinates = mutableListOf(some<AveragedLocationXml>()))

        //when
        val actual = XmlMapper.toEntity(xml)

        // then
        assertThat(actual.routeName).isEqualTo(xml.routeName)
        assertThat(actual.locations.map {
            AndroidLocationXml(
                latitude = it.latitude,
                longitude = it.longitude,
                accuracy = it.accuracy,
                observedAt = it.observedAt
            )
        })
            .containsExactlyElementsOf(xml.locations)
        assertThat(actual.start).isEqualTo(xml.start)
        assertThat(actual.end).isEqualTo(xml.end)
        assertThat(actual.chunkStart).isEqualTo(xml.chunkStart)
        val expectedChunks = xml.chunkedCoordinates
            .map { AveragedLocation(longitude = it.longitude, latitude = it.latitude) }
        assertThat(actual.chunkedCoordinates)
            .containsExactlyElementsOf(expectedChunks)
    }
}