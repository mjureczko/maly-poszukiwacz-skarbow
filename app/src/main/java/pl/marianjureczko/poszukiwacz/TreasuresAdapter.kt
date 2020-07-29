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
import android.widget.Button
import android.widget.ListAdapter
import android.widget.TextView
import pl.marianjureczko.poszukiwacz.dialog.RecordingDialog


private const val LOG_TAG = "TreasuresAdapter"

class TreasuresAdapter(
    private val list: TreasuresList,
    private val context: Activity,
    private val storageHelper: StorageHelper
) : BaseAdapter(), ListAdapter {

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
        label.text = list.treasures[position].prettyName()
    }

    private fun configureRemoveButton(view: View, position: Int) {
        val remove: Button = view.findViewById(R.id.del_treasure)
        remove.setOnClickListener {
            list.treasures.removeAt(position)
            notifyDataSetChanged()
            storageHelper.save(list)
            //TODO: remove when last treasure removed (?)
        }
    }

    private fun configureRecordTipButton(view: View, position: Int) {
        val record: Button = view.findViewById(R.id.record_tip)
        record.setOnClickListener{
            if(permissionToRecordAccepted) {
                val soundFileName = storageHelper.generateNewSoundFile()
                storageHelper.removeTipFile(list.treasures[position] )
                list.treasures[position].tipFileName = soundFileName
                storageHelper.save(list)
                RecordingDialog(context, soundFileName).show()
            } else {
                Log.w(LOG_TAG, "Recording not permitted")
                ToneGenerator(AudioManager.STREAM_NOTIFICATION, 50)
                    .startTone(ToneGenerator.TONE_PROP_BEEP)
            }
        }
    }

    override fun getItem(position: Int): Any = list.treasures[position]
    override fun getItemId(position: Int): Long = 0
    override fun getCount(): Int = list.treasures.size
}