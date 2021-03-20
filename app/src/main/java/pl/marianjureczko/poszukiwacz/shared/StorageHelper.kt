package pl.marianjureczko.poszukiwacz

import android.content.Context
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.shared.XmlHelper
import java.io.File
import java.util.*

class StorageHelper(val context: Context) {

    private val xmlHelper = XmlHelper()

    companion object {
        val routesDirectory = "/treasures_lists"
    }

    fun generateNewSoundFile() = getRoutesDir().absolutePath + "/" + "test" + UUID.randomUUID().toString() + ".3gp"

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
            removeTipFile(it)
        }
        val fileToRemove = getRouteFile(toRemove)
        fileToRemove.delete()
    }

    fun removeTipFile(treasureDescription: TreasureDescription) {
        if (treasureDescription.tipFileName != null) {
            File(treasureDescription.tipFileName).delete()
        }
    }

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

