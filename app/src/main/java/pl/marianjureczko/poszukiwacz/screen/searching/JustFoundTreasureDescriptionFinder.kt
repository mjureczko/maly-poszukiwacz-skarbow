package pl.marianjureczko.poszukiwacz.screen.searching

import android.util.Log
import pl.marianjureczko.poszukiwacz.compass.api.AndroidLocation
import pl.marianjureczko.poszukiwacz.compass.api.LocationCalculator
import pl.marianjureczko.poszukiwacz.model.Treasure
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.model.TreasureType

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
                val target = AndroidLocation.create(
                    latitude = selectedTreasureDescription.latitude,
                    longitude = selectedTreasureDescription.longitude,
                    accuracy = 0f,
                    observedAt = 0
                )
                val distance = locationCalculator.distanceInSteps(target, userLocation)
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