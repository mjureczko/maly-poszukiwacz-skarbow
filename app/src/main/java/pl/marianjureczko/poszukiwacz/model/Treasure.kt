package pl.marianjureczko.poszukiwacz.model

import pl.marianjureczko.poszukiwacz.R
import java.io.Serializable

enum class TreasureType {
    KNOWLEDGE {
        // remove image()
        override fun image() = R.drawable.gold
    },
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
                "k" -> KNOWLEDGE
                else -> throw IllegalArgumentException("$code is not valid treasure type")
            }

    }

    abstract fun image(): Int
}

/** QR code is the id */
data class Treasure(val id: String, val quantity: Int, val type: TreasureType) : Serializable {
    companion object {
        fun knowledgeTreasure(id: String): Treasure = Treasure(id, 1, TreasureType.KNOWLEDGE)
    }
}

class TreasureParser {
    fun parse(content: String): Treasure {
        val type = TreasureType.from(content.substring(0, 1))
        val quantity = if (type == TreasureType.KNOWLEDGE) {
            1
        } else {
            content.substring(1, 3).toInt()
        }
        return Treasure(content, quantity, type)
    }
}