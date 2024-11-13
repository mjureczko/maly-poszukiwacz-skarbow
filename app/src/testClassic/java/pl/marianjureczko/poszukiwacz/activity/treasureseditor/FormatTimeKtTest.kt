package pl.marianjureczko.poszukiwacz.activity.treasureseditor

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class FormatTimeKtTest {
    @Test
    fun shouldReturnZeros_whenTimeIsNull() {
        //when
        val actual = formatTime(null)

        //then
        assertThat(actual).isEqualTo("00:00")
    }

    @Test
    fun shouldReturnFormattedTime() {
        //when
        val actual = formatTime(3601_001)

        //then
        assertThat(actual).isEqualTo("60:01")
    }

    @Test
    fun shouldReturnFormattedTime_whenMinutesAbove100() {
        //when
        val actual = formatTime(7201_999)

        //then
        assertThat(actual).isEqualTo("120:01")
    }
}