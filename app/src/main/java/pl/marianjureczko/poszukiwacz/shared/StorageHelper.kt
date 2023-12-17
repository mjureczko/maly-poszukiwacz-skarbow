package pl.marianjureczko.poszukiwacz.shared

import android.content.Context
import android.util.Log
import org.apache.commons.io.IOUtils
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
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

    private val TAG = javaClass.simpleName
    private val xmlHelper = XmlHelper()

    companion object {
        const val routesDirectory = "/treasures_lists"
        const val progressDirectory = "/progress"
    }

    fun newSoundFile(): String = newFile("sound_", ".3gp")

    fun newPhotoFile(): String = newFile("photo_", ".jpg")

    fun newCommemorativePhotoFile(): String = newFile("commemorativephoto_", ".jpg")

    fun save(bag: TreasuresProgress) {
        val file = getProgressFile(bag.routeName)
        xmlHelper.writeToFile(bag, file)
    }

    fun loadProgress(routeName: String): TreasuresProgress? {
        val file = getProgressFile(routeName)
        return if (file.exists()) {
            try {
                xmlHelper.loadProgressFromFile(file)
            } catch (e: Exception) {
                Log.e(TAG, e.message, e)
                null
            }
        } else {
            null
        }
    }

    fun save(route: Route) {
        val xmlFile = getRouteFile(route.fileName())
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
        route.treasures.iterator().forEach {
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

    fun routeAlreadyExists(route: Route): Boolean =
        getRouteFile(route.fileName()).exists()


    fun loadAll(): MutableList<Route> {
        val dir = getRoutesDir()
        return dir.listFiles()
            .filter { it.name.toLowerCase().endsWith(".xml") }
            .mapNotNull {
                try {
                    xmlHelper.loadRouteFromFile(it)
                } catch (e: Exception) {
                    Log.d(TAG, "Error when loading ${it.name}:" + e.message)
                    null
                }
            }.toMutableList()
    }

    @Throws(FileNotFoundException::class)
    fun loadRoute(name: String): Route {
        val routeFile = getRouteFile(name)
        return xmlHelper.loadRouteFromFile(routeFile)
    }

    fun remove(toRemove: Route) {
        toRemove.treasures.iterator().forEach {
            removeTipFiles(it)
        }
        getProgressFile(toRemove.name).delete()
        val fileToRemove = getRouteFile(toRemove.fileName())
        fileToRemove.delete()
    }


    fun removeTipFiles(treasureDescription: TreasureDescription) {
        if (treasureDescription.tipFileName != null) {
            File(treasureDescription.tipFileName!!).delete()
        }
        if (treasureDescription.hasPhoto()) {
            File(treasureDescription.photoFileName!!).delete()
        }
    }

    fun removeRouteByName(name: String) {
        loadAll()
            .filter { it -> it.name == name }
            .forEach { it -> remove(it) }
    }

    fun pathToRoutesDir(): String = getRoutesDir().absolutePath


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
        val route = xmlHelper.loadFromString<Route>(routeXml)
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

    private fun newFile(prefix: String, extension: String) =
        getRoutesDir().absolutePath + File.separator + prefix + UUID.randomUUID().toString() + extension

    private fun getRouteFile(routeName: String): File {
        //TODO: what about invalid characters in name?
        val dir = getRoutesDir()
        return File("${dir.absolutePath}/$routeName.xml")
    }

    private fun getRoutesDir(): File = getDir(routesDirectory)

    private fun getProgressFile(routeName: String): File {
        val dir = getProgressDir()
        return File("${dir.absolutePath}/$routeName.xml")
    }

    private fun getProgressDir(): File = getDir(progressDirectory)

    private fun getDir(dirName: String): File {
        val dir = File(context.filesDir.absolutePath + dirName)
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

