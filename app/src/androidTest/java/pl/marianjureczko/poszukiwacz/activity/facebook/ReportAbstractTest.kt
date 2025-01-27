package pl.marianjureczko.poszukiwacz.activity.facebook

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Typeface
import androidx.core.content.res.ResourcesCompat
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Before
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.activity.facebook.n.FacebookViewModel
import pl.marianjureczko.poszukiwacz.activity.facebook.n.ROUTE_NAME
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
import pl.marianjureczko.poszukiwacz.shared.port.StorageHelper

abstract class ReportAbstractTest {
    val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    val storageHelper: StorageHelper = StorageHelper(context)

    val font: Typeface = ResourcesCompat.getFont(context, R.font.akaya_telivigala)!!
    val treasuresProgress: TreasuresProgress = TreasuresProgress(ROUTE_NAME, 0)

    private val testDispatcher = StandardTestDispatcher()

    fun createFacebookViewModel() = FacebookViewModel(storageHelper, context.resources, testDispatcher, testDispatcher)

    fun saveEmptyProgress() = storageHelper.save(treasuresProgress)

    fun expected(fileName: String): Bitmap {
        val inputStream = InstrumentationRegistry.getInstrumentation().context.resources.assets.open(fileName)
        return BitmapFactory.decodeStream(inputStream)
    }

    @Before
    fun setup() {
        val route = Route(ROUTE_NAME)
        storageHelper.save(route)
    }
}