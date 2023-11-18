package pl.marianjureczko.poszukiwacz.activity.facebook

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress

class ReportSummary(
    private val model: FacebookViewModel,
    private val font: Typeface
) : ReportPart {

    override fun height(): Float =
        if (model.getSummaryElement().isSelected) {
            50f
        } else {
            0f
        }

    fun draw(canvas: Canvas, currentTop: Float) {
        if (model.getSummaryElement().isSelected) {
            val textPaint = ReportCommons.getTextPaint(font, Paint.Align.LEFT)

            val summarySize = textPaint.measureText(summaryText(model.progress))
            val textY = currentTop + 50
            var x = ReportGenerator.margin
            canvas.drawText(summaryText(model.progress), x, textY, textPaint)
            val gold = IconHelper.loadIcon(R.drawable.gold, 40)
            x += summarySize
            canvas.drawBitmap(gold, x, currentTop + 20, null)

            val diamonds = "${model.progress.diamonds}"
            val diamondsSize = textPaint.measureText(diamonds)
            x += gold.width + 20
            canvas.drawText(diamonds, x, textY, textPaint)
            val diamond = IconHelper.loadIcon(R.drawable.diamond, 50)
            x += diamondsSize
            canvas.drawBitmap(diamond, x, currentTop + 10, null)

            val rubies = "${model.progress.rubies}"
            val rubiesSize = textPaint.measureText(rubies)
            x += diamond.width + 20
            canvas.drawText(rubies, x, textY, textPaint)
            val ruby = IconHelper.loadIcon(R.drawable.ruby, 50)
            x += rubiesSize
            canvas.drawBitmap(ruby, 565f, currentTop + 10, null);
        }
    }

    private fun summaryText(progress: TreasuresProgress): String {
        val count = progress.numberOfCollectedTreasures()
        var treasures = "skarbów"
        if (count == 1) treasures = "skarb"
        else if (count in 2..4) treasures = "skarby"
        return "Wynik poszukiwań to $count $treasures: ${progress.golds}"
    }
}

