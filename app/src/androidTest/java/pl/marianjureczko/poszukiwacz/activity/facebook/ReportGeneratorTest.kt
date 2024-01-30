package pl.marianjureczko.poszukiwacz.activity.facebook

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.SavedStateHandle
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Test
import org.junit.runner.RunWith
import pl.marianjureczko.poszukiwacz.activity.treasureselector.Coordinates
import pl.marianjureczko.poszukiwacz.model.HunterPath
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.Treasure
import pl.marianjureczko.poszukiwacz.model.TreasureType
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
import pl.marianjureczko.poszukiwacz.shared.StorageHelper
import java.io.File
import java.util.Date

@RunWith(AndroidJUnit4::class)
class ReportGeneratorTest {
    @Test
    fun shouldCreateImage() {
        //given
        val report = ReportGenerator()
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val photos = arrangePhotos(context)
        val treasuresProgress = TreasuresProgress("123456789 123456789 123456789 123456789 12345")
        val treasure = Treasure("1", 7, TreasureType.DIAMOND)
        treasuresProgress.collect(treasure)
        treasuresProgress.commemorativePhotosByTreasuresDescriptionIds.put(1, photos[0])
        treasuresProgress.commemorativePhotosByTreasuresDescriptionIds.put(2, photos[1])
        treasuresProgress.commemorativePhotosByTreasuresDescriptionIds.put(3, photos[2])
        treasuresProgress.commemorativePhotosByTreasuresDescriptionIds.put(13, photos[4])
        treasuresProgress.commemorativePhotosByTreasuresDescriptionIds.put(0, photos[5])
        val hunterPath = HunterPath()
        hunterPath.addLocation(Coordinates(10.0, 10.0), Date(1))
        hunterPath.addLocation(Coordinates(10.0, 11.0), Date(1_000_000))
        hunterPath.addLocation(Coordinates(10.0, 11.0), Date(2_000_000))
        StorageHelper(context).save(Route(treasuresProgress.routeName))

        //when
        val model = FacebookViewModel(SavedStateHandle(mapOf()))
        model.initialize(context, hunterPath, treasuresProgress)
        //MapBox doesn't work in tests
        model.getMap()?.isSelected = false
        val actual = report.create(context, model) {
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