package pl.marianjureczko.poszukiwacz.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.ElementMap
import org.simpleframework.xml.Root
import java.io.Serializable

@Root
class TreasuresProgress() : Serializable {

    constructor(routeName: String) : this() {
        this.routeName = routeName
    }

    @field:Element
    lateinit var routeName: String

    @field:ElementList
    var collectedQrCodes: MutableSet<String> = mutableSetOf()
        private set

    @field:ElementList
    var collectedTreasuresDescriptionId: MutableSet<Int> = mutableSetOf()
        private set

    //TODO: remove files on remove tresure bag (restart) and on removing route
    @field:ElementMap
    var commemorativePhotosByTreasuresDescriptionIds: MutableMap<Int, String> = mutableMapOf()
        private set
        public get

    @field:Element
    var knowledge: Int = 0
        private set

    @field:Element
    var golds: Int = 0
        private set

    @field:Element
    var rubies: Int = 0
        private set

    @field:Element
    var diamonds: Int = 0
        private set

    @field:Element(required = false)
    var selectedTreasure: TreasureDescription? = null

    fun contains(treasure: Treasure): Boolean =
        collectedQrCodes.contains(treasure.id)

    /** When collecting KNOWLEDGE collect(treasureDescription: TreasureDescription) must be called as well */
    fun collect(treasure: Treasure) {
        collectedQrCodes.add(treasure.id)
        when (treasure.type) {
            TreasureType.GOLD -> golds += treasure.quantity
            TreasureType.DIAMOND -> diamonds += treasure.quantity
            TreasureType.RUBY -> rubies += treasure.quantity
            TreasureType.KNOWLEDGE -> knowledge++
        }
    }

    /** When collecting KNOWLEDGE collect(treasure: Treasure) must be called as well */
    fun collect(treasureDescription: TreasureDescription) =
        collectedTreasuresDescriptionId.add(treasureDescription.id)

    fun addCommemorativePhoto(treasureDescription: TreasureDescription, commemorativePhoto: String) {
        commemorativePhotosByTreasuresDescriptionIds[treasureDescription.id] = commemorativePhoto
    }

    fun getCommemorativePhoto(treasureDescription: TreasureDescription): String? =
        commemorativePhotosByTreasuresDescriptionIds[treasureDescription.id]

    fun numberOfCollectedTreasures(): Int = collectedQrCodes.size
}