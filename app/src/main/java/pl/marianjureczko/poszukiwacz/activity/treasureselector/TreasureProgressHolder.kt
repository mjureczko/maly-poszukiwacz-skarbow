package pl.marianjureczko.poszukiwacz.activity.treasureselector

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.activity.searching.LocationCalculator
import pl.marianjureczko.poszukiwacz.model.TreasureBag
import pl.marianjureczko.poszukiwacz.model.TreasureDescription

interface ActivityTerminator {
    fun finishWithResult(treasureId: Int)
}

class TreasureProgressHolder(
    private val view: View,
    private val context: Context,
    private val userLocation: Coordinates?,
    private val activityTerminator: ActivityTerminator,
    private val progress: TreasureBag
) : RecyclerView.ViewHolder(view) {

    private val TAG = javaClass.simpleName

    private val locationCalculator = LocationCalculator()
    private val selectBtn: Button = itemView.findViewById(R.id.treasure_info)
    private val collectedBtn: ImageButton = itemView.findViewById(R.id.markCollected)
    private val previouslySelectedTreasurePrefix = "-> "

    fun setup(treasure: TreasureDescription) {
        if (isCollected(treasure)) {
            showCollected()
        } else {
            showNotCollected()
        }
        collectedBtn.setOnClickListener {
            if (isCollected(treasure)) {
                collect(treasure)
                showNotCollected()
            } else {
                uncollect(treasure)
                showCollected()
            }
        }
        selectBtn.text = if (userLocation != null) {
            describeTreasureUsingDistance(treasure, userLocation)
        } else {
            treasure.prettyName()
        }
        selectBtn.setOnClickListener { activityTerminator.finishWithResult(treasure.id) }
    }

    private fun isCollected(treasure: TreasureDescription) =
        progress.collectedTreasuresDescriptionId.contains(treasure.id)

    private fun showCollected() {
        collectedBtn.setImageResource(R.drawable.checkbox_checked)
        selectBtn.isEnabled = false
    }

    private fun showNotCollected() {
        collectedBtn.setImageResource(R.drawable.checkbox_empty)
        selectBtn.isEnabled = true
    }

    private fun collect(treasure: TreasureDescription) =
        progress.collectedTreasuresDescriptionId.remove(treasure.id)

    private fun uncollect(treasure: TreasureDescription) =
        progress.collectedTreasuresDescriptionId.add(treasure.id)

    private fun describeTreasureUsingDistance(treasure: TreasureDescription, userLocation: Coordinates): String {
        val distance: Int = locationCalculator.distanceInSteps(treasure, userLocation)
        val id: Int = treasure.id
        return context?.getString(R.string.steps_to_treasure, id, distance)
    }
}