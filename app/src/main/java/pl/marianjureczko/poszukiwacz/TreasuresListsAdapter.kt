package pl.marianjureczko.poszukiwacz

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ListAdapter


class TreasuresListsAdapter(
    private val list: MutableList<TreasuresList>,
    private val context: Context,
    private val storageHelper: StorageHelper
) : BaseAdapter(), ListAdapter {

    private val xmlHelper = XmlHelper()

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
        treasuresList.setOnClickListener(View.OnClickListener {
            //do something
        })
    }

    private fun configureEditButton(view: View, position: Int) {
        val edit: Button = view.findViewById(R.id.edit)
        edit.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, TreasuresEditorActivity::class.java).apply {
                putExtra(MainActivity.SELECTED_LIST, xmlHelper.writeToString(list[position]))
            }
            context.startActivity(intent)
        })
    }

    private fun configureRemoveButton(view: View, position: Int) {
        val remove: Button = view.findViewById(R.id.del)
        remove.setOnClickListener {
            AlertDialog.Builder(context)
                .setMessage(Html.fromHtml("Czy na pewno chcesz skasować listę <b>${list[position].name}</b>?"))
                .setPositiveButton("Nie") { dialog, which -> println("####no") }
                .setNegativeButton("Tak") { dialog, which -> removeList(position) }
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