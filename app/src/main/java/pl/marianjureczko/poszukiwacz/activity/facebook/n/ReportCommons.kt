package pl.marianjureczko.poszukiwacz.activity.facebook.n

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface

class ReportCommons {
    companion object {
        const val HEADER_FONT_SIZE = 36f
        const val REPORT_WIDTH = 1000
        const val REPORT_MARGIN = 50f
        const val HEADER_HORIZONTAL_SPACING = 10f
        fun reportWidthAsFloat(): Float = REPORT_WIDTH.toFloat();
        fun reportMarginAsFloat(): Int = REPORT_MARGIN.toInt()

        fun getHeaderPaint(font: Typeface): Paint =
            Paint().apply {
                color = Color.BLACK
                textSize = HEADER_FONT_SIZE
                textAlign = Paint.Align.CENTER
                typeface = font
            }
        fun getTextPaint(font: Typeface, align: Paint.Align) =
            Paint().apply {
                color = Color.BLACK
                textSize = 26f
                textAlign = align
                typeface = font
            }
        fun getTitlePaint(font: Typeface) =
            Paint().apply {
                color = Color.BLUE
                textSize = 46f
                textAlign = Paint.Align.CENTER
                typeface = font
            }
    }
}