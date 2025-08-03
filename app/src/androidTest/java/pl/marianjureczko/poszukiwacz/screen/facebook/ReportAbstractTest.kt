package pl.marianjureczko.poszukiwacz.screen.facebook

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Typeface
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.SavedStateHandle
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Before
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
import pl.marianjureczko.poszukiwacz.shared.port.storage.StoragePort

abstract class ReportAbstractTest {
    val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    val storagePort: StoragePort = StoragePort(context)
    val routeName = "custom"
    private val stateHandle: SavedStateHandle = SavedStateHandle(mapOf(PARAMETER_ROUTE_NAME to routeName))

    val font: Typeface = ResourcesCompat.getFont(context, R.font.akaya_telivigala)!!
    var treasuresProgress: TreasuresProgress = TreasuresProgress(routeName, 0)

    private val testDispatcher = StandardTestDispatcher()

    fun createFacebookViewModel() =
        FacebookViewModel(stateHandle, storagePort, context.resources, testDispatcher, testDispatcher)

    fun saveEmptyProgress() = storagePort.save(treasuresProgress)

    fun expected(fileName: String): Bitmap {
        val inputStream = InstrumentationRegistry.getInstrumentation().context.resources.assets.open(fileName)
        return BitmapFactory.decodeStream(inputStream)
    }

    @Before
    fun setup() {
        val route = Route(routeName)
        storagePort.save(route)
    }
}