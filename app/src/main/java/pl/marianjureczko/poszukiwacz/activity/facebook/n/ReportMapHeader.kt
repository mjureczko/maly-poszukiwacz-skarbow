package pl.marianjureczko.poszukiwacz.activity.facebook.n

import android.content.Context
import android.graphics.Canvas
import android.graphics.Typeface
import pl.marianjureczko.poszukiwacz.R

class ReportMapHeader(
    private val model: FacebookReportModel,
    private val font: Typeface
) : ReportPart {
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
            val y = currentTop + ReportCommons.HEADER_FONT_SIZE + ReportCommons.HEADER_HORIZONTAL_SPACING
            canvas.drawText(txt, ReportCommons.reportWidthAsFloat() / 2, y, headerPaint)
        }
    }

    private fun mapData(): Boolean {
        val mapSelected = model.getMap()?.isSelected ?: false
        val mapSummarySelected = model.getMapSummary()?.isSelected ?: false
        return mapSelected || mapSummarySelected
    }
}