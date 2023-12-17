package pl.marianjureczko.poszukiwacz.shared

import com.ocadotechnology.gembus.test.someText
import org.apache.commons.io.FileUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File

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

    @Test
    fun `SHOULD move commemorative file`() {
        //given
        val context = TestContext()
        val storageHelper = StorageHelper(context)
        val fileContent = someText()
        val tempFile = PhotoHelper(context, storageHelper).getCommemorativePhotoTempFile()
        FileUtils.copyToFile(fileContent.byteInputStream(), tempFile)

        //when
        val newLocation = PhotoHelper(context, storageHelper).moveCommemorativePhotoToPermanentLocation()

        //then
        val actualContent = FileUtils.readFileToString(File(newLocation))
        assertThat(actualContent).isEqualTo(fileContent);

    }
}