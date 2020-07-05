package pl.marianjureczko.poszukiwacz

import org.jeasy.random.EasyRandom
import org.jeasy.random.EasyRandomParameters
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class XmlHelperTest {

    private val parameters = EasyRandomParameters()
    private lateinit var easyRandom: EasyRandom

    @Before
    fun init() {
        parameters.overrideDefaultInitialization(true)
        easyRandom = EasyRandom(parameters)
    }

    @Test
    fun should_writeToAndLoadFromString() {
        //given
        val xmlHelper = XmlHelper()
        val treasures = easyRandom.nextObject(TreasuresList::class.java)

        //when
        val xml = xmlHelper.writeToString(treasures)
        val actual = xmlHelper.loadFromString(xml)

        //then
        assertEquals(treasures, actual)
    }

    @Test
    fun should_writeToAndLoadFromString_when_tipIsNull() {
        //given
        val xmlHelper = XmlHelper()
        var treasures = easyRandom.nextObject(TreasuresList::class.java)
        val treasureDescriptions = ArrayList(treasures.treasures.map { it -> it.tipFileName = null; it; })
        treasures = treasures.copy(treasures = treasureDescriptions)

        //when
        val xml = xmlHelper.writeToString(treasures)
        val actual = xmlHelper.loadFromString(xml)

        //then
        assertEquals(treasures, actual)
    }
}