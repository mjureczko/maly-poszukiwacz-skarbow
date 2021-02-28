package pl.marianjureczko.poszukiwacz.activity.searching

import android.app.Activity
import android.app.AlertDialog
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import pl.marianjureczko.poszukiwacz.R
import kotlin.math.roundToInt

interface DialogCleaner {
    fun cleanupAfterDialog()
}

class SearchResultDialog(
    private val activity: Activity,
    private val cleaner: DialogCleaner
) {

    private val TAG = javaClass.simpleName

    fun show(dialogData: DialogData): AlertDialog {
        val group = LinearLayout(activity)
        group.orientation = LinearLayout.VERTICAL
        group.gravity = Gravity.CENTER_HORIZONTAL

        if (dialogData.imageId != null && dialogData.imageId != 0) {
            val image = ImageView(activity)
            image.setImageResource(dialogData.imageId)
            val display = activity.windowManager.defaultDisplay
            val width: Int = (display.width * 0.75f).roundToInt()
            val height: Int = (display.height * 0.5f).roundToInt()
            image.layoutParams = LinearLayout.LayoutParams(width, height)
            group.addView(image)
        }

        val txtView = TextView(activity)
        txtView.text = dialogData.msg
        txtView.gravity = Gravity.CENTER_HORIZONTAL
        txtView.textSize = 45.0f

        group.addView(txtView)

        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setView(group)
        builder.setPositiveButton(R.string.ok) { dialog, _ ->
            cleaner.cleanupAfterDialog()
            dialog.dismiss()
        }
        builder.setOnCancelListener {
            cleaner.cleanupAfterDialog()
        }
        val dialog = builder.create()
        dialog.show()
        return dialog
    }
}