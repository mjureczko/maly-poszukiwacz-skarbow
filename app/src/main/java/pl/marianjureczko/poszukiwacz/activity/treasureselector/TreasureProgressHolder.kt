package pl.marianjureczko.poszukiwacz.activity.treasureselector

import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.activity.commemorative.CommemorativeInputData
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.shared.PhotoHelper
import pl.marianjureczko.poszukiwacz.shared.StorageHelper

interface ActivityTerminator {
    fun finishWithResult(treasureId: Int)
}

class TreasureProgressHolder(
    private val view: View,
    private val context: Context,
    private val activityTerminator: ActivityTerminator,
    private val model: SelectorViewModel,
    private val doPhotoLauncher: ActivityResultLauncher<Uri>,
    private val showCommemorativeLauncher: ActivityResultLauncher<CommemorativeInputData>
) : RecyclerView.ViewHolder(view) {

    private val TAG = javaClass.simpleName

    private val selectBtn: Button = itemView.findViewById(R.id.treasureInfo)
    private val collectedBtn: ImageButton = itemView.findViewById(R.id.markCollected)
    private val photoBtn: ImageButton = itemView.findViewById(R.id.showDetails)
    private val storageHelper = StorageHelper(context)
    private val photoHelper = PhotoHelper(context, storageHelper)

    fun setup(treasure: TreasureDescription, commemorativePhoto: String?) {
        if (model.isCollected(treasure) && treasure != model.justFoundTreasureDescription) {
            showCollected()
        } else {
            showNotCollected()
        }
        collectedBtn.setOnClickListener {
            if (model.isCollected(treasure)) {
                model.uncollect(treasure)
                showNotCollected()
            } else {
                model.collect(treasure)
                showCollected()
            }
        }
        selectBtn.text = model.generateTreasureDesription(treasure, object : TreasureDescriptionTemplateProvider {
            override fun provide(treasureId: Int, distanceInSteps: Int): String {
                return context.getString(R.string.steps_to_treasure, treasureId, distanceInSteps)
            }
        })
        selectBtn.setOnClickListener { activityTerminator.finishWithResult(treasure.id) }
        if (commemorativePhoto == null) {
            photoBtn.setImageResource(R.drawable.camera_do_photo)
        } else {
            photoBtn.setImageResource(R.drawable.camera_show_photo)
        }
        photoBtn.setOnClickListener {
            if (commemorativePhoto == null) {
                model.selectForCommemorativePhoto(treasure)
                doPhotoLauncher.launch(photoHelper.getCommemorativePhotoTempUri())
            } else {
                showCommemorativeLauncher.launch(CommemorativeInputData(commemorativePhoto, model.progress))
            }
        }
    }

    private fun showCollected() {
        collectedBtn.setImageResource(R.drawable.checkbox_checked)
        selectBtn.isEnabled = false
    }

    private fun showNotCollected() {
        collectedBtn.setImageResource(R.drawable.checkbox_empty)
        selectBtn.isEnabled = true
    }

}