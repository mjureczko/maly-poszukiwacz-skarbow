package pl.marianjureczko.poszukiwacz.activity.facebook

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress

class ReportTitle(
    private val model: FacebookViewModel,
    private val font: Typeface
) : ReportPart {
    override fun height(): Float = 150f

    fun draw(canvas: Canvas) {
        val titlePaint = Paint().apply {
            color = Color.BLUE
            textSize = 46f
            textAlign = Paint.Align.CENTER
            typeface = font
        }
        title(canvas, titlePaint, model.progress)
    }

    private fun title(canvas: Canvas, titlePaint: Paint, progress: TreasuresProgress) {
        canvas.drawText("Mały Poszukiwacz Skarbów", ReportGenerator.width() / 2, 100f, titlePaint)
        val maxTitleLength = 20
        val routeName = if (progress.routeName.length > maxTitleLength) "${progress.routeName.subSequence(0, maxTitleLength)}..." else progress.routeName
        canvas.drawText("Raport z wyprawy, trasa \"$routeName\"", ReportGenerator.width() / 2, 150f, titlePaint)
    }
}