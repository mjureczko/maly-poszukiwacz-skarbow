package pl.marianjureczko.poszukiwacz.activity.facebook

import android.content.Context
import android.graphics.Canvas
import android.graphics.Typeface
import pl.marianjureczko.poszukiwacz.R

class ReportMapHeader(
    private val model: FacebookViewModel,
    private val font: Typeface
) : ReportPart {

    //TODO: ReportCommemorativePhotos has the same, maybe a header abstraction...
    private val HORIZONTAL_SPACING = 10f
    override fun height(): Float {
        return if (mapData()) {
            50f
        } else {
            0f
        }
    }

    fun draw(context: Context, canvas: Canvas, currentTop: Float) {
        if (mapData()) {
            val headerPaint = ReportCommons.getHeaderPaint(font)
            val txt = context.getString(R.string.expedition_map)
            val y = currentTop + ReportCommons.HEADER_FONT_SIZE + HORIZONTAL_SPACING
            canvas.drawText(txt, ReportGenerator.width() / 2, y, headerPaint)
        }
    }

    private fun mapData(): Boolean {
        val mapSelected = model.getMap()?.isSelected ?: false
        val mapSummarySelected = model.getMapSummary()?.isSelected ?: false
        return mapSelected || mapSummarySelected
    }
}