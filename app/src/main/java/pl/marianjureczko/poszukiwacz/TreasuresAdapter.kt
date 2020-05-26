package pl.marianjureczko.poszukiwacz

import android.app.AlertDialog
import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ListAdapter
import android.widget.TextView

class TreasuresAdapter(
    private val list: TreasuresList,
    private val context: Context,
    private val storageHelper: StorageHelper
) : BaseAdapter(), ListAdapter {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if (view == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.treasure_item_layout, null)
        }
        configureLabel(view!!, position)
        configureRemoveButton(view, position)
        return view
    }

    private fun configureLabel(view: View, position: Int) {
        val label = view.findViewById<TextView>(R.id.treasure_name)
        val treasure = list.tresures[position]
        label.text = "${treasure.latitude};${treasure.longitude}"
    }

    private fun configureRemoveButton(view: View, position: Int) {
        val remove: Button = view.findViewById(R.id.del_treasure)
        remove.setOnClickListener {

        }
    }

    override fun getItem(position: Int): Any = list.tresures[position]
    override fun getItemId(position: Int): Long = 0
    override fun getCount(): Int = list.tresures.size

}