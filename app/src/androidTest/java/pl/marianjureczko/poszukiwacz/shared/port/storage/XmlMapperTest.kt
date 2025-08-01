package pl.marianjureczko.poszukiwacz.shared.port.storage

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ocadotechnology.gembus.test.some
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class XmlMapperTest {
    @Test
    fun should_mapXmlToHunterPath() {
        // given
        val xml = some<HunterPathXml>()

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
        assertThat(actual.chunkedCoordinates.map { it.toString() })
            .containsExactlyElementsOf(xml.chunkedCoordinates.map { it.toString() })
    }
}