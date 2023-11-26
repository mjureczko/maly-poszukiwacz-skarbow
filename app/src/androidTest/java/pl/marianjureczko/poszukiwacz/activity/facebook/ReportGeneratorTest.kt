package pl.marianjureczko.poszukiwacz.activity.facebook

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.SavedStateHandle
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Test
import org.junit.runner.RunWith
import pl.marianjureczko.poszukiwacz.activity.treasureselector.Coordinates
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.Treasure
import pl.marianjureczko.poszukiwacz.model.TreasureType
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
import pl.marianjureczko.poszukiwacz.shared.StorageHelper
import java.util.Date

@RunWith(AndroidJUnit4::class)
class ReportGeneratorTest {
    @Test
    fun shouldCreateImage() {
        //given
        val report = ReportGenerator()
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val treasuresProgress = TreasuresProgress("123456789 123456789 123456789 123456789 12345")
        val treasure = Treasure("1", 7, TreasureType.DIAMOND)
        treasuresProgress.collect(treasure)
        treasuresProgress.commemorativePhotosByTreasuresDescriptionIds.put(
            1,
            "/data/data/pl.marianjureczko.poszukiwacz/files/treasures_lists/commemorativephoto_b0746921-c67e-47e9-b98c-ef9b260dc8d2.jpg"
        )
        treasuresProgress.commemorativePhotosByTreasuresDescriptionIds.put(
            2,
            "/data/data/pl.marianjureczko.poszukiwacz/files/treasures_lists/commemorativephoto_e846d747-6029-402b-878e-0de057719cb2.jpg"
        )
        treasuresProgress.commemorativePhotosByTreasuresDescriptionIds.put(
            3,
            "/data/data/pl.marianjureczko.poszukiwacz/files/treasures_lists/photo_0a182204-c56a-4a19-9d69-bc5d9e48ccb6.jpg"
        )
        treasuresProgress.commemorativePhotosByTreasuresDescriptionIds.put(
            13,
            "/data/data/pl.marianjureczko.poszukiwacz/files/treasures_lists/photo_0a182204-c56a-4a19-9d69-bc5d9e48ccb6.jpg"
        )
        treasuresProgress.commemorativePhotosByTreasuresDescriptionIds.put(
            0,
            "/data/data/pl.marianjureczko.poszukiwacz/files/treasures_lists/photo_0a182204-c56a-4a19-9d69-bc5d9e48ccb6.jpg"
        )
        treasuresProgress.hunterPath.addLocation(Coordinates(10.0, 10.0), Date(1))
        treasuresProgress.hunterPath.addLocation(Coordinates(10.0, 11.0), Date(1_000_000))
        treasuresProgress.hunterPath.addLocation(Coordinates(10.0, 11.0), Date(2_000_000))
        StorageHelper(context).save(Route(treasuresProgress.routeName))

        //when
        val model = FacebookViewModel(SavedStateHandle(mapOf()))
        model.initialize(treasuresProgress, context)
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
}