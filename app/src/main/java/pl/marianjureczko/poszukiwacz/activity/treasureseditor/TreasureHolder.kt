package pl.marianjureczko.poszukiwacz.activity.treasureseditor

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.media.AudioManager
import android.media.ToneGenerator
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.shared.StorageHelper

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
    private val photoBtn: ImageButton = itemView.findViewById(R.id.photo_tip)
    private val deleteBtn: ImageButton = itemView.findViewById(R.id.del_treasure)

    fun setupTreasure(treasure: TreasureDescription, route: Route) {
        treasureNameLabel.text = treasure.prettyName();
        deleteBtn.setOnClickListener { treasureRemover.remove(treasure) }
        recordTipBtn.setOnClickListener {
            if (recordingPermission.granted()) {
                val soundFileName = storageHelper.newSoundFile()
                storageHelper.removeTipFiles(treasure)
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

        val photoFile = storageHelper.photoFile()
        val photoUri = FileProvider.getUriForFile(activity, "pl.marianjureczko.poszukiwacz.fileprovider", photoFile)
        val captureImage = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val packageManager: PackageManager = activity.packageManager
        photoBtn.setOnClickListener {
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            val cameraActivities: List<ResolveInfo> = packageManager.queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY)
            for (cameraActivity in cameraActivities) {
                activity.grantUriPermission(cameraActivity.activityInfo.packageName, photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            }
            activity.startActivityForResult(captureImage, TreasuresEditorActivity.REQUEST_PHOTO)
        }
    }
}