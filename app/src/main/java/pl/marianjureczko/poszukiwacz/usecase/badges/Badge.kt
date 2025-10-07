package pl.marianjureczko.poszukiwacz.usecase.badges

data class Badge(
    val type: BadgeType,
    val level: Int,
    val timestamp: Long = System.currentTimeMillis(),
)
