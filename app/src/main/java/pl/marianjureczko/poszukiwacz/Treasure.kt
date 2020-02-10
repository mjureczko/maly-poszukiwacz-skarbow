package pl.marianjureczko.poszukiwacz

enum class TreasureType {
    GOLD {
        override fun image() = R.drawable.gold
    },
    RUBY {
        override fun image() = R.drawable.ruby
    },
    DIAMOND {
        override fun image() = R.drawable.diamond
    };

    companion object {
        fun from(code: String): TreasureType =
            when (code) {
                "g" -> GOLD
                "r" -> RUBY
                "d" -> DIAMOND
                else -> throw IllegalArgumentException("$code is not valid treasure type")
            }

    }

    abstract fun image(): Int
}

data class Treasure(val id: String, val quantity: Int, val type: TreasureType)

class TreasureParser {
    fun parse(content: String): Treasure =
        Treasure(
            content.substring(3),
            content.substring(1, 3).toInt(),
            TreasureType.from(content.substring(0, 1))
        )
}