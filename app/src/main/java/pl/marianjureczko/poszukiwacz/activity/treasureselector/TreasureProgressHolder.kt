package pl.marianjureczko.poszukiwacz.activity.treasureselector

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.activity.searching.LocationCalculator
import pl.marianjureczko.poszukiwacz.model.TreasureDescription

interface ActivityTerminator {
    fun finishWithResult(treasureId: Int)
}

class TreasureProgressHolder(
    private val view: View,
    private val context: Context,
    private val userLocation: Coordinates?,
    private val activityTerminator: ActivityTerminator
    ) : RecyclerView.ViewHolder(view)  {

    private val TAG = javaClass.simpleName

    private val locationCalculator = LocationCalculator()
    private val selectBtn: Button = itemView.findViewById(R.id.treasure_info)

    fun setup(treasure: TreasureDescription) {
        selectBtn.text = if(userLocation != null) {
            describeTreasureUsingDistance(treasure, userLocation)
        } else {
            treasure.prettyName()
        }
        selectBtn.setOnClickListener { activityTerminator.finishWithResult(treasure.id) }
    }

    private fun describeTreasureUsingDistance(treasure: TreasureDescription, userLocation: Coordinates): String {
        val distance: Int = locationCalculator.distanceInSteps(treasure, userLocation)
        val id: Int = treasure.id
        return context?.getString(R.string.steps_to_treasure, id, distance)
    }
}