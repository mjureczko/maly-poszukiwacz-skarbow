package pl.marianjureczko.poszukiwacz.activity.facebook

import android.graphics.BitmapFactory
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.shared.PhotoHelper

class ElementHolder(
    private val activity: FragmentActivity,
    private val view: View
) : RecyclerView.ViewHolder(view) {

    private val selected: ImageButton = itemView.findViewById(R.id.selected)
    private val description: TextView = itemView.findViewById(R.id.description)
    private val elementLayout: LinearLayout = itemView.findViewById(R.id.element)

    fun setup(element: ElementDescription) {
        description.text = element.description
        if (element.isSelected) {
            selected.setImageResource(R.drawable.checkbox_checked)
        } else {
            selected.setImageResource(R.drawable.checkbox_empty)
        }
        addListener(selected, element)
        addPhotoWidgets(elementLayout, element.photo)
    }

    private fun addPhotoWidgets(elementLayout: LinearLayout, photoFile: String?) {
        photoFile?.let { photoFile ->
            val image = ImageView(elementLayout.context)
            renderPhoto(image, photoFile)
            elementLayout.addView(image)

            //add ImageButton to elementLayout
            val imageButton = ImageButton(elementLayout.context)
            imageButton.background = null
            imageButton.setImageResource(R.drawable.rotate_arc)
            imageButton.layoutParams = LinearLayout.LayoutParams(toPx(100), toPx(100))
            imageButton.scaleType = ImageView.ScaleType.FIT_CENTER
            imageButton.setOnClickListener {
                activity.lifecycleScope.launch {
                    PhotoHelper.rotateGraphicClockwise(photoFile) {
                        renderPhoto(image, photoFile)
                    }
                }
            }
            elementLayout.addView(imageButton)
        }
    }

    private fun renderPhoto(image: ImageView, photoFile: String) {
        val photo = BitmapFactory.decodeFile(photoFile)
        val resizedPhoto = PhotoHelper.scalePhotoKeepRatio(photo, 250f, 300f)
        image.setImageBitmap(resizedPhoto)
    }

    private fun toPx(dp: Int): Int = (dp * view.context.resources.displayMetrics.density).toInt()

    private fun addListener(selected: ImageButton, element: ElementDescription) {
        selected.setOnClickListener {
            if (element.isSelected) {
                element.isSelected = false
                selected.setImageResource(R.drawable.checkbox_empty)
            } else {
                element.isSelected = true
                selected.setImageResource(R.drawable.checkbox_checked)
            }
        }
    }

}
