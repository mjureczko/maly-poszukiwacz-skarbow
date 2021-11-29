package pl.marianjureczko.poszukiwacz.activity.treasureseditor

import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.Treasure
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.shared.PermissionsManager
import pl.marianjureczko.poszukiwacz.shared.StorageHelper

interface TreasureRemover {
    fun remove(treasureToRemove: TreasureDescription)
}

interface TreasurePhotoMaker {
    fun doPhotoForTreasure(treasure: TreasureDescription)
}

class TreasureAdapter(
    private val activity: FragmentActivity,
    private val route: Route,
    private val permissions: PermissionsManager,
    private val storageHelper: StorageHelper,
    private val treasurePhotoMaker: TreasurePhotoMaker
) : RecyclerView.Adapter<TreasureHolder>(), TreasureRemover {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TreasureHolder {
        val view = activity.layoutInflater.inflate(R.layout.treasures_item, parent, false)
        return TreasureHolder(view, activity, this, treasurePhotoMaker, permissions, storageHelper)
    }

    override fun onBindViewHolder(holder: TreasureHolder, position: Int) {
        holder.setupTreasure(route.treasures[position], route)
    }

    override fun getItemCount(): Int {
        return route.treasures.size
    }

    override fun remove(treasureToRemove: TreasureDescription) {
        route.remove(treasureToRemove, storageHelper)
        notifyDataSetChanged()
        storageHelper.save(route)
    }
}