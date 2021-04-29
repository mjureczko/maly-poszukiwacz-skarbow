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

    fun newSoundFile() = newFile("sound_", ".3gp")

    open fun newPhotoFile() = newFile("photo_", ".jpg")

    fun photoFile(): File {
        return File(context.applicationContext.filesDir, "photo_" + UUID.randomUUID().toString() + ".jpg")
    }

    fun save(route: Route) {
        val xmlFile = getRouteFile(route)
        xmlHelper.writeToFile(route, xmlFile)
    }

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
        //TODO: remove photo
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

