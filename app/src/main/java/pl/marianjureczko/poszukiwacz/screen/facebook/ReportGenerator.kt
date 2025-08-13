package pl.marianjureczko.poszukiwacz.screen.facebook

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.res.ResourcesCompat
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.model.HunterPath
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
import pl.marianjureczko.poszukiwacz.screen.searching.LocationCalculator
import java.util.Collections

interface ReportPart {
    fun height(): Float
}

interface FacebookReportState {
    fun getSummaryElement(): ElementDescription
    fun getCommemorativePhotoElements(): List<ElementDescription>
    fun getMap(): ElementDescription?
    fun getMapSummary(): ElementDescription?
    fun getMapRoute(): ElementDescription?

    val hunterPath: HunterPath?
    val route: Route
    val progress: TreasuresProgress
}

class ReportGenerator {
    /**
     * @return it is returned for testing purposes
     */
    fun create(
        context: Context,
        state: FacebookReportState,
        locationCalculator: LocationCalculator,
        reportPublisher: (Bitmap) -> Unit
    ): Bitmap {
        val typeface = ResourcesCompat.getFont(context, R.font.akaya_telivigala)!!
        val title = ReportTitle(state, typeface)
        val summary = ReportSummary(state, typeface)
        val commemorativePhotos = ReportCommemorativePhotos(state, typeface)
        val mapHeader = ReportMapHeader(state, typeface)
        val map = ReportMap(state)
        val mapSummary = ReportMapSummary(state, typeface, locationCalculator)
        val footer = ReportFooter()
        val height =
            title.height() + summary.height() + commemorativePhotos.height() + mapHeader.height() + map.height() + mapSummary.height() + footer.height()
        val bitmap = Bitmap.createBitmap(ReportCommons.REPORT_WIDTH, height.toInt(), Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)
        createBackground(context.resources, canvas, height)

        var currentTop = 0f
        title.draw(context, canvas)
        currentTop += title.height()

        summary.draw(context, canvas, currentTop)
        currentTop += summary.height()

        commemorativePhotos.draw(context, canvas, currentTop)
        currentTop += commemorativePhotos.height()

        mapHeader.draw(context, canvas, currentTop)
        currentTop += mapHeader.height()

        val published = map.draw(context, canvas, currentTop) { reportPublisher(bitmap) }
        currentTop += map.height()

        mapSummary.draw(context, canvas, currentTop)
        currentTop += mapSummary.height()

        footer.draw(context.resources, canvas, currentTop)

        if (!published) {
            reportPublisher(bitmap)
        }
        return bitmap
    }

    private fun createBackground(resources: Resources, canvas: Canvas, height: Float) {
        val backgroundHeight = 1127
        val background = IconHelper.loadIcon(resources, R.drawable.background, backgroundHeight)
        canvas.drawBitmap(background, 0f, 0f, null)
        if (height > backgroundHeight) {
            val partHeight = 138
            val background1 = IconHelper.loadIcon(resources, R.drawable.background_1, partHeight)
            val background2 = IconHelper.loadIcon(resources, R.drawable.background_2, partHeight)
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