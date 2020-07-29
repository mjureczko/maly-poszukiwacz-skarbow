package pl.marianjureczko.poszukiwacz

import android.content.Context
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import java.io.File
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class StorageHelper(val context: Context) {

    private val xmlHelper = XmlHelper()

    companion object {
        val treasuresDirectory = "/treasures_lists"
    }

    fun generateNewSoundFile() = getTreasuresDir().absolutePath + "/" + "test" + UUID.randomUUID().toString() + ".3gp"

    fun save(treasures: TreasuresList) {
        val xmlFile = getTreasuresFile(treasures)
        xmlHelper.writeToFile(treasures, xmlFile)
    }

    fun loadAll(): MutableList<TreasuresList> {
        val dir = getTreasuresDir()
        return dir.listFiles()
            .mapNotNull {
                try {
                    xmlHelper.loadFromFile(it)
                } catch (e: Exception) {
                    null
                }
            }
            .toMutableList()
    }

    fun remove(listToRemove: TreasuresList) {
        listToRemove.treasures.forEach {
            removeTipFile(it)
        }
        val fileToRemove = getTreasuresFile(listToRemove)
        fileToRemove.delete()
    }

    fun removeTipFile(treasureDescription: TreasureDescription) {
        if (treasureDescription.tipFileName != null) {
            File(treasureDescription.tipFileName).delete()
        }
    }

    private fun getTreasuresFile(treasures: TreasuresList): File {
        val dir = getTreasuresDir()
        return File("${dir.absolutePath}/${treasures.fileName()}.xml")
    }

    private fun getTreasuresDir(): File {
        val dir = File(context.filesDir.absolutePath + treasuresDirectory)
        if (!dir.exists()) {
            dir.mkdir()
        }
        return dir
    }
}

@Root
data class TreasuresList(
    @field:Element var name: String,
    @field:ElementList var treasures: ArrayList<TreasureDescription>
) {
    constructor() : this("", ArrayList())

    //todo: validate name
    fun fileName(): String {
        return name
    }
}

//TODO: remove tim file when removing description
@Root
data class TreasureDescription(
    @field:Element var latitude: Double,
    @field:Element var longitude: Double,
    @field:Element(required = false) var tipFileName: String?
) {
    constructor(latitude: Double, longitude: Double) : this(latitude, longitude, null)
    constructor() : this(0.0, 0.0, null)

    fun prettyName(): String = "$latitude $longitude"
}