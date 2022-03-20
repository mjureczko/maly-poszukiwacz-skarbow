package pl.marianjureczko.poszukiwacz.activity.treasureseditor

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.ToneGenerator
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.permissions.RequirementsForPhotoAndAudioTip
import pl.marianjureczko.poszukiwacz.permissions.PermissionManager
import pl.marianjureczko.poszukiwacz.shared.StorageHelper

private const val RECORD_TIP_DIALOG = "RecordTipDialog"

class TreasureHolder(
    view: View,
    private val activity: FragmentActivity,
    private val treasureRemover: TreasureRemover,
    private val treasurePhotoMaker: TreasurePhotoMaker,
    private val storageHelper: StorageHelper
) : RecyclerView.ViewHolder(view) {

    private val TAG = javaClass.simpleName
    private val packageManager: PackageManager by lazy { activity.packageManager }
    private val thereIsActivityCapableOfCapturingPhoto: Boolean by lazy {
        packageManager.resolveActivity(capturePhotoIntent(), PackageManager.MATCH_DEFAULT_ONLY) != null
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
            if (PermissionManager.isPermissionGranted(activity, RequirementsForPhotoAndAudioTip.microphone) ) {
                if (treasure.tipFileName != null) {
                    AlertDialog.Builder(activity)
                        .setMessage(R.string.overwritting_tip)
                        .setPositiveButton(R.string.no) { _, _ -> }
                        .setNegativeButton(R.string.yes) { _, _ -> recordNewTip(treasure, route) }
                        .show()
                } else {
                    recordNewTip(treasure, route)
                }
            } else {
                operationNotPermitted("Recording not permitted")
            }
        }
    }

    private fun recordNewTip(treasure: TreasureDescription, route: Route) {
        val soundFileName = treasure.instantiateSoundFile(storageHelper)
        storageHelper.save(route)
        RecordingDialog.newInstance(soundFileName.absolutePath).apply {
            show(this@TreasureHolder.activity.supportFragmentManager, RECORD_TIP_DIALOG)
        }
    }

    private fun setupPhotoBtn(treasure: TreasureDescription, route: Route) {
        photoBtn.setOnClickListener {
            val a = thereIsActivityCapableOfCapturingPhoto
            val permissionGranted = PermissionManager.isPermissionGranted(activity, RequirementsForPhotoAndAudioTip.camera)
            if (a && permissionGranted) {
                if (treasure.hasPhoto()) {
                    AlertDialog.Builder(activity)
                        .setMessage(R.string.overwritting_photo)
                        .setPositiveButton(R.string.no) { _, _ -> }
                        .setNegativeButton(R.string.yes) { _, _ ->
                            treasurePhotoMaker.doPhotoForTreasure(treasure)
                        }
                        .show()
                } else {
                    treasurePhotoMaker.doPhotoForTreasure(treasure)
                }
            } else {
                operationNotPermitted("No intent capable of capturing photo is available")
            }
        }
    }

    private fun capturePhotoIntent() = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

    private fun operationNotPermitted(message: String) {
        Log.w(TAG, message)
        Toast.makeText(activity, R.string.operation_not_permitted, Toast.LENGTH_SHORT).show()
        ToneGenerator(AudioManager.STREAM_NOTIFICATION, 90).startTone(ToneGenerator.TONE_PROP_BEEP)
    }
}