package pl.marianjureczko.poszukiwacz.activity.treasureselector

import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import pl.marianjureczko.poszukiwacz.R

class TreasureProgressAdapter(
    private val activity: FragmentActivity,
    private val model: SelectorViewModel,
    private val activityTerminator: ActivityTerminator
) : RecyclerView.Adapter<TreasureProgressHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TreasureProgressHolder {
        val view = activity.layoutInflater.inflate(R.layout.treasureprogres_item, parent, false)
        return TreasureProgressHolder(view, activity, activityTerminator, model)
    }

    override fun onBindViewHolder(holder: TreasureProgressHolder, position: Int) {
        holder.setup(model.getTreasureDescriptionByPosition(position))
    }

    override fun getItemCount(): Int =
        model.getNumberOfTreasures()

}