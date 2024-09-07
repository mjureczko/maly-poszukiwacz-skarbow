package pl.marianjureczko.poszukiwacz.activity.facebook.n

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import pl.marianjureczko.poszukiwacz.R

class ReportFooter() : ReportPart {
    override fun height(): Float = 100.0f

    fun draw(resources: Resources, canvas: Canvas, currentTop: Float) {
        val footerImg = IconHelper.loadIcon(resources, R.drawable.facebook_report_footer, 41)
        val x = (ReportCommons.REPORT_WIDTH.toFloat() - footerImg.width) / 2
        canvas.drawBitmap(footerImg, x, currentTop + 50f, Paint())
    }
}