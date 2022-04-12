package pl.marianjureczko.poszukiwacz.activity.treasureselector

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.model.TreasureDescription

interface ActivityTerminator {
    fun finishWithResult(treasureId: Int)
}

class TreasureProgressHolder(
    private val view: View,
    private val context: Context,
    private val activityTerminator: ActivityTerminator,
    private val model: SelectorViewModel
) : RecyclerView.ViewHolder(view) {

    private val TAG = javaClass.simpleName

    private val selectBtn: Button = itemView.findViewById(R.id.treasure_info)
    private val collectedBtn: ImageButton = itemView.findViewById(R.id.markCollected)

    fun setup(treasure: TreasureDescription) {
        if (model.isCollected(treasure)) {
            showCollected()
        } else {
            showNotCollected()
        }
        collectedBtn.setOnClickListener {
            if (model.isCollected(treasure)) {
                model.uncollect(treasure)
                showNotCollected()
            } else {
                model.collect(treasure)
                showCollected()
            }
        }
        selectBtn.text = model.generateTreasureDesription(treasure, object: TreasureDescriptionTemplateProvider{
            override fun provide(treasureId: Int, distanceInSteps: Int): String {
                return context.getString(R.string.steps_to_treasure, treasureId, distanceInSteps)
            }
        })
        selectBtn.setOnClickListener { activityTerminator.finishWithResult(treasure.id) }
    }

    private fun showCollected() {
        collectedBtn.setImageResource(R.drawable.checkbox_checked)
        selectBtn.isEnabled = false
    }

    private fun showNotCollected() {
        collectedBtn.setImageResource(R.drawable.checkbox_empty)
        selectBtn.isEnabled = true
    }

}