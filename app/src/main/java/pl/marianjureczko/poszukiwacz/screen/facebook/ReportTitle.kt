package pl.marianjureczko.poszukiwacz.screen.facebook

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress

class ReportTitle(
    private val model: FacebookReportState,
    private val font: Typeface
) : ReportPart {
    override fun height(): Float = 150f

    fun draw(context: Context, canvas: Canvas) {
        val titlePaint = ReportCommons.getTitlePaint(font)
        title(context, canvas, titlePaint, model.progress)
    }

    private fun title(context: Context, canvas: Canvas, titlePaint: Paint, progress: TreasuresProgress) {
        canvas.drawText(context.getString(R.string.generic_app_name), ReportCommons.reportWidthAsFloat() / 2, 100f, titlePaint)
        val maxTitleLength = 20
        var routeName = getRouteName(context, progress)
        routeName = if (routeName.length > maxTitleLength) "${routeName.subSequence(0, maxTitleLength)}..." else routeName
        canvas.drawText("${context.getString(R.string.report_from_expedition)} \"$routeName\"", ReportCommons.reportWidthAsFloat() / 2, 150f, titlePaint)
    }
}