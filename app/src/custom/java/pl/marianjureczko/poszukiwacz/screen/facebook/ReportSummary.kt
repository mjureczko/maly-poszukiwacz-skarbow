package pl.marianjureczko.poszukiwacz.screen.facebook

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import pl.marianjureczko.poszukiwacz.R

class ReportSummary(private val model: FacebookReportModel, font: Typeface) : AbstractReportSummary(model, font) {
    override fun numberOfTreasuresOfFirstType(): Int {
        return model.progress.knowledge
    }

    override fun partWithIcons(
        context: Context,
        x: Float,
        summarySize: Float,
        canvas: Canvas,
        currentTop: Float,
        textPaint: Paint,
        textY: Float
    ) {
        var currentX = x
        val knowledge = IconHelper.loadIcon(context.resources, R.drawable.chest_small, 40)
        currentX += summarySize
        canvas.drawBitmap(knowledge, currentX, currentTop + 20, null)
    }
}