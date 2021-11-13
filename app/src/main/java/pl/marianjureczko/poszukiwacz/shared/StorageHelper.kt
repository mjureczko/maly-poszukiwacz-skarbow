package pl.marianjureczko.poszukiwacz.shared

import android.content.Context
import org.apache.commons.io.IOUtils
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import java.io.*
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

interface ExtractionProgress {
    fun routeExtracted(routeName: String)
    fun fileExtracted(fileName: String)
}

open class StorageHelper(val context: Context) {

    private val xmlHelper = XmlHelper()

    companion object {
        const val routesDirectory = "/treasures_lists"
    }

    fun newSoundFile(): String = newFile("sound_", ".3gp")

    open fun newPhotoFile(): String = newFile("photo_", ".jpg")

    fun save(route: Route) {
        val xmlFile = getRouteFile(route)
        xmlHelper.writeToFile(route, xmlFile)
    }

    /** The route should be already saved */
    fun routeToZipOutputStream(route: Route): ByteArrayOutputStream {
        val outputStream = ByteArrayOutputStream()
        val zipOut = ZipOutputStream(outputStream)

        val routeString = xmlHelper.writeToString(pathsToRelative(route))

        val zipEntry = ZipEntry(route.name + ".xml")
        zipOut.putNextEntry(zipEntry)
        zipOut.write(routeString.toByteArray())
        zipOut.closeEntry()
        route.treasures.forEach {
            addFileToZipStream(zipOut, it.photoFileName)
            addFileToZipStream(zipOut, it.tipFileName)
        }

        zipOut.close()
        return outputStream
    }

    fun extractZipStream(inStream: InputStream, progress: ExtractionProgress) {
        val actualZip = ZipInputStream(inStream)
        var zipEntry: ZipEntry?
        var routesDir = this.getRoutesDir().absolutePath
        if (!routesDir.endsWith("/")) {
            routesDir = "$routesDir/"
        }
        while (actualZip.nextEntry.also { zipEntry = it } != null) {
            if (zipEntry!!.name.endsWith(".xml")) {
                val route = extractRoute(actualZip, routesDir)
                progress.routeExtracted(route.name)
            } else {
                extractFile(zipEntry!!, actualZip, routesDir)
                progress.fileExtracted(zipEntry!!.name)
            }
        }
        actualZip.close()
    }

    private fun extractFile(zipEntry: ZipEntry, actualZip: ZipInputStream, routesDir: String) {
        FileOutputStream("${routesDir}${zipEntry.name}").use {
            IOUtils.copy(actualZip, it)
            actualZip.closeEntry()
        }
    }

    private fun extractRoute(actualZip: ZipInputStream, routesDir: String): Route {
        val stringWriter = StringWriter()
        IOUtils.copy(actualZip, stringWriter, StandardCharsets.UTF_8)
        val routeXml = stringWriter.toString()
        var route = xmlHelper.loadFromString(routeXml)
        route.addPrefixToFilesPaths(routesDir)
        save(route)
        actualZip.closeEntry()
        return route
    }

    private fun addFileToZipStream(zipOut: ZipOutputStream, fileName: String?) {
        if (fileName != null) {
            val zipEntry = ZipEntry(toRelativePath(fileName))
            zipOut.putNextEntry(zipEntry)
            zipOut.write(File(fileName).readBytes())
            zipOut.closeEntry()
        }
    }

    fun routeAlreadyExists(route: Route): Boolean =
        getRouteFile(route).exists()


    fun loadAll(): MutableList<Route> {
        val dir = getRoutesDir()
        return dir.listFiles().mapNotNull {
            try {
                xmlHelper.loadFromFile(it)
            } catch (e: Exception) {
                null
            }
        }.toMutableList()
    }

    fun remove(toRemove: Route) {
        toRemove.treasures.forEach {
            removeTipFiles(it)
        }
        val fileToRemove = getRouteFile(toRemove)
        fileToRemove.delete()
    }

    fun removeTipFiles(treasureDescription: TreasureDescription) {
        if (treasureDescription.tipFileName != null) {
            File(treasureDescription.tipFileName).delete()
        }
        if (treasureDescription.hasPhoto()) {
            File(treasureDescription.photoFileName).delete()
        }
    }

    fun removeRouteByName(name: String) {
        loadAll()
            .filter { it -> it.name == name }
            .forEach { it -> remove(it) }
    }

    fun pathToRoutesDir(): String = getRoutesDir().absolutePath

    private fun newFile(prefix: String, extension: String) =
        getRoutesDir().absolutePath + File.separator + prefix + UUID.randomUUID().toString() + extension

    private fun getRouteFile(route: Route): File {
        val dir = getRoutesDir()
        return File("${dir.absolutePath}/${route.fileName()}.xml")
    }

    private fun getRoutesDir(): File {
        val dir = File(context.filesDir.absolutePath + routesDirectory)
        if (!dir.exists()) {
            dir.mkdir()
        }
        return dir
    }

    private fun pathsToRelative(route: Route): Route {
        val relativeTreasures = route.treasures
            .map { it.copy(tipFileName = toRelativePath(it.tipFileName), photoFileName = toRelativePath(it.photoFileName)) }
            .toMutableList()
        return route.copy(treasures = relativeTreasures)
    }

    private fun toRelativePath(fileName: String?): String? {
        return fileName?.let {
            return if (it.startsWith(pathToRoutesDir())) {
                var cutSize = pathToRoutesDir().length
                if (it[cutSize] == '/') {
                    cutSize++
                }
                return it.substring(cutSize)
            } else {
                it
            }
        }
    }

}

