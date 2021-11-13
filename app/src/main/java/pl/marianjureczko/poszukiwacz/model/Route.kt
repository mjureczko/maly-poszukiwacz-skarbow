package pl.marianjureczko.poszukiwacz.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import pl.marianjureczko.poszukiwacz.shared.StorageHelper
import java.io.Serializable

@Root
data class Route(
    @field:Element var name: String,
    @field:ElementList var treasures: MutableList<TreasureDescription>
) : Serializable {
    constructor() : this("", ArrayList())

    companion object {
        fun nullObject(): Route = Route("???", ArrayList())
    }

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

    fun remove(td: TreasureDescription, storageHelper: StorageHelper) {
        treasures.remove(td)
        storageHelper.removeTipFiles(td)
    }

    fun addPrefixToFilesPaths(prefix: String) {
        treasures.forEach { treasure ->
            treasure.photoFileName?.let {
                treasure.photoFileName = prefix + treasure.photoFileName
            }
            treasure.tipFileName?.let {
                treasure.tipFileName = prefix + treasure.tipFileName
            }
        }
    }
}