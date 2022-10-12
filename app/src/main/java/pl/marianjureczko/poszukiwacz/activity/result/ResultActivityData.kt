package pl.marianjureczko.poszukiwacz.activity.result

import pl.marianjureczko.poszukiwacz.model.Treasure
import java.io.Serializable

data class ResultActivityData(val treasure: Treasure?, val error: String?) : Serializable {
    constructor(treasure: Treasure?) : this(treasure, null)
    constructor(error: String?) : this(null, error)

    fun isError(): Boolean = error != null
}