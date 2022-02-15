package pl.marianjureczko.poszukiwacz.activity.treasureseditor

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PhotoHelperTest  {

    @Test
    fun `SHOULD scale height to ~ 800px WHEN height is greater than weight`() {
        //given

        //when
        val actual = PhotoHelper.calculateScalingFactor(900, 1000)

        //then
        assertThat(actual).isEqualTo(0.8f)
    }

    @Test
    fun `SHOULD scale weight to ~ 800px WHEN weight is greater than height`() {
        //given

        //when
        val actual = PhotoHelper.calculateScalingFactor(950, 1000)

        //then
        assertThat(actual).isEqualTo(0.8f)
    }
}