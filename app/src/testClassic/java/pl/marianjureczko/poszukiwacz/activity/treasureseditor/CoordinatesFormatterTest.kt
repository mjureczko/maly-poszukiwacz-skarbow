package pl.marianjureczko.poszukiwacz.activity.treasureseditor

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CoordinatesFormatterTest {

    @Test
    fun formatCorrectCord() {
        //when
        val actual = pl.marianjureczko.poszukiwacz.screen.treasureseditor.CoordinatesFormatter().format(2.5433)

        //then
        assertEquals("02.54330", actual)
    }

    @Test
    fun formatToBigCord() {
        //when
        val actual = pl.marianjureczko.poszukiwacz.screen.treasureseditor.CoordinatesFormatter().format(122.3365645)

        //then
        assertEquals("22.33656", actual)
    }

    @Test
    fun formatNull() {
        //when
        val actual = pl.marianjureczko.poszukiwacz.screen.treasureseditor.CoordinatesFormatter().format(null)

        //then
        assertEquals("??.?????", actual)
    }

}