package pl.marianjureczko.poszukiwacz.activity.searching.n

import pl.marianjureczko.poszukiwacz.model.Treasure
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.model.TreasureType

class TreasureDescriptionFinder(
    private val treasureDescriptions: List<TreasureDescription>
) {
    //TODO t: add unit test
    fun findTreasureDescription(qrCode: String, treasure: Treasure): TreasureDescription? {
        if (treasure.type == TreasureType.KNOWLEDGE) {
            return treasureDescriptions.find { td ->
                qrCode == td.qrCode
            }
        } else { //TODO t: implement for classic
            return null
        }
    }
}