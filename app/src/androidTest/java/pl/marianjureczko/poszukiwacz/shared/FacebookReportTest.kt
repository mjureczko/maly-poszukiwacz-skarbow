package pl.marianjureczko.poszukiwacz.shared

import android.content.Context
import android.graphics.Bitmap
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Test
import org.junit.runner.RunWith
import pl.marianjureczko.poszukiwacz.model.Treasure
import pl.marianjureczko.poszukiwacz.model.TreasureType
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress

@RunWith(AndroidJUnit4::class)
class FacebookReportTest {
    @Test
    fun shouldCreateImage() {
        //given
        val report = FacebookReport()
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

        //when
        val actual = report.create(context, treasuresProgress)

        //then
        val fileName = "TEST_REPORT.jpeg"
        val stream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
        actual.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        stream.flush()
        stream.close()

        //TODO: check the image at /data/data/pl.marianjureczko.poszukiwacz/files/TEST_REPORT.jpeg
    }
}