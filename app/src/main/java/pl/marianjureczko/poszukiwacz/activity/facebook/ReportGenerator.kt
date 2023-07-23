package pl.marianjureczko.poszukiwacz.activity.facebook

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.core.content.res.ResourcesCompat
import pl.marianjureczko.poszukiwacz.R

interface ReportPart {
    fun height(): Float
}

class ReportGenerator {
    companion object {
        const val reportWidth = 1000
        fun width(): Float = reportWidth.toFloat();
        const val margin = 50f
        fun margin(): Int = margin.toInt()
    }

    fun create(context: Context, model: FacebookViewModel): Bitmap {
        //TODO: handle missing font
        val typeface = ResourcesCompat.getFont(context, R.font.akaya_telivigala)!!
        val title = ReportTitle(model, typeface)
        val summary = ReportSummary(model, typeface)
        val commemorativePhotos = ReportCommemorativePhotos(model, typeface)
        val height = title.height() + summary.height() + commemorativePhotos.height()
        val bitmap = Bitmap.createBitmap(reportWidth, height.toInt(), Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)

        var currentTop = 0f
        title.draw(canvas)
        currentTop += title.height()

        summary.draw(canvas, currentTop)
        currentTop += summary.height()

        commemorativePhotos.draw(canvas, currentTop)

        return bitmap
    }

}