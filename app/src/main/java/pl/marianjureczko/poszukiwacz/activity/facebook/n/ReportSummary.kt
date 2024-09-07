package pl.marianjureczko.poszukiwacz.activity.facebook.n

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress

class ReportSummary(
    private val model: FacebookReportModel,
    private val font: Typeface
) : ReportPart {

    override fun height(): Float =
        if (model.getSummaryElement().isSelected) {
            50f
        } else {
            0f
        }

    fun draw(context: Context, canvas: Canvas, currentTop: Float) {
        if (model.getSummaryElement().isSelected) {
            val textPaint = ReportCommons.getTextPaint(font, Paint.Align.LEFT)

            val summaryText = summaryText(context, model.progress)
            val summarySize = textPaint.measureText(summaryText)
            val textY = currentTop + 50
            var x = ReportCommons.REPORT_MARGIN
            canvas.drawText(summaryText, x, textY, textPaint)
            val gold = IconHelper.loadIcon(context.resources, R.drawable.gold, 40)
            x += summarySize
            canvas.drawBitmap(gold, x, currentTop + 20, null)

            val diamonds = "${model.progress.diamonds}"
            val diamondsSize = textPaint.measureText(diamonds)
            x += gold.width + 20
            canvas.drawText(diamonds, x, textY, textPaint)
            val diamond = IconHelper.loadIcon(context.resources, R.drawable.diamond, 50)
            x += diamondsSize
            canvas.drawBitmap(diamond, x, currentTop + 10, null)

            val rubies = "${model.progress.rubies}"
            val rubiesSize = textPaint.measureText(rubies)
            x += diamond.width + 20
            canvas.drawText(rubies, x, textY, textPaint)
            val ruby = IconHelper.loadIcon(context.resources, R.drawable.ruby, 50)
            x += rubiesSize
            canvas.drawBitmap(ruby, x, currentTop + 10, null)
        }
    }

    private fun summaryText(context: Context, progress: TreasuresProgress): String {
        val treasure1 = context.getString(R.string.treasure1)
        val treasure2 = context.getString(R.string.treasure2)
        val treasureMany = context.getString(R.string.treasure_many)
        val count = progress.numberOfCollectedTreasures()
        var treasures = treasureAsWord(treasure1, treasure2, treasureMany, count)
        return "${context.getString(R.string.we_found)} $count $treasures: ${progress.golds}"
    }
}

fun treasureAsWord(one: String, two: String, many: String, quantity: Int): String {
    return if (quantity == 1) {
        one;
    } else if (quantity in 2..4) {
        two;
    } else {
        many;
    }
}