package pl.marianjureczko.poszukiwacz

import android.app.Application
import org.jeasy.random.EasyRandom
import org.jeasy.random.EasyRandomParameters
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import java.io.File

class StorageHelperTest {

    private val parameters = EasyRandomParameters()
    private lateinit var easyRandom: EasyRandom
    private val context = TestContext()
    private val storageHelper = StorageHelper(context)

    @Before
    fun init() {
        parameters.overrideDefaultInitialization(true)
        easyRandom = EasyRandom(parameters)
    }

    @After
    fun cleanup() {
        File(context.filesDir.absolutePath + StorageHelper.treasuresDirectory).delete()
    }

    @Test
    fun `save and load treasures list`() {
        //given
        val someTreasures = easyRandom.nextObject(TreasuresList::class.java)

        //when
        storageHelper.save(someTreasures)
        val actual = storageHelper.loadAll()

        //then
        val matching = actual.first { it.name == someTreasures.name }
        assertEquals(someTreasures, matching)
    }

    @Test
    fun `save and remove treasures list`() {
        //given
        val someTreasures = easyRandom.nextObject(TreasuresList::class.java)
        storageHelper.save(someTreasures)

        //when
        storageHelper.remove(someTreasures)
        val actual = storageHelper.loadAll()

        //then
        val matching = actual.firstOrNull { it.name == someTreasures.name }
        assertNull(matching)
    }

    @Test
    fun `ignore invalid files when loading treasures`() {
        //given
        storageHelper.save(easyRandom.nextObject(TreasuresList::class.java))
        File(context.filesDir.absolutePath + StorageHelper.treasuresDirectory + "/invalid.file.xml")
            .writeText("it' not a xml")

        //when
        val actual = storageHelper.loadAll()

        //then
        assertEquals(1, actual.size)
    }
}

class TestContext : Application() {
    override fun getFilesDir(): File = File(".")
}