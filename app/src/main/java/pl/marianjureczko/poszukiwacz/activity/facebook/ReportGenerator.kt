package pl.marianjureczko.poszukiwacz.activity.facebook

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.res.ResourcesCompat
import pl.marianjureczko.poszukiwacz.R
import java.util.Collections

interface ReportPublisher {
    fun publish(bitmap: Bitmap)
}

interface ReportPart {
    fun height(): Float
}

class ReportGenerator {
    //TODO: move to commons
    companion object {
        const val reportWidth = 1000
        fun width(): Float = reportWidth.toFloat();
        const val margin = 50f
        fun margin(): Int = margin.toInt()
    }

    /**
     * @return it is returned for testing purposes
     */
    fun create(context: Context, model: FacebookViewModel, reportPublisher: (Bitmap) -> Unit): Bitmap {
        //TODO: handle missing font
        val typeface = ResourcesCompat.getFont(context, R.font.akaya_telivigala)!!
        val title = ReportTitle(model, typeface)
        val summary = ReportSummary(model, typeface)
        val commemorativePhotos = ReportCommemorativePhotos(model, typeface)
        val mapHeader = ReportMapHeader(model, typeface)
        val map = ReportMap(model)
        val mapSummary = ReportMapSummary(model, typeface)
        val footer = ReportFooter()
        val height = title.height() + summary.height() + commemorativePhotos.height() + mapHeader.height() + map.height() + mapSummary.height() + footer.height()
        val bitmap = Bitmap.createBitmap(reportWidth, height.toInt(), Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)
        createBackground(canvas, height)

        var currentTop = 0f
        title.draw(context, canvas)
        currentTop += title.height()

        summary.draw(context, canvas, currentTop)
        currentTop += summary.height()

        commemorativePhotos.draw(context, canvas, currentTop)
        currentTop += commemorativePhotos.height()

        mapHeader.draw(context, canvas, currentTop)
        currentTop += mapHeader.height()

        map.draw(context, canvas, currentTop) { reportPublisher(bitmap) }
        currentTop += map.height()

        mapSummary.draw(context, canvas, currentTop)
        currentTop += mapSummary.height()

        footer.draw(canvas, currentTop)

        return bitmap
    }

    private fun createBackground(canvas: Canvas, height: Float) {
        val backgroundHeight = 1127
        val background = IconHelper.loadIcon(R.drawable.background, backgroundHeight)
        canvas.drawBitmap(background, 0f, 0f, null)
        if (height > backgroundHeight) {
            val partHeight = 138
            val background1 = IconHelper.loadIcon(R.drawable.background_1, partHeight)
            val background2 = IconHelper.loadIcon(R.drawable.background_2, partHeight)
            val parts = mutableListOf(background1, background2)
            var currentY = backgroundHeight.toFloat()
            while (currentY < height) {
                canvas.drawBitmap(parts[0], 0f, currentY, null)
                currentY += partHeight
                Collections.swap(parts, 0, 1)
            }
        }
    }

}