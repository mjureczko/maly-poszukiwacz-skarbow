package pl.marianjureczko.poszukiwacz.activity.treasureseditor

import android.app.Activity
import android.media.AudioManager
import android.media.ToneGenerator
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.Route
import pl.marianjureczko.poszukiwacz.StorageHelper
import pl.marianjureczko.poszukiwacz.TreasureDescription
import pl.marianjureczko.poszukiwacz.dialog.RecordingDialog

class TreasureHolder(
    view: View,
    private val activity: Activity,
    private val treasureRemover: TreasureRemover,
    private val recordingPermission: RecordingPermission,
    private val storageHelper: StorageHelper
) : RecyclerView.ViewHolder(view) {
    private val TAG = javaClass.simpleName
    private val treasureNameLabel: TextView = itemView.findViewById((R.id.treasure_name))
    private val recordTipBtn: ImageButton = itemView.findViewById(R.id.record_tip)
    private val deleteBtn: ImageButton = itemView.findViewById(R.id.del_treasure)

    fun setupTreasure(treasure: TreasureDescription, route: Route) {
        treasureNameLabel.text = treasure.prettyName();
        deleteBtn.setOnClickListener { treasureRemover.remove(treasure) }
        recordTipBtn.setOnClickListener {
            if (recordingPermission.granted()) {
                val soundFileName = storageHelper.generateNewSoundFile()
                storageHelper.removeTipFile(treasure)
                treasure.tipFileName = soundFileName
                storageHelper.save(route)
                RecordingDialog(activity, soundFileName).show()
            } else {
                Log.w(TAG, "Recording not permitted")
                ToneGenerator(AudioManager.STREAM_NOTIFICATION, 50).startTone(ToneGenerator.TONE_PROP_BEEP)
            }
        }
    }
}