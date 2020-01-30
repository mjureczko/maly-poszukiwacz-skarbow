package pl.marianjureczko.poszukiwacz

class TreasureBag {
    private val collected : MutableSet<String> = mutableSetOf()

    var golds: Int = 0
        private set

    var rubies: Int = 0
        private set

    var diamonds: Int = 0
        private set

    fun containsTreasure(treasure: Treasure): Boolean =
        collected.contains(treasure.id)

    fun collectTreasure(treasure: Treasure) {
        collected.add(treasure.id)
        when(treasure.type) {
            TreasureType.GOLD -> golds += treasure.quantity
            TreasureType.DIAMOND -> diamonds += treasure.quantity
            TreasureType.RUBY -> rubies += treasure.quantity
        }
    }
}