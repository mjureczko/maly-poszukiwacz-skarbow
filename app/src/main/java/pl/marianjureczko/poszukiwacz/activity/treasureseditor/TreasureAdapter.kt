package pl.marianjureczko.poszukiwacz.activity.treasureseditor

import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.shared.StorageHelper

interface TreasurePhotoMaker {
    fun doPhotoForTreasure(treasure: TreasureDescription)
}

interface TreasureRemover {
    fun remove(treasureToRemove: TreasureDescription)
}

class TreasureAdapter(
    private val activity: FragmentActivity,
    private val route: Route,
    private val storageHelper: StorageHelper,
    private val treasurePhotoMaker: TreasurePhotoMaker,
    private val treasureRemover: TreasureRemover
) : RecyclerView.Adapter<TreasureHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TreasureHolder {
        val view = activity.layoutInflater.inflate(R.layout.treasures_item, parent, false)
        return TreasureHolder(view, activity, treasureRemover, treasurePhotoMaker, storageHelper)
    }

    override fun onBindViewHolder(holder: TreasureHolder, position: Int) {
        holder.setupTreasure(route.treasures[position], route)
    }

    override fun getItemCount(): Int {
        return route.treasures.size
    }
}