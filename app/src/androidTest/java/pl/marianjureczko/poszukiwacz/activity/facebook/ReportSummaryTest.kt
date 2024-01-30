package pl.marianjureczko.poszukiwacz.activity.facebook

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import pl.marianjureczko.poszukiwacz.model.Treasure
import pl.marianjureczko.poszukiwacz.model.TreasureType
import java.io.File

// Requires phone with API 34
@RunWith(AndroidJUnit4::class)
internal class ReportSummaryTest : ReportAbstractTest() {

    @Test
    fun shouldDrawSummary() {
        //given
        val reportSummary = ReportSummary(model, font)
        val bitmap = Bitmap.createBitmap(ReportCommons.REPORT_WIDTH, 200, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)

        val treasure = Treasure("1", 17, TreasureType.GOLD)
        treasuresProgress.collect(treasure)
        model.initialize(context, null, treasuresProgress)

        //when
        reportSummary.draw(context, canvas, 0f)

        //then
        //save to hava a reference in case of failing test, image available at /data/data/pl.marianjureczko.poszukiwacz/files/summary.png
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, File(context.getFilesDir(), "/summary.png").outputStream())
        assertTrue(bitmap.sameAs(expected("summary.png")))
    }
}