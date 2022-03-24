package pl.marianjureczko.poszukiwacz.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root
class TreasureBag() {

    constructor(routeName: String) : this() {
        this.routeName = routeName
    }

    @field:Element
    lateinit var routeName: String

    @field:ElementList
    private var collectedQrCodes: MutableSet<String> = mutableSetOf()

    @field:ElementList
    var collectedTreasuresDescriptionId: MutableSet<Int> = mutableSetOf()
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

    fun contains(treasure: Treasure): Boolean =
        collectedQrCodes.contains(treasure.id)

    fun collect(treasure: Treasure) {
        collectedQrCodes.add(treasure.id)
        when (treasure.type) {
            TreasureType.GOLD -> golds += treasure.quantity
            TreasureType.DIAMOND -> diamonds += treasure.quantity
            TreasureType.RUBY -> rubies += treasure.quantity
        }
    }

    fun collect(treasureDescription: TreasureDescription) =
        collectedTreasuresDescriptionId.add(treasureDescription.id)

}