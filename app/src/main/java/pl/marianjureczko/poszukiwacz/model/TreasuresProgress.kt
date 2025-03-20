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
    var selectedTreasureDescriptionId: Int,

    /** Used in treasure selector for marking treasure as found with a delay */
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
    //TODO: mutable only for deserialization, wrap to limit access from code
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

    constructor() : this("", 0) {
        this.routeName = routeName
    }

    fun contains(treasure: Treasure): Boolean =
        collectedQrCodes.contains(treasure.id)

    fun collect(treasure: Treasure, treasureDescription: TreasureDescription?): TreasuresProgress {
        treasureDescription?.let { collectedTreasuresDescriptionId.add(it.id) }
        collectedQrCodes.add(treasure.id)
        when (treasure.type) {
            TreasureType.GOLD -> golds += treasure.quantity
            TreasureType.DIAMOND -> diamonds += treasure.quantity
            TreasureType.RUBY -> rubies += treasure.quantity
            TreasureType.KNOWLEDGE -> knowledge++
        }
        return this
    }

    /** To be used for the classic version, ie when user wants to mark as collected despite the treasure wasn't detected autoamtically */
    fun toggleTreasureDescriptionCollected(treasureDescriptionId: Int): TreasuresProgress {
        val result = if (collectedTreasuresDescriptionId.contains(treasureDescriptionId)) {
            (collectedTreasuresDescriptionId - treasureDescriptionId).toMutableSet()
        } else {
            (collectedTreasuresDescriptionId + treasureDescriptionId).toMutableSet()
        }
        return copy(collectedTreasuresDescriptionId = result)
    }

    fun getCommemorativePhoto(treasureDescription: TreasureDescription): String? =
        commemorativePhotosByTreasuresDescriptionIds[treasureDescription.id]

    fun numberOfCollectedTreasures(): Int = collectedQrCodes.size
}