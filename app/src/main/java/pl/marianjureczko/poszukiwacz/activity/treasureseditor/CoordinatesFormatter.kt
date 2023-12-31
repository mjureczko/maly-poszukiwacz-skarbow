package pl.marianjureczko.poszukiwacz.activity.treasureseditor

import java.text.NumberFormat
import java.util.*

class CoordinatesFormatter {

    fun format(cord: Double?): String {
        if (cord == null) {
            return "??.?????"
        }
        val nf: NumberFormat = NumberFormat.getInstance(Locale.UK)
        nf.minimumFractionDigits = 5
        nf.maximumFractionDigits = 5
        nf.minimumIntegerDigits = 2
        nf.maximumIntegerDigits = 2

        return nf.format(cord)
    }
}