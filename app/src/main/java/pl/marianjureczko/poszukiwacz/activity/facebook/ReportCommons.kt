package pl.marianjureczko.poszukiwacz.activity.facebook

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface

class ReportCommons {
    companion object {
        const val HEADER_FONT_SIZE = 36f

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

    }
}