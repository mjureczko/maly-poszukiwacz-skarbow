package pl.marianjureczko.poszukiwacz.screen.facebook

import android.content.Context
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

//Prepared for Pixel 6a API 34
@RunWith(AndroidJUnit4::class)
internal class ReportSummaryTest : ReportAbstractTest() {

    @Test
    fun shouldDrawSummary() {
        //given
        val bitmap = Bitmap.createBitmap(ReportCommons.REPORT_WIDTH, 200, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)

        val treasureGold = Treasure("1", 17, TreasureType.GOLD)
        treasuresProgress.collect(treasureGold, null)
        val treasureRuby = Treasure("2", 23, TreasureType.RUBY)
        treasuresProgress.collect(treasureRuby, null)
        storageHelper.save(treasuresProgress)
        val reportSummary = ReportSummary(createFacebookViewModel().state.value, font)

        //when
        reportSummary.draw(context, canvas, 0f)

        //then
        //save to hava a reference in case of failing test, image available at /data/data/pl.marianjureczko.poszukiwacz/files/summary.png
        val fileName = "summary.png"
        val stream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.flush()
        stream.close()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, File(context.getFilesDir(), "/$fileName").outputStream())
        assertTrue(bitmap.sameAs(expected(fileName)))
    }
}