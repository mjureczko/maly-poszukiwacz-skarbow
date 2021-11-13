package pl.marianjureczko.poszukiwacz.activity.main

import android.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.activity.bluetooth.BluetoothActivity
import pl.marianjureczko.poszukiwacz.activity.searching.SearchingActivity
import pl.marianjureczko.poszukiwacz.activity.treasureseditor.TreasuresEditorActivity
import pl.marianjureczko.poszukiwacz.model.Route

class RouteHolder(
    view: View,
    private val activity: MainActivity,
    private val routesRemover: RoutesRemover
) : RecyclerView.ViewHolder(view) {
    private val TAG = javaClass.simpleName
    private val selectBtn: Button = itemView.findViewById(R.id.select_route)
    private val editBtn: ImageButton = itemView.findViewById(R.id.edit_route)
    private val shareBtn: ImageButton = itemView.findViewById(R.id.share_route)
    private val deleteBtn: ImageButton = itemView.findViewById(R.id.delete_route)

    fun setupRoute(route: Route) {
        selectBtn.text = route.name
        selectBtn.setOnClickListener { activity.startActivity(SearchingActivity.intent(activity, route)) }
        shareBtn.setOnClickListener {
            activity.startActivity(BluetoothActivity.intent(activity, BluetoothActivity.Mode.SENDING, route))
        }
        editBtn.setOnClickListener { activity.startActivity(TreasuresEditorActivity.intent(activity, route)) }

        deleteBtn.setOnClickListener {
            AlertDialog.Builder(activity)
                .setMessage(activity.getString(R.string.route_remove_prompt, route.name))
                .setPositiveButton(R.string.no) { _, _ -> Log.d(TAG, "####no") }
                .setNegativeButton(R.string.yes) { _, _ -> routesRemover.remove(route) }
                .show()
        }
    }
}
