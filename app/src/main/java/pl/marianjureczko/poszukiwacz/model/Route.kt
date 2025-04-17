package pl.marianjureczko.poszukiwacz.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import java.io.Serializable

@Root
data class Route(
    @field:Element var name: String,
    @field:ElementList var treasures: List<TreasureDescription>
) : Serializable {
    constructor() : this("", ArrayList())
    constructor(name: String) : this(name, ArrayList())

    //todo: validate name
    fun fileName(): String {
        return name
    }

    fun nextId(): Int {
        return if (treasures.isEmpty()) {
            1
        } else {
            1 + treasures.maxOf { it.id }
        }
    }

    fun getTreasureDescriptionById(id: Int): TreasureDescription? = treasures.find { it.id == id }

    fun addPrefixToFilesPaths(prefix: String) {
        treasures.iterator().forEach { treasure ->
            treasure.photoFileName?.let {
                treasure.photoFileName = prefix + treasure.photoFileName
            }
            treasure.tipFileName?.let {
                treasure.tipFileName = prefix + treasure.tipFileName
            }
        }
    }
}