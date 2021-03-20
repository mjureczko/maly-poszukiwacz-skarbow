package pl.marianjureczko.poszukiwacz.activity.searching

import android.location.Location
import android.widget.ImageView
import android.widget.TextView
import pl.marianjureczko.poszukiwacz.model.TreasureDescription

class CompassPresenter(
    private val stepsToDo: TextView,
    private val arrow: ImageView
) {

    private val locationCalculator = LocationCalculator()
    private val arcCalculator = ArcCalculator()

    fun adjustCompassToCurrentLocationAndTreasure(location: Location?, treasure: TreasureDescription?) {
        location?.let { location ->
            treasure?.let { treasure ->
                stepsToDo.text = locationCalculator.distanceInSteps(treasure, location).toString()
                val arc = arcCalculator.arc(treasure.longitude, treasure.latitude, location.longitude, location.latitude)
                arrow.rotation = arc.toFloat()
            }
        }
    }
}