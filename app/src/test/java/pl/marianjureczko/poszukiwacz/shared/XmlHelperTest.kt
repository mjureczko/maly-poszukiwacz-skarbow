package pl.marianjureczko.poszukiwacz.shared

import com.ocadotechnology.gembus.test.some
import org.junit.Assert.assertEquals
import org.junit.Test
import pl.marianjureczko.poszukiwacz.model.Route

class XmlHelperTest {

    @Test
    fun should_writeToAndLoadFromString() {
        //given
        val xmlHelper = XmlHelper()
        val route = some<Route>()

        //when
        val xml = xmlHelper.writeToString(route)
        val actual = xmlHelper.loadFromString(xml)

        //then
        assertEquals(route, actual)
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
        val actual = xmlHelper.loadFromString(xml)

        //then
        assertEquals(route, actual)
    }
}