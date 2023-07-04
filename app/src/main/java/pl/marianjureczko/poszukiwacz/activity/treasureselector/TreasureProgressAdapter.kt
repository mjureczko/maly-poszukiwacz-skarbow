package pl.marianjureczko.poszukiwacz.activity.treasureselector

import android.net.Uri
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import pl.marianjureczko.poszukiwacz.R

class TreasureProgressAdapter(
    private val activity: FragmentActivity,
    private val model: SelectorViewModel,
    private val activityTerminator: ActivityTerminator,
    private val doPhotoLauncher: ActivityResultLauncher<Uri>
) : RecyclerView.Adapter<TreasureProgressHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TreasureProgressHolder {
        val view = activity.layoutInflater.inflate(R.layout.treasureprogres_item, parent, false)
        return TreasureProgressHolder(view, activity, activityTerminator, model, doPhotoLauncher)
    }

    override fun onBindViewHolder(holder: TreasureProgressHolder, position: Int) {
        val treasureDescription = model.getTreasureDescriptionByPosition(position)
        holder.setup(treasureDescription, model.getCommemorativePhoto(treasureDescription))
    }

    override fun getItemCount(): Int =
        model.getNumberOfTreasures()

}