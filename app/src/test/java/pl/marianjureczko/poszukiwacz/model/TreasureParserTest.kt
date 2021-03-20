package pl.marianjureczko.poszukiwacz.model

import org.junit.Assert.assertEquals
import org.junit.Test
import pl.marianjureczko.poszukiwacz.model.TreasureType

class TreasureParserTest {

    @Test
    fun parseDiamond() {
        //given
        val type = "d"
        val quantity = 17
        val id = "234"

        //when
        val actual = TreasureParser().parse("$type$quantity$id")

        //then
        assertEquals(TreasureType.DIAMOND, actual.type)
        assertEquals(quantity, actual.quantity)
        assertEquals(id, actual.id)
    }

    @Test
    fun parseGold() {
        //given
        val type = "g"
        val quantity = 97
        val id = "abf"

        //when
        val actual = TreasureParser().parse("$type$quantity$id")

        //then
        assertEquals(TreasureType.GOLD, actual.type)
        assertEquals(quantity, actual.quantity)
        assertEquals(id, actual.id)
    }

    @Test
    fun parseRuby() {
        //given
        val type = "r"
        val quantity = 1
        val id = "iiii"

        //when
        val actual = TreasureParser().parse("${type}0$quantity$id")

        //then
        assertEquals(TreasureType.RUBY, actual.type)
        assertEquals(quantity, actual.quantity)
        assertEquals(id, actual.id)
    }

    @Test(expected = IllegalArgumentException::class)
    fun parseInvalidTreasureType() {
        TreasureParser().parse("x01id")
    }

    @Test(expected = IllegalArgumentException::class)
    fun parseInvalidQuantityType() {
        TreasureParser().parse("d0aid")
    }

    @Test
    fun parseMissingId() {
        val actual = TreasureParser().parse("d01")
        assertEquals("", actual.id)
    }
}