package pl.marianjureczko.poszukiwacz.screen.facebook

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
            val summaryText = summaryText(context, model.progress, numberOfTreasuresOfFirstType())
            val summarySize = textPaint.measureText(summaryText)
            val textY = currentTop + 50
            var x = ReportCommons.REPORT_MARGIN
            canvas.drawText(summaryText, x, textY, textPaint)

            partWithIcons(context, x, summarySize, canvas, currentTop, textPaint, textY)
        }
    }

    private fun numberOfTreasuresOfFirstType(): Int {
        return model.progress.knowledge
    }

    private fun partWithIcons(
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

    private fun summaryText(context: Context, progress: TreasuresProgress, numberOfTreasuresOfFirstType: Int): String {
        val treasure1 = context.getString(R.string.treasure1)
        val treasure2 = context.getString(R.string.treasure2)
        val treasureMany = context.getString(R.string.treasure_many)
        val count = progress.numberOfCollectedTreasures()
        val treasures = treasureAsWord(treasure1, treasure2, treasureMany, count)
        return "${context.getString(R.string.we_found)} $count $treasures: $numberOfTreasuresOfFirstType"
    }

    private fun treasureAsWord(one: String, two: String, many: String, quantity: Int): String {
        return if (quantity == 1) {
            one
        } else if (quantity in 2..4) {
            two
        } else {
            many
        }
    }
}