package pl.marianjureczko.poszukiwacz.activity.searching

import android.util.Log
import pl.marianjureczko.poszukiwacz.activity.treasureselector.Coordinates
import pl.marianjureczko.poszukiwacz.model.Treasure
import pl.marianjureczko.poszukiwacz.model.TreasureDescription

/**
 * To find data about the "just found" by user treasure
 */
class JustFoundFinder(
    /**justFoundTreasure shall be used for app version with fixed treasures locations*/
    val justFoundTreasure: Treasure?,
    val selectedTreasureDescription: TreasureDescription?,
    val userLocation: Coordinates?,
    val locationCalculator: LocationCalculator = LocationCalculator()
) {
    private val TAG = javaClass.simpleName

    fun findTreasureDescription(): TreasureDescription? {
        return if (justFoundTreasure != null && selectedTreasureDescription != null && userLocation != null) {
            val distance = locationCalculator.distanceInSteps(selectedTreasureDescription, userLocation)
            Log.i(TAG, "Distance is $distance")
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

