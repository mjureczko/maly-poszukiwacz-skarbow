package pl.marianjureczko.poszukiwacz

import android.content.Context
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import org.simpleframework.xml.Serializer
import org.simpleframework.xml.core.Persister
import java.io.File
import java.lang.Exception

class StorageHelper(val context: Context) {

    companion object {
        val treasuresDirectory = "/treasures_lists"
    }

    fun save(treasures: TreasuresList) {
        val xmlFile = getTreasuresFile(treasures)
        saveXml(treasures, xmlFile)
    }

    fun loadAll(): MutableList<TreasuresList> {
        val dir = getTreasuresDir()
        return dir.listFiles()
            .mapNotNull {
                try {
                    loadTreasuresFromFile(it)
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

    private fun loadTreasuresFromFile(xmlFile: File): TreasuresList {
        val serializer: Serializer = Persister()
        val xml = xmlFile.readText()
        return serializer.read(TreasuresList::class.java, xml)
    }

    private fun getTreasuresDir(): File {
        val dir = File(context.filesDir.absolutePath + treasuresDirectory)
        if (!dir.exists()) {
            dir.mkdir()
        }
        return dir
    }

    private fun saveXml(treasures: TreasuresList, outputFile: File) {
        val serializer: Serializer = Persister()
        serializer.write(treasures, outputFile)
    }
}

@Root
data class TreasuresList(
    @field:Element var name: String,
    @field:ElementList var tresures: ArrayList<TreasureDescription>
) {
    constructor() : this("", ArrayList()) {
    }

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
    constructor() : this(0.0, 0.0) {
    }
}