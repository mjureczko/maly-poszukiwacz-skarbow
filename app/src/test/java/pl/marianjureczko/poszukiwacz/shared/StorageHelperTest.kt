package pl.marianjureczko.poszukiwacz.shared

import android.app.Application
import com.ocadotechnology.gembus.test.some
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.withinPercentage
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.RouteArranger
import java.io.ByteArrayInputStream
import java.io.File
import java.io.StringWriter
import java.nio.charset.StandardCharsets
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
        var actualRoute: Route? = null
        var actualFiles: MutableSet<String> = mutableSetOf()
        while (actualZip.nextEntry.also { zipEntry = it } != null) {
            if (zipEntry!!.name == route.name + ".xml") {
                val stringWriter = StringWriter()
                IOUtils.copy(actualZip, stringWriter, StandardCharsets.UTF_8)
                actualRoute = xmlHelper.loadFromString(stringWriter.toString())
            } else {
                actualFiles.add(zipEntry!!.name)
            }
        }
        actualZip.close()
        assertRoute(actualRoute!!, route)
        assertRouteFiles(actualFiles, route)
    }

    @Test
    fun `SHOULD save route with pictures and sounds WHEN loading a zip`() {
        //given
        val loggedRoutes: MutableList<String> = mutableListOf()
        val loggedFiles: MutableList<String> = mutableListOf()
        var progress: ExtractionProgress = object : ExtractionProgress {
            override fun routeExtracted(routeName: String) {
                loggedRoutes.add(routeName)
            }

            override fun fileExtracted(fileName: String) {
                loggedFiles.add(fileName)
            }
        }
        val routeName = "001"
        val inStream = ClassLoader.getSystemResourceAsStream("route.zip")
        val treasure2Photo = "photo_dc4dff0e-d133-4abc-a8a5-d97bfc922f6e.jpg"
        val treasure2Sound = "sound_c7cb62ea-909c-4612-ba44-3083467c054d.3gp"
        val treasure3Photo = "photo_1d165328-7c40-486b-b802-1a84c0efd9ab.jpg"
        FileUtils.cleanDirectory(File(storageHelper.pathToRoutesDir()))

        //when
        storageHelper.extractZipStream(inStream, progress)

        //then
        val dir = storageHelper.pathToRoutesDir() + "/";
        var routes = storageHelper.loadAll().filter { it.name == routeName }
        assertThat(routes).hasSize(1)
        val actualRoute = routes[0]
        assertThat(actualRoute.treasures).hasSize(2)
        assertThat(actualRoute.treasures).anySatisfy {
            assertThat(it.id).isEqualTo(2)
            assertThat(it.photoFileName).isEqualTo(dir + treasure2Photo)
            assertThat(it.tipFileName).isEqualTo(dir + treasure2Sound)
            assertThat(it.latitude).isCloseTo(51.25482, withinPercentage(0.1))
            assertThat(it.longitude).isCloseTo(16.9326, withinPercentage(0.1))
        }
        assertThat(actualRoute.treasures).anySatisfy {
            assertThat(it.id).isEqualTo(3)
            assertThat(it.photoFileName).isEqualTo(dir + treasure3Photo)
            assertThat(it.tipFileName).isNull()
            assertThat(it.latitude).isCloseTo(51.2502, withinPercentage(0.1))
            assertThat(it.longitude).isCloseTo(16.93156, withinPercentage(0.1))
        }
        assertThat(File("${storageHelper.pathToRoutesDir()}/$treasure2Photo").length()).isEqualTo(163850);
        assertThat(File("${storageHelper.pathToRoutesDir()}/$treasure2Sound").length()).isEqualTo(585221);
        assertThat(File("${storageHelper.pathToRoutesDir()}/$treasure3Photo").length()).isEqualTo(61781);
        assertThat(loggedRoutes).contains(routeName)
        assertThat(loggedFiles).contains(treasure2Photo)
        assertThat(loggedFiles).contains(treasure2Sound)
        assertThat(loggedFiles).contains(treasure3Photo)
    }

    private fun assertRoute(actual: Route, expected: Route) {
        assertThat(actual.name).isEqualTo(expected.name)
        assertThat(actual.treasures).hasSize(expected.treasures.size)
        expected.treasures.forEach {
            assertThat(actual.treasures).contains(
                it.copy(tipFileName = toRelative(it.tipFileName!!))
            )
        }
    }

    private fun toRelative(s: String) =
        s.substring(storageHelper.pathToRoutesDir().length + 1)

    private fun assertRouteFiles(actualFiles: MutableSet<String>, route: Route) {
        route.treasures.forEach {
            assertThat(actualFiles).contains(toRelative(it.tipFileName!!))
            assertThat(actualFiles).contains(it.photoFileName!!)
        }
    }
}

class TestContext : Application() {
    override fun getFilesDir(): File = File(".")
}