package pl.marianjureczko.poszukiwacz.activity.facebook

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress

class ReportTitle(
    private val model: FacebookViewModel,
    private val font: Typeface
) : ReportPart {
    override fun height(): Float = 150f

    fun draw(context: Context, canvas: Canvas) {
        val titlePaint = Paint().apply {
            color = Color.BLUE
            textSize = 46f
            textAlign = Paint.Align.CENTER
            typeface = font
        }
        title(context, canvas, titlePaint, model.progress)
    }

    private fun title(context: Context, canvas: Canvas, titlePaint: Paint, progress: TreasuresProgress) {
        canvas.drawText(context.getString(R.string.app_name), ReportGenerator.width() / 2, 100f, titlePaint)
        val maxTitleLength = 20
        val routeName = if (progress.routeName.length > maxTitleLength) "${progress.routeName.subSequence(0, maxTitleLength)}..." else progress.routeName
        canvas.drawText("${context.getString(R.string.report_from_expedition)} \"$routeName\"", ReportGenerator.width() / 2, 150f, titlePaint)
    }
}