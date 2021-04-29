package pl.marianjureczko.poszukiwacz.model

import com.ocadotechnology.gembus.test.some
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import pl.marianjureczko.poszukiwacz.shared.StorageHelper

@RunWith(MockitoJUnitRunner::class)
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
    fun createPhotoFile_when_thereIsNoPhotoFileInTheDescription() {
        //given
        val given = some<TreasureDescription>().copy(photoFileName = null)
        val photoFile = some<String>()
        Mockito.`when`(storageHelper.newPhotoFile()).thenReturn(photoFile)

        //when
        val actual = given.createPhotoFile(storageHelper)

        //then
        assertThat(actual.name).isEqualTo(photoFile)
        assertThat(given.photoFileName).isEqualTo(photoFile)
    }

    @Test
    fun createPhotoFile_when_thereIsPhotoFileInTheDescription() {
        //given
        val given = some<TreasureDescription>()
        val photoFile = given.photoFileName

        //when
        val actual = given.createPhotoFile(storageHelper)

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
            given.createPhotoFile(storageHelper).createNewFile()

            //then
            assertTrue(given.hasPhoto())
        } finally {
            given.createPhotoFile(storageHelper).delete()
        }
    }
}