package pl.marianjureczko.poszukiwacz.screen.facebook

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory

class IconHelper() {

    companion object {
        /**
         * Loads resource and scales it to desired height, but keeps the ratio.
         * Is intended to scale down, for scaling up more computation costly interpolation should be used (filter parameter).
         * @param desiredHeight in pixels
         */
        fun loadIcon(resources: Resources, resource: Int, desiredHeight: Int): Bitmap {
            val rawIcon: Bitmap = BitmapFactory.decodeResource(resources, resource)
            val desiredScale = desiredHeight.toFloat() / rawIcon.height.toFloat()
            val desiredWidth = desiredScale * rawIcon.width.toFloat()
            return Bitmap.createScaledBitmap(rawIcon, desiredWidth.toInt(), desiredHeight, false)
        }
    }
}