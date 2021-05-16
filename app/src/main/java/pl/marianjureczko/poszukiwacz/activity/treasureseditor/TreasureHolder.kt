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
    private val permissions: PermissionsManager,
    private val storageHelper: StorageHelper
) : RecyclerView.ViewHolder(view) {

    private val TAG = javaClass.simpleName
    private val packageManager: PackageManager by lazy { activity.packageManager }
    private val thereIsActivityCapableOfCapturingPhoto: Boolean by lazy {
        packageManager.resolveActivity(
            capturePhotoIntent(),
            PackageManager.MATCH_DEFAULT_ONLY
        ) != null
    }
    private val treasureNameLabel: TextView = itemView.findViewById((R.id.treasure_name))
    private val recordTipBtn: ImageButton = itemView.findViewById(R.id.record_tip)
    private val photoBtn: ImageButton = itemView.findViewById(R.id.photo_tip)
    private val deleteBtn: ImageButton = itemView.findViewById(R.id.del_treasure)

    fun setupTreasure(treasure: TreasureDescription, route: Route) {
        treasureNameLabel.text = treasure.prettyName();
        deleteBtn.setOnClickListener { treasureRemover.remove(treasure) }
        setupRecordTipBtn(treasure, route)
        setupPhotoBtn(treasure, route)
    }

    private fun setupRecordTipBtn(treasure: TreasureDescription, route: Route) {
        recordTipBtn.setOnClickListener {
            if (permissions.recordingGranted()) {
                val soundFileName = storageHelper.newSoundFile()
                storageHelper.removeTipFiles(treasure)
                treasure.tipFileName = soundFileName
                storageHelper.save(route)
                RecordingDialog.newInstance(soundFileName).apply {
                    show(this@TreasureHolder.activity.supportFragmentManager, RECORD_TIP_DIALOG)
                }
            } else {
                operationNotPermitted("Recording not permitted")
            }
        }
    }

    private fun setupPhotoBtn(treasure: TreasureDescription, route: Route) {
        val instantiatePhotoFile = treasure.instantiatePhotoFile(storageHelper)
        val photoUri =
            FileProvider.getUriForFile(activity, "pl.marianjureczko.poszukiwacz.fileprovider", instantiatePhotoFile)
        val captureImage = capturePhotoIntent()
        captureImage.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        photoBtn.setOnClickListener {
            if (thereIsActivityCapableOfCapturingPhoto && permissions.capturingPhotoGranted()) {
                val cameraActivities: List<ResolveInfo> =
                    packageManager.queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY)
                for (cameraActivity in cameraActivities) {
                    activity.grantUriPermission(cameraActivity.activityInfo.packageName, photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                }
                storageHelper.save(route)
                activity.startActivityForResult(captureImage, TreasuresEditorActivity.REQUEST_PHOTO)
            } else {
                operationNotPermitted("No intent capable of capturing photo is available")
            }
        }
    }

    private fun capturePhotoIntent() = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

    private fun operationNotPermitted(message: String) {
        Log.w(TAG, message)
        ToneGenerator(AudioManager.STREAM_NOTIFICATION, 50).startTone(ToneGenerator.TONE_PROP_BEEP)
    }
}