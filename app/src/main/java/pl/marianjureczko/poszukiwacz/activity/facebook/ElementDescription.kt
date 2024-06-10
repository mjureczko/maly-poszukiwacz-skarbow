package pl.marianjureczko.poszukiwacz.activity.facebook

import android.graphics.Bitmap

data class ElementDescription(
    val index: Int,
    val type: Type,
    val isSelected: Boolean,
    val description: String,
    val orderNumber: Int? = null,
    val photo: String? = null,
    val scaledPhoto: Bitmap? = null
)

enum class Type {
    TREASURES_SUMMARY, COMMEMORATIVE_PHOTO, MAP, MAP_SUMMARY, MAP_ROUTE
}