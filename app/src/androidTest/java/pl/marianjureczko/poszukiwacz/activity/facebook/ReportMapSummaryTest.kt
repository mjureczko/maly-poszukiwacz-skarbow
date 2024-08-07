package pl.marianjureczko.poszukiwacz.activity.facebook

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase
import org.junit.Test
import org.junit.runner.RunWith
import pl.marianjureczko.poszukiwacz.activity.facebook.n.ROUTE_NAME
import pl.marianjureczko.poszukiwacz.activity.facebook.n.ReportCommons
import pl.marianjureczko.poszukiwacz.activity.facebook.n.ReportMapSummary
import pl.marianjureczko.poszukiwacz.model.HunterPath
import pl.marianjureczko.poszukiwacz.shared.Coordinates
import java.io.File
import java.util.Date

// Requires phone with API 34
@RunWith(AndroidJUnit4::class)
class ReportMapSummaryTest : ReportAbstractTest() {

    @Test
    fun should_drawSummaryWithLengthAndTimestamps() {
        //given
        val bitmap = Bitmap.createBitmap(ReportCommons.REPORT_WIDTH, 200, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)
        val hunterPath = HunterPath(ROUTE_NAME)
        hunterPath.addLocation(Coordinates(10.0, 10.0), Date(1))
        hunterPath.addLocation(Coordinates(10.0, 11.0), Date(1_000_000))
        hunterPath.addLocation(Coordinates(10.0, 11.0), Date(2_000_000))
//        model.initialize(context, hunterPath, treasuresProgress)
        saveEmptyProgress()
        storageHelper.save(hunterPath)
        val reportMapHeader = ReportMapSummary(createFacebookViewModel().state.value, font)

        //when
        reportMapHeader.draw(context, canvas, 0f)

        //then
        //save to hava a reference in case of failing test
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, File(context.getFilesDir(), "/map_summary.png").outputStream())
        TestCase.assertTrue(bitmap.sameAs(expected("map_summary.png")))
    }
}