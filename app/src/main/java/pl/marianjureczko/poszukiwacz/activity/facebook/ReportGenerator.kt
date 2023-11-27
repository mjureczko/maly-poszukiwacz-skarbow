package pl.marianjureczko.poszukiwacz.activity.facebook

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.core.content.res.ResourcesCompat
import pl.marianjureczko.poszukiwacz.R

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
        canvas.drawColor(Color.WHITE)

        var currentTop = 0f
        title.draw(canvas)
        currentTop += title.height()

        summary.draw(canvas, currentTop)
        currentTop += summary.height()

        commemorativePhotos.draw(canvas, currentTop)
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

}