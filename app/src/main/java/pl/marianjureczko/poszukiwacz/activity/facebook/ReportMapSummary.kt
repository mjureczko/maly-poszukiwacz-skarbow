package pl.marianjureczko.poszukiwacz.activity.facebook

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Build
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.model.HunterPath
import java.text.DateFormat
import java.util.Locale

class ReportMapSummary(
    private val model: FacebookViewModel,
    private val font: Typeface
) : ReportPart {

    private var userLocale: Locale = Locale.getDefault()
    override fun height(): Float =
        if (model.getSummaryElement().isSelected) {
            100.0f
        } else {
            0.0f
        }

    fun draw(context: Context, canvas: Canvas, currentTop: Float) {
        if (model.getSummaryElement().isSelected) {
            userLocale = userLocale(context)
            val textPaint = ReportCommons.getTextPaint(font, Paint.Align.LEFT)
            var textY = currentTop + 50
            var x = ReportCommons.REPORT_MARGIN
            canvas.drawText(distanceText(context, model.progress.hunterPath), x, textY, textPaint)
            textY += 40
            canvas.drawText(timeText(context, model.progress.hunterPath), x, textY, textPaint)
        }
    }

    private fun userLocale(context: Context): Locale =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.getResources().getConfiguration().locales[0]
        } else {
            context.getResources().getConfiguration().locale
        }

    private fun distanceText(context: Context, hunterPath: HunterPath): String {
        val formattedDistance = "%.2f".format(hunterPath.pathLengthInKm())
        return "${context.getString(R.string.walked_route)} $formattedDistance km."
    }

    private fun timeText(context: Context, hunterPath: HunterPath): String {
        val loc = Locale("en", "US")
        val timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT, loc)
        val dateFormat: DateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, loc)

        hunterPath.getStartTime()?.let { start ->
            val startDate = dateFormat.format(start)
            val startTime = timeFormat.format(start)
            hunterPath.getEndTime()?.let { end ->
                val endDate = dateFormat.format(end)
                val endTime = timeFormat.format(end)
                return "${context.getString(R.string.expedition_started)} $startDate, $startTime ${context.getString(R.string.expedition_ended)} $endDate, $endTime."
            }
        }
        return ""
    }
}