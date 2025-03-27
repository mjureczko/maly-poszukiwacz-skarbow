package pl.marianjureczko.poszukiwacz.shared

import android.graphics.Matrix
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.then
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class PhotoScalingHelperTest {

    @Mock
    private lateinit var matrix: Matrix

    @Test
    fun `SHOULD scale width to ~ 800px WHEN width is greater than height`() {
        //when
        val actual = PhotoScalingHelper.createScalingMatrix(950, 1000, matrix)

        //then
        assertThat(actual).isSameAs(matrix)
        val expected = 0.8f
        then(matrix).should(times(1)).postScale(expected, expected)
    }

    @Test
    fun `SHOULD scale height to ~ 800px WHEN height is greater than width`() {
        //when
        val actual = PhotoScalingHelper.createScalingMatrix(1000, 900, matrix)

        //then
        assertThat(actual).isSameAs(matrix)
        val expected = 0.8f
        then(matrix).should(times(1)).postScale(expected, expected)
    }

    @Test
    fun `SHOULD scale to ~ 800px WHEN image is large`() {
        //when
        val actual = PhotoScalingHelper.createScalingMatrix(4000, 4000, matrix)

        //then
        assertThat(actual).isSameAs(matrix)
        val expected = 0.2f
        then(matrix).should(times(1)).postScale(expected, expected)
    }
}