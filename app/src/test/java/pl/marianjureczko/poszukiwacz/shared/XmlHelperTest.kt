package pl.marianjureczko.poszukiwacz.shared

import com.ocadotechnology.gembus.test.some
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureBag
import pl.marianjureczko.poszukiwacz.model.TreasureDescription

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
        var bag = some<TreasureBag>()

        //when
        val xml = xmlHelper.writeToString(bag)
        val actual = xmlHelper.loadFromString<TreasureBag>(xml)

        //then
        assertThat(actual).usingRecursiveComparison().isEqualTo(bag)
    }

    @Test
    fun should_writeToAndLoadFromStringTreasureBagWithoutSelectedTreasure() {
        //given
        val xmlHelper = XmlHelper()
        var bag = some<TreasureBag> {
            selectedTreasure = null
        }

        //when
        val xml = xmlHelper.writeToString(bag)
        val actual = xmlHelper.loadFromString<TreasureBag>(xml)

        //then
        assertThat(actual).usingRecursiveComparison().isEqualTo(bag)
    }
}