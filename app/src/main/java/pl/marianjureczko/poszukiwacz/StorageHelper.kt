package pl.marianjureczko.poszukiwacz

import android.content.Context
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import org.simpleframework.xml.Serializer
import org.simpleframework.xml.core.Persister
import java.io.File

class StorageHelper(val context: Context) {

    private val treasuresDirectory = "/treasures_lists"

    fun save(treasures: TreasuresList) {
        val dir = getTreasuresDir()
        //todo: validate name
        val xmlFile = File("${dir.absolutePath}/${treasures.name}.xml")
        saveXml(treasures, xmlFile)
    }

    fun loadAll(): MutableList<TreasuresList> {
        val dir = getTreasuresDir()
        return dir.listFiles()
            .map { loadTreasuresFromFile(it) }
            .toMutableList()
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
}

@Root
data class TreasureDescription(
    @field:Element var latitude: Double,
    @field:Element var longitude: Double
) {
    constructor() : this(0.0, 0.0) {
    }
}