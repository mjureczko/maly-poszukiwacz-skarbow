package pl.marianjureczko.poszukiwacz.shared.port

import android.content.Context
import android.util.Log
import org.apache.commons.io.IOUtils
import pl.marianjureczko.poszukiwacz.model.HunterPath
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
        const val hunterPathsDirectory = "/huner_paths"
    }

    fun newSoundFile(): String = newFile("sound_", ".3gp")

    open fun newPhotoFile(): String = newFile("photo_", ".jpg")

    fun newCommemorativePhotoFile(): String = newFile("commemorativephoto_", ".jpg")

    open fun save(route: Route) {
        xmlHelper.writeToFile(route, getRouteFile(route.fileName()))
    }

    open fun save(progress: TreasuresProgress) {
        xmlHelper.writeToFile(progress, getProgressFile(progress.routeName))
    }

    open fun save(hunterPath: HunterPath) {
        xmlHelper.writeToFile(hunterPath, getHunterPathFile(hunterPath.routeName))
    }

    open fun loadProgress(routeName: String): TreasuresProgress? {
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

    open fun loadHunterPath(routeName: String): HunterPath? {
        val file = getHunterPathFile(routeName)
        return if (file.exists()) {
            try {
                xmlHelper.loadHunterPathFromFile(file)
            } catch (e: Exception) {
                Log.e(TAG, e.message, e)
                null
            }
        } else {
            null
        }
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

    //TODO t: security issue to fix
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


    open fun loadAll(): MutableList<Route> {
        val dir = getRoutesDir()
        return dir.listFiles()
            .filter { it.name.lowercase().endsWith(".xml") }
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
    open fun loadRoute(name: String): Route {
        val routeFile = getRouteFile(name)
        return xmlHelper.loadRouteFromFile(routeFile)
    }

    open fun remove(toRemove: Route) {
        toRemove.treasures.iterator().forEach {
            removeTipFiles(it)
        }
        removeProgress(toRemove.name)
        val fileToRemove = getRouteFile(toRemove.fileName())
        fileToRemove.delete()
    }

    open fun removeProgress(routeName: String) {
        getProgressFile(routeName).delete()
    }

    open fun removeTipFiles(treasureDescription: TreasureDescription) {
        if (treasureDescription.tipFileName != null) {
            File(treasureDescription.tipFileName!!).delete()
        }
        if (treasureDescription.hasPhoto()) {
            File(treasureDescription.photoFileName!!).delete()
        }
    }

    open fun removeRouteByName(name: String) {
        loadAll()
            .filter { it -> it.name == name }
            .forEach { it -> remove(it) }
    }

    open fun pathToRoutesDir(): String = getRoutesDir().absolutePath

    //TODO: what about invalid characters in name?
    fun getRouteFile(routeName: String): File = getFile(getRoutesDir(), routeName)

    open fun fileIsNotBlank(tipFileName: String?): Boolean {
        TODO("Not yet implemented")
    }

    open fun fileNotEmpty(file: String?) = file?.let { File(it).length() > 0 } ?: false

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

    private fun getProgressFile(routeName: String): File = getFile(getProgressDir(), routeName)

    private fun getHunterPathFile(routeName: String): File = getFile(getHunterPathsDir(), routeName)

    private fun getFile(dir: File, routeName: String) = File("${dir.absolutePath}/$routeName.xml")

    private fun getRoutesDir(): File = getDir(routesDirectory)

    private fun getProgressDir(): File = getDir(progressDirectory)
    private fun getHunterPathsDir(): File = getDir(hunterPathsDirectory)

    private fun getDir(dirName: String): File {
        val dir = File(context.filesDir.absolutePath + dirName)
        if (!dir.exists()) {
            dir.mkdir()
        }
        return dir
    }

    private fun pathsToRelative(route: Route): Route {
        val relativeTreasures = route.treasures
            .map {
                it.copy(
                    tipFileName = toRelativePath(it.tipFileName),
                    photoFileName = toRelativePath(it.photoFileName)
                )
            }
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

