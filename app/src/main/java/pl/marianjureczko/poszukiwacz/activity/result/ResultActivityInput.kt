package pl.marianjureczko.poszukiwacz.activity.result

import pl.marianjureczko.poszukiwacz.model.Treasure
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
import java.io.Serializable

//TODO t: remove ?
data class ResultActivityInput(
    val treasure: Treasure?,
    val treasureDescription: TreasureDescription?,
    val progress: TreasuresProgress?
) : Serializable

data class ResultActivityOutput(
    val progress: TreasuresProgress?,
    val justFoundTreasureDescription: TreasureDescription?,
    val newTreasureCollected: Boolean
) : Serializable