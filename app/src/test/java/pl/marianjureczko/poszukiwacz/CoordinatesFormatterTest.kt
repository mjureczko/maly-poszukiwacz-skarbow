package pl.marianjureczko.poszukiwacz

import org.junit.Assert.assertEquals
import org.junit.Test
import pl.marianjureczko.poszukiwacz.activity.treasureseditor.CoordinatesFormatter

class CoordinatesFormatterTest {

    @Test
    fun formatCorrectCord() {
        //when
        val actual = CoordinatesFormatter().format(2.5433)

        //then
        assertEquals("02.54330", actual)
    }

    @Test
    fun formatToBigCord() {
        //when
        val actual = CoordinatesFormatter().format(122.3365645)

        //then
        assertEquals("22.33656", actual)
    }

    @Test
    fun formatNull() {
        //when
        val actual = CoordinatesFormatter().format(null)

        //then
        assertEquals("??.?????", actual)
    }

}