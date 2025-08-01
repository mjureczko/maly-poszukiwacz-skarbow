package pl.marianjureczko.poszukiwacz.screen.facebook

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase
import org.junit.Test
import org.junit.runner.RunWith

//Prepared for Pixel 6a API 34
@RunWith(AndroidJUnit4::class)
class ReportFooterTest : ReportAbstractTest() {

    @Test
    fun should_drawFooter() {
        //given
        val footer = ReportFooter()
        val bitmap = Bitmap.createBitmap(ReportCommons.REPORT_WIDTH, 200, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)

        //when
        footer.draw(context.resources, canvas, 0f)

        //then
        //save to hava a reference in case of failing test
        val fileName = "footer.png"
        val stream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.flush()
        stream.close()

        TestCase.assertTrue(bitmap.sameAs(expected("footer.png")))
        //stop at breakpoint and check the image at /data/data/pl.marianjureczko.poszukiwacz/files/map_footer.jpeg
    }
}