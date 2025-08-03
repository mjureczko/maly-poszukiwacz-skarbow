package pl.marianjureczko.poszukiwacz.shared

import com.ocadotechnology.gembus.test.someText
import org.apache.commons.io.FileUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import pl.marianjureczko.poszukiwacz.shared.port.storage.StoragePort
import java.io.File

class PhotoHelperTest  {

    @Test
    fun `SHOULD move commemorative file`() {
        //given
        val context = TestContext()
        val storagePort = StoragePort(context)
        val fileContent = someText()
        val tempFile = PhotoHelper(context, storagePort).getCommemorativePhotoTempFile()
        FileUtils.copyToFile(fileContent.byteInputStream(), tempFile)

        //when
        val newLocation = PhotoHelper(context, storagePort).moveCommemorativePhotoToPermanentLocation()

        //then
        val actualContent = FileUtils.readFileToString(File(newLocation))
        assertThat(actualContent).isEqualTo(fileContent);

    }
}