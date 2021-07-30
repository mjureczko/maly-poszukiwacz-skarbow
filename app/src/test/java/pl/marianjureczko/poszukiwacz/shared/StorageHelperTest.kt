package pl.marianjureczko.poszukiwacz.shared

import android.app.Application
import com.ocadotechnology.gembus.test.some
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.RouteArranger
import java.io.ByteArrayInputStream
import java.io.File
import java.io.StringWriter
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream


class StorageHelperTest {

    private val context = TestContext()
    private val storageHelper = StorageHelper(context)
    private val xmlHelper = XmlHelper()

    @Before
    fun cleanup() {
        FileUtils.deleteDirectory(File(context.filesDir.absolutePath + StorageHelper.routesDirectory))
    }

    @Test
    fun `SHOULD save and load route`() {
        //given
        val randomUUID = UUID.randomUUID()
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

    @Test
    fun `SHOULD convert route to a zip stream WHEN the route is already saved`() {
        //given
        val route = RouteArranger.savedWithFiles(storageHelper)

        //when
        val actual = storageHelper.routeToZipOutputStream(route)
        val actualZip = ZipInputStream(ByteArrayInputStream(actual.toByteArray()))

        //then
        var zipEntry: ZipEntry?
        var checked = false
        while (actualZip.nextEntry.also { zipEntry = it } != null) {
            assertThat(zipEntry!!.name).isEqualTo(route.name + ".xml")
            val stringWriter = StringWriter()
            IOUtils.copy(actualZip, stringWriter, StandardCharsets.UTF_8)
            assertThat(stringWriter.toString()).isEqualTo(xmlHelper.writeToString(route))
            checked = true
        }
        actualZip.close()
        assertTrue("no entries in zip", checked)
    }
}

class TestContext : Application() {
    override fun getFilesDir(): File = File(".")
}