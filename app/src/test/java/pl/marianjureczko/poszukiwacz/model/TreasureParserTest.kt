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
        val code = "$type$quantity$id"

        //when
        val actual = TreasureParser().parse(code)

        //then
        assertEquals(TreasureType.DIAMOND, actual.type)
        assertEquals(quantity, actual.quantity)
        assertEquals(code, actual.id)
    }

    @Test
    fun parseGold() {
        //given
        val type = "g"
        val quantity = 97
        val id = "abf"
        val code = "$type$quantity$id"

        //when
        val actual = TreasureParser().parse(code)

        //then
        assertEquals(TreasureType.GOLD, actual.type)
        assertEquals(quantity, actual.quantity)
        assertEquals(code, actual.id)
    }

    @Test
    fun parseRuby() {
        //given
        val type = "r"
        val quantity = 1
        val id = "iiii"
        val code = "${type}0$quantity$id"

        //when
        val actual = TreasureParser().parse(code)

        //then
        assertEquals(TreasureType.RUBY, actual.type)
        assertEquals(quantity, actual.quantity)
        assertEquals(code, actual.id)
    }

    @Test
    fun parseKnowledge() {
        //given
        val type = "k"
        val quantity = somePositiveInt(9)
        val id = "a11"
        val code = "${type}9$quantity$id"

        //when
        val actual = TreasureParser().parse(code)

        //then
        assertEquals(TreasureType.KNOWLEDGE, actual.type)
        assertEquals(1, actual.quantity)
        assertEquals(code, actual.id)
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
        val code = "d01"
        val actual = TreasureParser().parse(code)
        assertEquals(code, actual.id)
    }
}