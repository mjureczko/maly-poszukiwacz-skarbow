package pl.marianjureczko.poszukiwacz.activity.facebook

import android.graphics.Canvas
import android.graphics.Paint
import pl.marianjureczko.poszukiwacz.R

class ReportFooter() : ReportPart {
    override fun height(): Float = 100.0f

    fun draw(canvas: Canvas, currentTop: Float) {
        val footerImg = IconHelper.loadIcon(R.drawable.facebook_report_footer, 41)
        val x = (ReportGenerator.reportWidth.toFloat() - footerImg.width) / 2
        canvas.drawBitmap(footerImg, x, currentTop + 50f, Paint())
    }
}