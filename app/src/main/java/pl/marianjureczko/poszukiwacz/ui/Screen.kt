package pl.marianjureczko.poszukiwacz.ui

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.window.layout.WindowMetricsCalculator
import pl.marianjureczko.poszukiwacz.activity.main.MainActivity

object Screen {
    var WIDTH = 1080f
        private set
    var HEIGHT = 2280f
        private set

    var DENSITY = 2.75f
        private  set
    fun init(activity: MainActivity) {
        WindowMetricsCalculator
            .getOrCreate()
            .computeCurrentWindowMetrics(activity)
            .bounds.let {
                WIDTH = it.width().toFloat()
                HEIGHT = it.height().toFloat()
            }
        DENSITY = activity.resources.displayMetrics.density
    }

    inline val Number.PxToDp get() = this.toFloat() / DENSITY
    inline val Number.dw: Dp
        get() = Dp(value = (this.toFloat() * WIDTH).PxToDp)
    inline val Number.dh: Dp
        get() = Dp(value = (this.toFloat() * HEIGHT).PxToDp)
}

fun dp2SameSizeSp(dp: Dp, factor: Double = 0.75): TextUnit = ((dp.value * factor).toInt()).sp
