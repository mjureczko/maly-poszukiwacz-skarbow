package pl.marianjureczko.poszukiwacz.screen.treasureseditor

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CoordinatesFormatterTest {

    @Test
    fun formatCorrectCoordinates() {
        //when
        val actual = CoordinatesFormatter().format(2.5433)

        //then
        assertEquals("2.54330", actual)
    }

    @Test
    fun formatToBigCoordinates() {
        //when
        val actual = CoordinatesFormatter().format(122.3365645)

        //then
        assertEquals("122.33656", actual)
    }

    @Test
    fun formatToNegativeCoordinates() {
        //when
        val actual = CoordinatesFormatter().format(-155.9365645)

        //then
        assertEquals("-155.93656", actual)
    }

    @Test
    fun formatNull() {
        //when
        val actual = CoordinatesFormatter().format(null)

        //then
        assertEquals("??.?????", actual)
    }

}