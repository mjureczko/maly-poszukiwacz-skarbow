package pl.marianjureczko.poszukiwacz

import android.content.Context
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

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

@Root
data class Route(
    @field:Element var name: String,
    @field:ElementList var treasures: MutableList<TreasureDescription>
) {
    constructor() : this("", ArrayList())

    //todo: validate name
    fun fileName(): String {
        return name
    }

    fun nextId(): Int {
        return if (treasures.isEmpty()) {
            1
        } else {
            1 + treasures.map { it.id }.max()!!
        }
    }
}

@Root
data class TreasureDescription(
    @field:Element var id: Int,
    @field:Element var latitude: Double,
    @field:Element var longitude: Double,
    @field:Element(required = false) var tipFileName: String?
) {
    constructor(id: Int, latitude: Double, longitude: Double) : this(id, latitude, longitude, null)
    constructor() : this(0, 0.0, 0.0, null)

    fun prettyName(): String = "[$id] $latitude $longitude"
}