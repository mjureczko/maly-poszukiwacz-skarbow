package pl.marianjureczko.poszukiwacz.screen.treasureseditor

import com.ocadotechnology.gembus.test.some
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TreasureEditorStateTest {
    @Test
    fun shouldDisableButtonAndHideLocation_whenCoordinatesAreNull() {
        //given
        val state = some<TreasureEditorState>().copy(currentLocation = null)

        //when
        val actual = state.locationBarData()

        //then
        assertThat(actual.buttonEnabled).isFalse()
        assertThat(actual.latitude).isEqualTo("??.?????")
        assertThat(actual.longitude).isEqualTo("??.?????")
    }

    @Test
    fun shouldEnableButtonAndFormatCoordinates_whenCoordinatesArePresent() {
        //given
        val state = some<TreasureEditorState>()

        //when
        val actual = state.locationBarData()

        //then
        assertThat(actual.buttonEnabled).isTrue()
        assertThat(actual.latitude).isEqualTo(CoordinatesFormatter().format(state.currentLocation!!.latitude))
        assertThat(actual.longitude).isEqualTo(CoordinatesFormatter().format(state.currentLocation!!.longitude))
    }
}