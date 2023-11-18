package pl.marianjureczko.poszukiwacz.activity.facebook

class ElementDescription(
    var type: Type,
    var isSelected: Boolean,
    val description: String,
    val orderNumber: Int? = null,
    val photo: String? = null
)

enum class Type {
    TREASURES_SUMMARY, COMMEMORATIVE_PHOTO, MAP, MAP_SUMMARY
}