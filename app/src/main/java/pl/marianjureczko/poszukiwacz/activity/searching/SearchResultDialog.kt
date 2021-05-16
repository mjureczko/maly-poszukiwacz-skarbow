package pl.marianjureczko.poszukiwacz.activity.searching

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import pl.marianjureczko.poszukiwacz.R
import kotlin.math.roundToInt

private const val DIALOG_DATA = "dialog_data"

class SearchResultDialog : DialogFragment() {
    private val TAG = javaClass.simpleName

    companion object {
        fun newInstance(dialogData: DialogData): SearchResultDialog {
            val args = Bundle().apply {
                putSerializable(DIALOG_DATA, dialogData)
            }

            return SearchResultDialog().apply {
                arguments = args
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val layout = LinearLayout(activity)
        layout.orientation = LinearLayout.VERTICAL
        layout.gravity = Gravity.CENTER_HORIZONTAL
        arguments?.getSerializable(DIALOG_DATA)?.let {
            it as DialogData
            if (isThereImageToShow(it)) {
                addImageToLayout(layout, it.imageId!!)
            }
            addTextToLayout(it, layout)
        }
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setView(layout)
        builder.setPositiveButton(R.string.ok) { dialog, _ ->
            dialog.dismiss()
        }
        return builder.create()

    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            val ft = manager.beginTransaction()
            ft.add(this, tag)
            ft.commitAllowingStateLoss()
        } catch (ignored: IllegalStateException) {
            ignored.printStackTrace()
        }
    }

    private fun isThereImageToShow(it: DialogData) =
        it.imageId != null && it.imageId != 0

    private fun addImageToLayout(layout: LinearLayout, imageId: Int) {
        val image = ImageView(activity)
        image.setImageResource(imageId)
        activity?.windowManager?.defaultDisplay?.let { display ->
            val width: Int = (display.width * 0.75f).roundToInt()
            val height: Int = (display.height * 0.5f).roundToInt()
            image.layoutParams = LinearLayout.LayoutParams(width, height)
            layout.addView(image)
        }
    }

    private fun addTextToLayout(it: DialogData, group: LinearLayout) {
        val txtView = TextView(activity)
        txtView.text = it.msg
        txtView.gravity = Gravity.CENTER_HORIZONTAL
        txtView.textSize = 45.0f
        group.addView(txtView)
    }
}