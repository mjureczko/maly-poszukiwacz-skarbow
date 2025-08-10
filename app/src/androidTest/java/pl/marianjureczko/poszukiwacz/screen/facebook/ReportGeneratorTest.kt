package pl.marianjureczko.poszukiwacz.screen.facebook

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.SavedStateHandle
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Test
import org.junit.runner.RunWith
import pl.marianjureczko.poszukiwacz.model.HunterPath
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.Treasure
import pl.marianjureczko.poszukiwacz.model.TreasureType
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
import pl.marianjureczko.poszukiwacz.shared.port.storage.StoragePort
import pl.marianjureczko.poszukiwacz.usecase.TestLocation
import java.io.File

@RunWith(AndroidJUnit4::class)
class ReportGeneratorTest {
    private val testDispatcher = StandardTestDispatcher()

    @Test
    fun shouldCreateImage() {
        //given
        val routeName = "custom"
        val report = ReportGenerator()
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val storagePort: StoragePort = StoragePort(context)
        val photos = arrangePhotos(context)
        var treasuresProgress = TreasuresProgress(routeName, 0)
        val treasure = Treasure("1", 7, TreasureType.DIAMOND)
        treasuresProgress.collect(treasure, null)
        val mapOfPhotos = treasuresProgress.commemorativePhotosByTreasuresDescriptionIds + mapOf(
            1 to photos[0],
            2 to photos[1],
            3 to photos[2],
            13 to photos[4],
            0 to photos[5],
        )
        treasuresProgress = treasuresProgress.copy(
            commemorativePhotosByTreasuresDescriptionIds = mapOfPhotos.toMutableMap()
        )
        storagePort.save(treasuresProgress)
        val hunterPath = HunterPath(routeName)
            .addLocation(TestLocation(10.0, 10.0, observedAt = 1))
            .addLocation(TestLocation(10.0, 11.0, observedAt = 1_000_000))
            .addLocation(TestLocation(10.0, 11.0, observedAt = 2_000_000))
        storagePort.save(hunterPath)
        StoragePort(context).save(Route(treasuresProgress.routeName))
        val stateHandle: SavedStateHandle = SavedStateHandle(mapOf(PARAMETER_ROUTE_NAME to routeName))

        //when
        val model =
            FacebookViewModel(stateHandle, StoragePort(context), context.resources, testDispatcher, testDispatcher)
//        //MapBox doesn't work in tests
        var state = model.state.value
        val mapIdx = state.elements.indices.find { state.elements[it].type == Type.MAP }!!
        val elements = state.elements.toMutableList()
        elements[mapIdx] = state.elements[mapIdx].copy(isSelected = false)
        state = state.copy(elements = elements)

        val actual = report.create(context, state) {
            // do nothing
        }

        //then
        val fileName = "TEST_REPORT.jpeg"
        val stream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
        actual.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        stream.flush()
        stream.close()

        //TODO: check the image at /data/data/pl.marianjureczko.poszukiwacz/files/TEST_REPORT.jpeg
    }

    private fun arrangePhotos(context: Context): List<String> {
        val input = InstrumentationRegistry.getInstrumentation().context.resources.assets.open("sample_photo.jpg")
        val source = BitmapFactory.decodeStream(input)
        return (0..5).map {
            val scaled = Bitmap.createScaledBitmap(source, 250, 350 - (50 * it), false)
            val file: File = File(context.getFilesDir(), "tmp_$it.png")
            scaled.compress(Bitmap.CompressFormat.PNG, 100, file.outputStream())
            file.absolutePath
        }.toList()
    }
}