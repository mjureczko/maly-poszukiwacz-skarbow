package pl.marianjureczko.poszukiwacz.shared

import com.ocadotechnology.gembus.test.some
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
import pl.marianjureczko.poszukiwacz.shared.port.storage.HunterPathXml
import pl.marianjureczko.poszukiwacz.shared.port.storage.XmlHelper
import java.time.Instant
import java.util.Date

class XmlHelperTest {

    @Test
    fun should_writeToAndLoadFromStringRoute() {
        //given
        val xmlHelper = XmlHelper()
        val route = some<Route>()

        //when
        val xml = xmlHelper.writeToString(route)
        val actual = xmlHelper.loadFromString<Route>(xml)

        //then
        assertEquals(route, actual)
    }

    @Test
    fun should_writeToAndLoadFromStringTreasureDescription() {
        //given
        val xmlHelper = XmlHelper()
        val treasureDescription = some<TreasureDescription>()

        //when
        val xml = xmlHelper.writeToString(treasureDescription)
        val actual = xmlHelper.loadFromString<TreasureDescription>(xml)

        //then
        assertEquals(treasureDescription, actual)
    }

    @Test
    fun should_writeToAndLoadFromString_when_tipIsNull() {
        //given
        val xmlHelper = XmlHelper()
        var route = some<Route>()
        val treasureDescriptions = ArrayList(route.treasures.map { it -> it.tipFileName = null; it; })
        route = route.copy(treasures = treasureDescriptions)

        //when
        val xml = xmlHelper.writeToString(route)
        val actual = xmlHelper.loadFromString<Route>(xml)

        //then
        assertEquals(route, actual)
    }

    @Test
    fun should_writeToAndLoadFromStringTreasureBag() {
        //given
        val xmlHelper = XmlHelper()
        var bag = some<TreasuresProgress>()

        //when
        val xml = xmlHelper.writeToString(bag)
        val actual = xmlHelper.loadFromString<TreasuresProgress>(xml)

        //then
        assertThat(actual).usingRecursiveComparison().isEqualTo(bag)
    }

    @Test
    fun should_writeToAndLoadFromStringHunterPath() {
        //given
        val xmlHelper = XmlHelper()
        val path = some<HunterPathXml>()

        //when
        val xml = xmlHelper.writeToString(path)
        val actual = xmlHelper.loadFromString<HunterPathXml>(xml)

        //then
        assertThat(actual).usingRecursiveComparison().isEqualTo(path)
    }

    @Test
    fun should_loadPreviousVersionXmlToHunterPath() {
        //given
        val xmlPath = """<hunterPath>
   <chunkStart>2025-08-01 18:44:01.2 GMT+02:00</chunkStart>
   <chunkedCoordinates class="java.util.ArrayList">
      <hunterLocation>
         <latitude>37.4220936</latitude>
         <longitude>-122.083922</longitude>
      </hunterLocation>
   </chunkedCoordinates>
   <end>2025-08-01 18:44:01.2 GMT+02:00</end>
   <locations class="java.util.ArrayList">
      <hunterLocation>
         <latitude>37.4220936</latitude>
         <longitude>-122.083922</longitude>
      </hunterLocation>
   </locations>
   <routeName>custom</routeName>
   <start>2025-08-01 18:42:52.1 GMT+02:00</start>
</hunterPath>""".trimIndent()
        val xmlHelper = XmlHelper()

        //when
        val actual = xmlHelper.loadFromString<HunterPathXml>(xmlPath)

        //then
        assertThat(actual.routeName).isEqualTo("custom")
        assertThat(actual.locations).hasSize(1)
            .allSatisfy {
                assertThat(it.latitude).isEqualTo(37.4220936)
                assertThat(it.longitude).isEqualTo(-122.083922)
            }
        assertThat(actual.start).isEqualTo(Date.from(Instant.parse("2025-08-01T16:42:52.001Z")))
        assertThat(actual.end).isEqualTo(Date.from(Instant.parse("2025-08-01T16:44:01.002Z")))
        assertThat(actual.chunkStart).isEqualTo(Date.from(Instant.parse("2025-08-01T16:44:01.002Z")))
        assertThat(actual.chunkedCoordinates).hasSize(1)
            .allSatisfy {
                assertThat(it.latitude).isEqualTo(37.4220936)
                assertThat(it.longitude).isEqualTo(-122.083922)
            }
    }
}