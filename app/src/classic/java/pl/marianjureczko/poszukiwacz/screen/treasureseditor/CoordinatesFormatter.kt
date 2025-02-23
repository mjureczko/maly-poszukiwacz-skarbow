package pl.marianjureczko.poszukiwacz.screen.treasureseditor

import java.text.NumberFormat
import java.util.Locale

class CoordinatesFormatter {

    fun format(cord: Double?): String {
        if (cord == null) {
            return "??.?????"
        }
        val nf: NumberFormat = NumberFormat.getInstance(Locale.UK)
        nf.minimumFractionDigits = 5
        nf.maximumFractionDigits = 5
        nf.minimumIntegerDigits = 1
        nf.maximumIntegerDigits = 3

        return nf.format(cord)
    }
}