package pl.marianjureczko.poszukiwacz.activity.facebook

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress

class ReportTitle(
    private val model: FacebookReportModel,
    private val font: Typeface
) : ReportPart {
    override fun height(): Float = 150f

    fun draw(context: Context, canvas: Canvas) {
        val titlePaint = ReportCommons.getTitlePaint(font)
        title(context, canvas, titlePaint, model.progress)
    }

    private fun title(context: Context, canvas: Canvas, titlePaint: Paint, progress: TreasuresProgress) {
        canvas.drawText(context.getString(R.string.app_name), ReportCommons.reportWidthAsFloat() / 2, 100f, titlePaint)
        val maxTitleLength = 20
        val routeName = if (progress.routeName.length > maxTitleLength) "${progress.routeName.subSequence(0, maxTitleLength)}..." else progress.routeName
        canvas.drawText("${context.getString(R.string.report_from_expedition)} \"$routeName\"", ReportCommons.reportWidthAsFloat() / 2, 150f, titlePaint)
    }
}