package pl.marianjureczko.poszukiwacz.activity.facebook

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Typeface
import pl.marianjureczko.poszukiwacz.App
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress

class ReportSummary(
    private val model: FacebookViewModel,
    private val font: Typeface
) : ReportPart {

    val iconHelper = IconHelper()

    override fun height(): Float =
        if (model.getSummaryElement().isSelected) {
            50f
        } else {
            0f
        }

    fun draw(canvas: Canvas, currentTop: Float) {
        if (model.getSummaryElement().isSelected) {
            val textPaint = Paint().apply {
                color = Color.BLACK
                textSize = 26f
                textAlign = Paint.Align.LEFT
                typeface = font
            }

            val summarySize = textPaint.measureText(summaryText(model.progress))
            val textY = currentTop + 50
            var x = ReportGenerator.margin
            canvas.drawText(summaryText(model.progress), x, textY, textPaint)
            val gold = iconHelper.loadTreasureTypeIcon(R.drawable.gold)
            x += summarySize
            canvas.drawBitmap(gold, x, currentTop + 20, null)

            val diamonds = "${model.progress.diamonds}"
            val diamondsSize = textPaint.measureText(diamonds)
            x += gold.width + 20
            canvas.drawText(diamonds, x, textY, textPaint)
            val diamond = iconHelper.loadTreasureTypeIcon(R.drawable.diamond)
            x += diamondsSize
            canvas.drawBitmap(diamond, x, currentTop + 10, null)

            val rubies = "${model.progress.rubies}"
            val rubiesSize = textPaint.measureText(rubies)
            x += diamond.width + 20
            canvas.drawText(rubies, x, textY, textPaint)
            val ruby = iconHelper.loadTreasureTypeIcon(R.drawable.ruby)
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

class IconHelper() {

    private val matrix = Matrix()

    init {
        matrix.postScale(0.03f, 0.03f)
    }

    fun loadTreasureTypeIcon(resource: Int): Bitmap {
        val icon: Bitmap = BitmapFactory.decodeResource(App.getResources(), resource)
        return Bitmap.createBitmap(icon, 0, 0, icon.width, icon.height, matrix, false)
    }

}