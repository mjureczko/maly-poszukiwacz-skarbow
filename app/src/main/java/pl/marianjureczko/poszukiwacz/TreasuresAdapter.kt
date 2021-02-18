package pl.marianjureczko.poszukiwacz

import android.app.Activity
import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.ListAdapter
import android.widget.TextView
import pl.marianjureczko.poszukiwacz.dialog.RecordingDialog

class TreasuresAdapter(
    private val route: Route,
    private val context: Activity,
    private val storageHelper: StorageHelper
) : BaseAdapter(), ListAdapter {
    private val TAG = javaClass.simpleName
    var permissionToRecordAccepted: Boolean = false

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if (view == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.treasure_item_layout, null)
        }
        configureLabel(view!!, position)
        configureRemoveButton(view, position)
        configureRecordTipButton(view, position)
        return view
    }

    private fun configureLabel(view: View, position: Int) {
        val label = view.findViewById<TextView>(R.id.treasure_name)
        label.text = route.treasures[position].prettyName()
    }

    private fun configureRemoveButton(view: View, position: Int) {
        val remove: ImageButton = view.findViewById(R.id.del_treasure)
        remove.setOnClickListener {
            route.treasures.removeAt(position)
            notifyDataSetChanged()
            storageHelper.save(route)
            //TODO: remove when last treasure removed (?)
        }
    }

    private fun configureRecordTipButton(view: View, position: Int) {
        val record: ImageButton = view.findViewById(R.id.record_tip)
        record.setOnClickListener {
            if (permissionToRecordAccepted) {
                val soundFileName = storageHelper.generateNewSoundFile()
                storageHelper.removeTipFile(route.treasures[position])
                route.treasures[position].tipFileName = soundFileName
                storageHelper.save(route)
                RecordingDialog(context, soundFileName).show()
            } else {
                Log.w(TAG, "Recording not permitted")
                ToneGenerator(AudioManager.STREAM_NOTIFICATION, 50).startTone(ToneGenerator.TONE_PROP_BEEP)
            }
        }
    }

    override fun getItem(position: Int): Any = route.treasures[position]
    override fun getItemId(position: Int): Long = 0
    override fun getCount(): Int = route.treasures.size
}