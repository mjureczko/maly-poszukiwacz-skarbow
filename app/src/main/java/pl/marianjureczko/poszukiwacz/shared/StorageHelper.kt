package pl.marianjureczko.poszukiwacz.shared

import android.content.Context
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import java.io.File
import java.util.*

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

    private fun newFile(prefix: String, extension: String) =
        getRoutesDir().absolutePath + File.separator + prefix + UUID.randomUUID().toString() + extension

    private fun getRouteFile(treasures: Route): File {
        val dir = getRoutesDir()
        return File("${dir.absolutePath}/${treasures.fileName()}.xml")
    }

    private fun getRoutesDir(): File {
        val dir = File(context.filesDir.absolutePath + routesDirectory)
        if (!dir.exists()) {
            dir.mkdir()
        }
        return dir
    }
}

