package pl.marianjureczko.poszukiwacz

import android.app.AlertDialog
import android.content.Context
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListAdapter
import pl.marianjureczko.poszukiwacz.activity.SearchingActivity
import pl.marianjureczko.poszukiwacz.activity.TreasuresEditorActivity


class TreasuresListsAdapter(
    private val list: MutableList<TreasuresList>,
    private val context: Context,
    private val storageHelper: StorageHelper
) : BaseAdapter(), ListAdapter {

    private val TAG = javaClass.simpleName

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if (view == null) {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.treasures_lists_item_layout, null)
        }
        configureTresuresListButton(view!!, position)
        configureEditButton(view, position)
        configureRemoveButton(view, position)
        return view
    }

    private fun configureTresuresListButton(view: View, position: Int) {
        val treasuresList: Button = view.findViewById(R.id.list)
        treasuresList.text = list[position].name
        treasuresList.setOnClickListener { context.startActivity(SearchingActivity.intent(context, list[position])) }
    }

    private fun configureEditButton(view: View, position: Int) {
        val edit: ImageButton = view.findViewById(R.id.edit)
        edit.setOnClickListener { context.startActivity(TreasuresEditorActivity.intent(context, list[position])) }
    }

    private fun configureRemoveButton(view: View, position: Int) {
        val remove: ImageButton = view.findViewById(R.id.del)
        remove.setOnClickListener {
            val name = list[position].name
            val msg = App.getResources().getString(R.string.list_remove_prompt)
            AlertDialog.Builder(context)
                .setMessage(Html.fromHtml(String.format(msg, name)))
                .setPositiveButton(R.string.no) { _, _ -> Log.d(TAG, "####no") }
                .setNegativeButton(R.string.yes) { _, _ -> removeList(position) }
                .show()
        }
    }

    private fun removeList(position: Int) {
        val listToRemove = list[position]
        list.removeAt(position)
        this.notifyDataSetChanged()
        storageHelper.remove(listToRemove)
    }

    override fun getItem(position: Int): Any = list[position]
    override fun getItemId(position: Int): Long = 0
    override fun getCount(): Int = list.size

}