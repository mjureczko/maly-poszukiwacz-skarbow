package pl.marianjureczko.poszukiwacz.screen.facebook

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

//Prepared for Pixel 6a API 34
@RunWith(AndroidJUnit4::class)
class ReportMapHeaderTest : ReportAbstractTest() {
    @Test
    fun shouldDrawSummary() {
        //given
        val bitmap = Bitmap.createBitmap(ReportCommons.REPORT_WIDTH, 200, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)
        saveEmptyProgress()
        val reportMapHeader = ReportMapHeader(createFacebookViewModel().state.value, font)

        //when
        reportMapHeader.draw(context, canvas, 0f)

        //then
        //save to hava a reference in case of failing test
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, File(context.getFilesDir(), "/map_header.png").outputStream())
        TestCase.assertTrue(bitmap.sameAs(expected("map_header.png")))
    }
}