package pl.marianjureczko.poszukiwacz.usecase.badges

data class Achievements(
    val golds: Int = 0,
    val diamonds: Int = 0,
    val rubies: Int = 0,
    val knowledge: Int = 0,
    val treasures: Int = 0,
    val completedRoutes: Int = 0,
    val greatestNumberOfTreasuresOnRoute: Int = 0,
    val badges: List<Badge> = emptyList()
) {
    fun allJewelries() = golds + diamonds + rubies
}
