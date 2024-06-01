package pl.marianjureczko.poszukiwacz.activity.facebook

//TODO t: change var to val to avoid missing compose refreshes
data class ElementDescription(
    val index: Int,
    var type: Type,
    var isSelected: Boolean,
    val description: String,
    val orderNumber: Int? = null,
    val photo: String? = null
)

enum class Type {
    TREASURES_SUMMARY, COMMEMORATIVE_PHOTO, MAP, MAP_SUMMARY, MAP_ROUTE
}