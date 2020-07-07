package pl.marianjureczko.poszukiwacz

import org.simpleframework.xml.Serializer
import org.simpleframework.xml.core.Persister
import java.io.File
import java.io.StringWriter

class XmlHelper {
    private val serializer: Serializer = Persister()

    fun writeToFile(treasures: TreasuresList, outputFile: File) {
        serializer.write(treasures, outputFile)
    }

    fun writeToString(treasures: TreasuresList): String {
        val stringWriter = StringWriter()
        serializer.write(treasures, stringWriter)
        return stringWriter.toString()
    }

    fun loadFromFile(xmlFile: File): TreasuresList {
        val xml = xmlFile.readText()
        return serializer.read(TreasuresList::class.java, xml)
    }

    fun loadFromString(xml: String): TreasuresList {
        return serializer.read(TreasuresList::class.java, xml)
    }
}