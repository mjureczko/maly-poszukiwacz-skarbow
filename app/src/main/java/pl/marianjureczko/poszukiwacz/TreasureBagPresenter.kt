package pl.marianjureczko.poszukiwacz

class TreasureBagPresenter {

    private val treasureBag = TreasureBag()

    fun contains(treasure: Treasure) = treasureBag.contains(treasure)

    fun add(treasure: Treasure) {
        treasureBag.collect(treasure)
    }
}