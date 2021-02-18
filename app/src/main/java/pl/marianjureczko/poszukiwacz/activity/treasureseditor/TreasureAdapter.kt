package pl.marianjureczko.poszukiwacz.activity.treasureseditor

import android.app.Activity
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.Route
import pl.marianjureczko.poszukiwacz.StorageHelper
import pl.marianjureczko.poszukiwacz.TreasureDescription

interface TreasureRemover {
    fun remove(treasureToRemove: TreasureDescription)
}

class TreasureAdapter(
    private val activity: Activity,
    private val route: Route,
    private val recordingPermission: RecordingPermission,
    private val storageHelper: StorageHelper
) : RecyclerView.Adapter<TreasureHolder>(), TreasureRemover {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TreasureHolder {
        val view = activity.layoutInflater.inflate(R.layout.treasures_item, parent, false)
        return TreasureHolder(view, activity, this, recordingPermission, storageHelper)
    }

    override fun onBindViewHolder(holder: TreasureHolder, position: Int) {
        holder.setupTreasure(route.treasures[position], route)
    }

    override fun getItemCount(): Int {
        return route.treasures.size
    }

    override fun remove(treasureToRemove: TreasureDescription) {
        route.treasures.remove(treasureToRemove)
        notifyDataSetChanged()
        storageHelper.save(route)
        //TODO: remove route when last treasure removed (?)
    }
}