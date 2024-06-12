package pl.marianjureczko.poszukiwacz.shared

import com.ocadotechnology.gembus.test.some
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import pl.marianjureczko.poszukiwacz.model.HunterPath
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress

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
        val path = some<HunterPath>()

        //when
        val xml = xmlHelper.writeToString(path)
        val actual = xmlHelper.loadFromString<HunterPath>(xml)

        //then
        assertThat(actual).usingRecursiveComparison().isEqualTo(path)
    }
}