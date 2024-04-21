package pl.marianjureczko.poszukiwacz.model

import com.ocadotechnology.gembus.test.somePositiveInt
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

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

    @Test
    fun parseKnowledge() {
        //given
        val type = "k"
        val quantity = somePositiveInt(9)
        val id = "a11"

        //when
        val actual = TreasureParser().parse("${type}9$quantity$id")

        //then
        assertEquals(TreasureType.KNOWLEDGE, actual.type)
        assertEquals(1, actual.quantity)
        assertEquals(id, actual.id)
    }

    @Test
    fun parseInvalidTreasureType() {
        assertThatThrownBy { TreasureParser().parse("x01id") }
            .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun parseInvalidQuantityType() {
        assertThatThrownBy { TreasureParser().parse("d0aid") }
            .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun parseMissingId() {
        val actual = TreasureParser().parse("d01")
        assertEquals("", actual.id)
    }
}