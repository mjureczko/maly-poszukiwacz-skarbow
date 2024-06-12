package pl.marianjureczko.poszukiwacz.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.ElementMap
import org.simpleframework.xml.Root
import java.io.Serializable

@Root
data class TreasuresProgress(
    @field:Element
    var routeName: String,

    /** Selected for searching */
    @field:Element
    var selectedTreasure: TreasureDescription,

    @field:Element(required = false)
    var justFoundTreasureId: Int? = null,

    @field:Element(required = false)
    var resultRequiresPresentation: Boolean? = false,

    @field:Element(required = false)
    var treasureFoundGoToSelector: Boolean? = false,

    @field:ElementList(required = false)
    var collectedQrCodes: MutableSet<String> = mutableSetOf(),

    @field:ElementList(required = false)
    var collectedTreasuresDescriptionId: MutableSet<Int> = mutableSetOf(),

//TODO: remove files on remove tresure bag (restart) and on removing route
    @field:ElementMap(required = false)
    var commemorativePhotosByTreasuresDescriptionIds: MutableMap<Int, String> = mutableMapOf(),

    @field:Element(required = false)
    var knowledge: Int = 0,

    @field:Element(required = false)
    var golds: Int = 0,

    @field:Element(required = false)
    var rubies: Int = 0,

    @field:Element(required = false)
    var diamonds: Int = 0,
) : Serializable {

    constructor() : this("", TreasureDescription.nullObject()) {
        this.routeName = routeName
    }

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