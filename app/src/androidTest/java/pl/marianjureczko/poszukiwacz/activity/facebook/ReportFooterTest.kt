package pl.marianjureczko.poszukiwacz.activity.facebook

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase
import org.junit.Test
import org.junit.runner.RunWith
import pl.marianjureczko.poszukiwacz.activity.facebook.n.ReportCommons
import pl.marianjureczko.poszukiwacz.activity.facebook.n.ReportFooter
import java.io.File

// Requires phone with API 34
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
        footer.draw(canvas, 0f)

        //then
        //save to hava a reference in case of failing test
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, File(context.getFilesDir(), "/map_footer.png").outputStream())
        TestCase.assertTrue(bitmap.sameAs(expected("map_footer.png")))
    }
}