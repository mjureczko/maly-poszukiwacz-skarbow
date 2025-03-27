package pl.marianjureczko.poszukiwacz.screen.facebook

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import pl.marianjureczko.poszukiwacz.R

class ReportSummary(private val model: FacebookReportModel, font: Typeface) : AbstractReportSummary(model, font) {
    override fun numberOfTreasuresOfFirstType(): Int {
        return model.progress.golds
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
        val gold = IconHelper.loadIcon(context.resources, R.drawable.gold, 40)
        currentX += summarySize
        canvas.drawBitmap(gold, currentX, currentTop + 20, null)

        val diamonds = "${model.progress.diamonds}"
        val diamondsSize = textPaint.measureText(diamonds)
        currentX += gold.width + 20
        canvas.drawText(diamonds, currentX, textY, textPaint)
        val diamond = IconHelper.loadIcon(context.resources, R.drawable.diamond, 50)
        currentX += diamondsSize
        canvas.drawBitmap(diamond, currentX, currentTop + 10, null)

        val rubies = "${model.progress.rubies}"
        val rubiesSize = textPaint.measureText(rubies)
        currentX += diamond.width + 20
        canvas.drawText(rubies, currentX, textY, textPaint)
        val ruby = IconHelper.loadIcon(context.resources, R.drawable.ruby, 50)
        currentX += rubiesSize
        canvas.drawBitmap(ruby, currentX, currentTop + 10, null)
    }
}
