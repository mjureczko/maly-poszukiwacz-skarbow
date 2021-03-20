package pl.marianjureczko.poszukiwacz.shared

import org.simpleframework.xml.Serializer
import org.simpleframework.xml.core.Persister
import pl.marianjureczko.poszukiwacz.model.Route
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

    fun loadFromFile(xmlFile: File): Route {
        val xml = xmlFile.readText()
        return serializer.read(Route::class.java, xml)
    }

    fun loadFromString(xml: String): Route {
        return serializer.read(Route::class.java, xml)
    }
}