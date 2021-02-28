package pl.marianjureczko.poszukiwacz.activity.main

import android.app.AlertDialog
import android.content.Context
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import pl.marianjureczko.poszukiwacz.App
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.Route
import pl.marianjureczko.poszukiwacz.activity.searching.SearchingActivity
import pl.marianjureczko.poszukiwacz.activity.treasureseditor.TreasuresEditorActivity

class RouteHolder(
    view: View,
    private val context: Context,
    private val routesRemover: RoutesRemover
) : RecyclerView.ViewHolder(view) {
    private val TAG = javaClass.simpleName
    private val selectBtn: Button = itemView.findViewById(R.id.select_route)
    private val editBtn: ImageButton = itemView.findViewById(R.id.edit_route)
    private val deleteBtn: ImageButton = itemView.findViewById(R.id.delete_route)

    fun setupRoute(route: Route) {
        selectBtn.text = route.name
        selectBtn.setOnClickListener { context.startActivity(SearchingActivity.intent(context, route)) }

        editBtn.setOnClickListener { context.startActivity(TreasuresEditorActivity.intent(context, route)) }

        deleteBtn.setOnClickListener {
            val name = route.name
            val msg = App.getResources().getString(R.string.route_remove_prompt)
            AlertDialog.Builder(context)
                .setMessage(Html.fromHtml(String.format(msg, name)))
                .setPositiveButton(R.string.no) { _, _ -> Log.d(TAG, "####no") }
                .setNegativeButton(R.string.yes) { _, _ -> routesRemover.remove(route) }
                .show()
        }
    }
}