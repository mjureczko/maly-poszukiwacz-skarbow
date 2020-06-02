package pl.marianjureczko.poszukiwacz.dialog

import android.app.Activity
import android.app.AlertDialog
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import kotlin.math.roundToInt

class SearchResultDialog(val activity: Activity) {

    fun show(text: String, imageId: Int?): AlertDialog {
        val group = LinearLayout(activity)
        group.orientation = LinearLayout.VERTICAL
        group.gravity = Gravity.CENTER_HORIZONTAL

        if (imageId != null) {
            val image = ImageView(activity)
            image.setImageResource(imageId)
            val display = activity.windowManager.defaultDisplay
            val width: Int = (display.width * 0.75f).roundToInt()
            val height: Int = (display.height * 0.5f).roundToInt()
            image.layoutParams = LinearLayout.LayoutParams(width, height)
            group.addView(image)
        }

        val txtView = TextView(activity)
        txtView.text = text
        txtView.gravity = Gravity.CENTER_HORIZONTAL
        txtView.textSize = 45.0f

        group.addView(txtView)

        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
        builder.setView(group)
        val dialog = builder.create()

        dialog.show()
        println("########> dialog.show()")

        return dialog
    }
}