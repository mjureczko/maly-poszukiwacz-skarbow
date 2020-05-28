package pl.marianjureczko.poszukiwacz

import android.content.Context
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import java.io.File
import java.lang.Exception

class StorageHelper(val context: Context) {

    private val xmlHelper = XmlHelper()

    companion object {
        val treasuresDirectory = "/treasures_lists"
    }

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
        val fileToRemove = getTreasuresFile(listToRemove)
        fileToRemove.delete()
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
    @field:ElementList var tresures: ArrayList<TreasureDescription>
) {
    constructor() : this("", ArrayList())

    //todo: validate name
    fun fileName(): String {
        return name
    }
}

@Root
data class TreasureDescription(
    @field:Element var latitude: Double,
    @field:Element var longitude: Double
) {
    constructor() : this(0.0, 0.0)

    fun prettyName(): String = "$latitude $longitude"
}