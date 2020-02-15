package pl.marianjureczko.poszukiwacz

import java.util.ArrayList

class TreasureBag(treasuresAmount: ArrayList<Int>?, collectedTreasures: ArrayList<String>?) {
    val collected : MutableSet<String> = collectedTreasures?.toMutableSet() ?: mutableSetOf()

    var golds: Int = treasuresAmount?.get(0) ?: 0
        private set

    var rubies: Int = treasuresAmount?.get(1) ?: 0
        private set

    var diamonds: Int = treasuresAmount?.get(2) ?: 0
        private set

    fun contains(treasure: Treasure): Boolean =
        collected.contains(treasure.id)

    fun collect(treasure: Treasure) {
        collected.add(treasure.id)
        when(treasure.type) {
            TreasureType.GOLD -> golds += treasure.quantity
            TreasureType.DIAMOND -> diamonds += treasure.quantity
            TreasureType.RUBY -> rubies += treasure.quantity
        }
    }

    fun bagContent() = arrayListOf(golds, rubies, diamonds)
}