package pl.marianjureczko.poszukiwacz.screen.searching

import android.util.Log
import pl.marianjureczko.poszukiwacz.model.Treasure
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.model.TreasureType
import pl.marianjureczko.poszukiwacz.usecase.AndroidLocation

class JustFoundTreasureDescriptionFinder(
    private val treasureDescriptions: List<TreasureDescription>,
    private val locationCalculator: LocationCalculator,
) {

    private val TAG = javaClass.simpleName

    fun findTreasureDescription(
        justFoundTreasure: Treasure,
        selectedTreasureDescription: TreasureDescription? = null,
        userLocation: AndroidLocation? = null,
    ): TreasureDescription? {
        if (justFoundTreasure.type == TreasureType.KNOWLEDGE) {
            return treasureDescriptions.find { td ->
                justFoundTreasure.id == td.qrCode
            }
        } else {
            return if (selectedTreasureDescription != null && userLocation != null) {
                val distance = locationCalculator.distanceInSteps(selectedTreasureDescription, userLocation)
                Log.d(TAG, "Distance is $distance")
                if (distance < 60) {
                    selectedTreasureDescription
                } else {
                    null
                }
            } else {
                null
            }
        }
    }
}