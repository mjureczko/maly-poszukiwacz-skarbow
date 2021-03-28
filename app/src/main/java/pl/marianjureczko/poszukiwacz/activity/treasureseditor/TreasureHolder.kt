package pl.marianjureczko.poszukiwacz.activity.treasureseditor

import android.media.AudioManager
import android.media.ToneGenerator
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.StorageHelper
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureDescription

private const val RECORD_TIP_DIALOG = "RecordTipDialog"

class TreasureHolder(
    view: View,
    private val activity: FragmentActivity,
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
                RecordingDialog.newInstance(soundFileName).apply {
                    show(this@TreasureHolder.activity.supportFragmentManager, RECORD_TIP_DIALOG)
                }
            } else {
                Log.w(TAG, "Recording not permitted")
                ToneGenerator(AudioManager.STREAM_NOTIFICATION, 50).startTone(ToneGenerator.TONE_PROP_BEEP)
            }
        }
    }
}