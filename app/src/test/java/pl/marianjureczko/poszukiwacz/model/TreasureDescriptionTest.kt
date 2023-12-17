package pl.marianjureczko.poszukiwacz.model

import com.ocadotechnology.gembus.test.some
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import pl.marianjureczko.poszukiwacz.shared.StorageHelper

//FIXME: clean files created during tests
@ExtendWith(MockitoExtension::class)
class TreasureDescriptionTest {

    @Mock
    lateinit var storageHelper: StorageHelper

    @Test
    fun prettyName() {
        //given
        val given = some<TreasureDescription>()

        //when
        val actual = given.prettyName()

        //then
        assertThat(actual).isEqualTo("[${given.id}] ${given.latitude} ${given.longitude}")
    }

    @Test
    fun instantiatePhotoFile_when_thereIsNoPhotoFileInTheDescription() {
        //given
        val given = some<TreasureDescription>().copy(photoFileName = null)
        val photoFile = some<String>()
        given(storageHelper.newPhotoFile()).willReturn(photoFile)

        //when
        val actual = given.instantiatePhotoFile(storageHelper)

        //then
        assertThat(actual.name).isEqualTo(photoFile)
        assertThat(given.photoFileName).isEqualTo(photoFile)
    }

    @Test
    fun instantiatePhotoFile_when_thereIsPhotoFileInTheDescription() {
        //given
        val given = some<TreasureDescription>()
        val photoFile = given.photoFileName

        //when
        val actual = given.instantiatePhotoFile(storageHelper)

        //then
        assertThat(actual.name).isEqualTo(photoFile)
        assertThat(given.photoFileName).isEqualTo(photoFile)
    }

    @Test
    fun hasPhotoFalse_when_theFileIsMissing() {
        //given
        val given = some<TreasureDescription>()

        //then
        assertFalse(given.hasPhoto())
    }

    @Test
    fun hasPhotoFalse_when_tipFileNameIsNull() {
        //given
        val given = some<TreasureDescription>().copy(tipFileName = null)

        //then
        assertFalse(given.hasPhoto())
    }

    @Test
    fun hasPhotoTrue_when_theFileExists() {
        //given
        val given = some<TreasureDescription>()
        try {

            //when
            given.instantiatePhotoFile(storageHelper).createNewFile()

            //then
            assertTrue(given.hasPhoto())
        } finally {
            given.instantiatePhotoFile(storageHelper).delete()
        }
    }
}