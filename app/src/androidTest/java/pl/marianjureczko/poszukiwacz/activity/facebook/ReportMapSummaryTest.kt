package pl.marianjureczko.poszukiwacz.activity.facebook

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase
import org.junit.Test
import org.junit.runner.RunWith
import pl.marianjureczko.poszukiwacz.activity.treasureselector.Coordinates
import java.io.File
import java.util.Date

@RunWith(AndroidJUnit4::class)
class ReportMapSummaryTest : ReportAbstractTest() {

    @Test
    fun should_drawSummaryWithLengthAndTimestamps() {
        //given
        val reportMapHeader = ReportMapSummary(model, font)
        val bitmap = Bitmap.createBitmap(ReportGenerator.reportWidth, 200, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)
        treasuresProgress.hunterPath.addLocation(Coordinates(10.0, 10.0), Date(1))
        treasuresProgress.hunterPath.addLocation(Coordinates(10.0, 11.0), Date(1_000_000))
        treasuresProgress.hunterPath.addLocation(Coordinates(10.0, 11.0), Date(2_000_000))
        model.initialize(treasuresProgress, context)

        //when
        reportMapHeader.draw(context, canvas, 0f)

        //then
        //save to hava a reference in case of failing test
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, File(context.getFilesDir(), "/map_summary.png").outputStream())
        TestCase.assertTrue(bitmap.sameAs(expected("map_summary.png")))
    }
}