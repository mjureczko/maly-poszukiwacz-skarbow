package pl.marianjureczko.poszukiwacz.shared

import android.app.Application
import com.ocadotechnology.gembus.test.some
import org.apache.commons.io.FileUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.RouteArranger
import java.io.File

class StorageHelperTest {

    private val context = TestContext()
    private val storageHelper = StorageHelper(context)

    @Before
    fun cleanup() {
        FileUtils.deleteDirectory(File(context.filesDir.absolutePath + StorageHelper.routesDirectory))
    }

    @Test
    fun `SHOULD save and load route`() {
        //given
        val route = some<Route>()

        //when
        storageHelper.save(route)
        val actual = storageHelper.loadAll()

        //then
        val matching = actual.first { it.name == route.name }
        assertEquals(route, matching)
    }

    @Test
    fun `SHOULD save and remove route`() {
        //given
        val route = some<Route>()
        storageHelper.save(route)

        //when
        storageHelper.remove(route)
        val actual = storageHelper.loadAll()

        //then
        val matching = actual.firstOrNull { it.name == route.name }
        assertNull(matching)
    }

    @Test
    fun `SHOULD ignore invalid files WHEN loading treasures`() {
        //given
        storageHelper.save(some<Route>())
        File(context.filesDir.absolutePath + StorageHelper.routesDirectory + "/invalid.file.xml")
            .writeText("it' not a xml")

        //when
        val actual = storageHelper.loadAll()

        //then
        assertEquals(1, actual.size)
    }

    @Test
    fun `SHOULD generate name for new sound file`() {
        //when
        val newSoundFile = storageHelper.newSoundFile()

        //then
        assertThat(newSoundFile).contains(".3gp")
        assertThat(newSoundFile).matches(".*/treasures_lists/sound_.+\\.3gp")
    }

    @Test
    fun `SHOULD generate name for new photo file`() {
        //when
        val newPhotoFile = storageHelper.newPhotoFile()

        //then
        assertThat(newPhotoFile).contains(".jpg")
        assertThat(newPhotoFile).matches(".*/treasures_lists/photo_.+\\.jpg")
    }

    @Test
    fun `SHOULD remove tip files WHEN removing route`() {
        //given
        val route = RouteArranger.savedWithFiles(storageHelper)

        //when
        storageHelper.remove(route)

        //then
        route.treasures.forEach { actual ->
            assertThat(File(actual.tipFileName).exists())
                .`as`("Tip file should be removed")
                .isFalse()
            assertThat(File(actual.photoFileName).exists())
                .`as`("Photo file should be removed")
                .isFalse()
        }
    }

    @Test
    fun `SHOULD recognize route WHEN the route already exists`() {
        //given
        val route = some<Route>()
        storageHelper.save(route)

        //then
        assertTrue(storageHelper.routeAlreadyExists(route))
    }

    @Test
    fun `SHOULD recognize new route`() {
        //given
        val route = some<Route>()

        //then
        assertFalse(storageHelper.routeAlreadyExists(route))
    }

    @Test
    fun `SHOULD remove tip files WHEN removing route by name`() {
        //given
        val route = RouteArranger.savedWithFiles(storageHelper)

        //when
        storageHelper.removeRouteByName(route.name)

        //then
        route.treasures.forEach { actual ->
            assertThat(File(actual.tipFileName).exists())
                .`as`("Tip file should be removed")
                .isFalse()
            assertThat(File(actual.photoFileName).exists())
                .`as`("Photo file should be removed")
                .isFalse()
        }
    }
}

class TestContext : Application() {
    override fun getFilesDir(): File = File(".")
}