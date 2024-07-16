package pl.marianjureczko.poszukiwacz.activity.facebook

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Typeface
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.SavedStateHandle
import androidx.test.platform.app.InstrumentationRegistry
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress

abstract class ReportAbstractTest {
    val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    val model: pl.marianjureczko.poszukiwacz.activity.facebook.FacebookViewModel =
        pl.marianjureczko.poszukiwacz.activity.facebook.FacebookViewModel(SavedStateHandle(mapOf()))
    val font: Typeface = ResourcesCompat.getFont(context, R.font.akaya_telivigala)!!
    val treasuresProgress: TreasuresProgress = TreasuresProgress("123456789", TreasureDescription.nullObject())

    fun expected(fileName: String): Bitmap {
        val inputStream = InstrumentationRegistry.getInstrumentation().context.resources.assets.open(fileName)
        return BitmapFactory.decodeStream(inputStream)
    }
}