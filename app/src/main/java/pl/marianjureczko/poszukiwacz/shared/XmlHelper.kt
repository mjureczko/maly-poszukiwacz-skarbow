package pl.marianjureczko.poszukiwacz.shared

import org.simpleframework.xml.Serializer
import org.simpleframework.xml.core.Persister
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
import java.io.File
import java.io.StringWriter

class XmlHelper {
    val serializer: Serializer = Persister()

    fun writeToFile(route: Route, outputFile: File) {
        serializer.write(route, outputFile)
    }

    fun writeToString(o: Any): String {
        val stringWriter = StringWriter()
        serializer.write(o, stringWriter)
        return stringWriter.toString()
    }

    fun loadRouteFromFile(xmlFile: File): Route {
        val xml = xmlFile.readText()
        return serializer.read(Route::class.java, xml)
    }

    inline fun <reified T> loadFromString(xml: String): T {
        return serializer.read(T::class.java, xml)
    }

    fun writeToFile(bag: TreasuresProgress, outputFile: File) =
        serializer.write(bag, outputFile)

    fun loadProgressFromFile(xmlFile: File): TreasuresProgress {
        val xml = xmlFile.readText()
        return serializer.read(TreasuresProgress::class.java, xml)
    }
}