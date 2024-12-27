package pl.marianjureczko.poszukiwacz.activity.searching.n

import android.util.Log
import pl.marianjureczko.poszukiwacz.activity.searching.LocationCalculator
import pl.marianjureczko.poszukiwacz.model.Treasure
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.model.TreasureType
import pl.marianjureczko.poszukiwacz.shared.Coordinates

class JustFoundTreasureDescriptionFinder(
    private val treasureDescriptions: List<TreasureDescription>,
    private val locationCalculator: LocationCalculator = LocationCalculator(),
) {

    private val TAG = javaClass.simpleName

    fun findTreasureDescription(
        qrCode: String,
        justFoundTreasure: Treasure,
        selectedTreasureDescription: TreasureDescription? = null,
        userLocation: Coordinates? = null,
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