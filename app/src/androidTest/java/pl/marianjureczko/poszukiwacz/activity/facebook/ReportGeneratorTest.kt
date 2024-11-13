package pl.marianjureczko.poszukiwacz.activity.facebook

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Test
import org.junit.runner.RunWith
import pl.marianjureczko.poszukiwacz.activity.facebook.n.FacebookViewModel
import pl.marianjureczko.poszukiwacz.activity.facebook.n.ROUTE_NAME
import pl.marianjureczko.poszukiwacz.activity.facebook.n.ReportGenerator
import pl.marianjureczko.poszukiwacz.activity.facebook.n.Type
import pl.marianjureczko.poszukiwacz.model.HunterPath
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.Treasure
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.model.TreasureType
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
import pl.marianjureczko.poszukiwacz.shared.Coordinates
import pl.marianjureczko.poszukiwacz.shared.port.StorageHelper
import java.io.File
import java.util.Date

@RunWith(AndroidJUnit4::class)
class ReportGeneratorTest {
    private val testDispatcher = StandardTestDispatcher()

    @Test
    fun shouldCreateImage() {
        //given
        val report = ReportGenerator()
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val storageHelper: StorageHelper = StorageHelper(context)
        val photos = arrangePhotos(context)
        val treasuresProgress = TreasuresProgress(ROUTE_NAME, TreasureDescription.nullObject())
        val treasure = Treasure("1", 7, TreasureType.DIAMOND)
        treasuresProgress.collect(treasure)
        treasuresProgress.commemorativePhotosByTreasuresDescriptionIds.put(1, photos[0])
        treasuresProgress.commemorativePhotosByTreasuresDescriptionIds.put(2, photos[1])
        treasuresProgress.commemorativePhotosByTreasuresDescriptionIds.put(3, photos[2])
        treasuresProgress.commemorativePhotosByTreasuresDescriptionIds.put(13, photos[4])
        treasuresProgress.commemorativePhotosByTreasuresDescriptionIds.put(0, photos[5])
        storageHelper.save(treasuresProgress)
        val hunterPath = HunterPath(ROUTE_NAME)
        hunterPath.addLocation(Coordinates(10.0, 10.0), Date(1))
        hunterPath.addLocation(Coordinates(10.0, 11.0), Date(1_000_000))
        hunterPath.addLocation(Coordinates(10.0, 11.0), Date(2_000_000))
        storageHelper.save(hunterPath)
        StorageHelper(context).save(Route(treasuresProgress.routeName))

        //when
        val model = FacebookViewModel(StorageHelper(context), context.resources, testDispatcher, testDispatcher)
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

    private fun arrangePhotos(context: Context) : List<String>{
        val input = InstrumentationRegistry.getInstrumentation().context.resources.assets.open("tmp.jpg")
        val source = BitmapFactory.decodeStream(input)
        return (0..5).map {
            val scaled = Bitmap.createScaledBitmap(source, 250, 350 - (50 * it), false)
            val file: File = File(context.getFilesDir(), "tmp_$it.png")
            scaled.compress(Bitmap.CompressFormat.PNG, 100, file.outputStream())
            file.absolutePath
        }.toList()
    }
}