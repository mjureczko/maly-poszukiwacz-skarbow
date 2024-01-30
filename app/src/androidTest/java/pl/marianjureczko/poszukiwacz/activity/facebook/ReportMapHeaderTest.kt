package pl.marianjureczko.poszukiwacz.activity.facebook

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

// Requires phone with API 34
@RunWith(AndroidJUnit4::class)
class ReportMapHeaderTest : ReportAbstractTest() {
    @Test
    fun shouldDrawSummary() {
        //given
        val reportMapHeader = ReportMapHeader(model, font)
        val bitmap = Bitmap.createBitmap(ReportCommons.REPORT_WIDTH, 200, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)
        model.initialize(context, null, treasuresProgress)

        //when
        reportMapHeader.draw(context, canvas, 0f)

        //then
        //save to hava a reference in case of failing test
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, File(context.getFilesDir(), "/map_header.png").outputStream())
        TestCase.assertTrue(bitmap.sameAs(expected("map_header.png")))
    }
}