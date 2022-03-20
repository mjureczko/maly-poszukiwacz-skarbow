package pl.marianjureczko.poszukiwacz.shared

import org.simpleframework.xml.Serializer
import org.simpleframework.xml.core.Persister
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import java.io.File
import java.io.StringWriter

class XmlHelper {
    private val serializer: Serializer = Persister()

    fun writeToFile(route: Route, outputFile: File) {
        serializer.write(route, outputFile)
    }

    fun writeToString(route: Route): String {
        val stringWriter = StringWriter()
        serializer.write(route, stringWriter)
        return stringWriter.toString()
    }

    fun writeToString(treasureDescription: TreasureDescription): String {
        return writeObjectToString(treasureDescription)
    }

    private fun writeObjectToString(o: Any): String {
        val stringWriter = StringWriter()
        serializer.write(o, stringWriter)
        return stringWriter.toString()
    }

    fun loadRouteFromFile(xmlFile: File): Route {
        val xml = xmlFile.readText()
        return serializer.read(Route::class.java, xml)
    }

    fun loadRouteFromString(xml: String): Route {
        return serializer.read(Route::class.java, xml)
    }

    fun loadTreasureDescriptionFromString(xml: String): TreasureDescription {
        return serializer.read(TreasureDescription::class.java, xml)
    }
}