package pl.marianjureczko.poszukiwacz.screen.facebook

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertTrue
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.shared.port.StorageHelper
import java.io.File
import java.util.UUID

//Prepared for Pixel 6a API 34
@RunWith(AndroidJUnit4::class)
internal class ReportCommemorativePhotosTest : ReportAbstractTest() {

    private val tmpFile = "/commemorative.png"
    private val filesToDelete = mutableListOf<String>()
    private val seed = 1L

    @After
    fun tearDown() {
        filesToDelete.forEach { context.deleteFile(it.replace("/", "")) }
        filesToDelete.clear()
    }

    @Test
    fun shouldDrawCommemorativePhotosInSingleLine_when2() {
        //given
        val bitmap = Bitmap.createBitmap(ReportCommons.REPORT_WIDTH, 800, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)
        val mapOfPhotos = treasuresProgress.commemorativePhotosByTreasuresDescriptionIds + mapOf(
            1 to tempPhoto(800, 400),
            2 to tempPhoto(600, 600),
        )
        treasuresProgress = treasuresProgress.copy(
            commemorativePhotosByTreasuresDescriptionIds = mapOfPhotos.toMutableMap()
        )
        storageHelper.save(treasuresProgress)
        StorageHelper(context).save(Route(treasuresProgress.routeName))
        val reportPhotos = ReportCommemorativePhotos(createFacebookViewModel().state.value, font, seed)

        //when
        reportPhotos.draw(context, canvas, 0f)

        //then
        //save to hava a reference in case of failing test
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, File(context.getFilesDir(), tmpFile).outputStream())
        assertTrue(bitmap.sameAs(expected("commemorative1.png")))
    }

    @Test
    fun shouldDrawCommemorativePhotosIn2Lines_when4() {
        //given
        val bitmap = Bitmap.createBitmap(ReportCommons.REPORT_WIDTH, 800, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)
        val mapOfPhotos = treasuresProgress.commemorativePhotosByTreasuresDescriptionIds + mapOf(
            1 to tempPhoto(800, 400),
            2 to tempPhoto(600, 600),
            3 to tempPhoto(400, 800),
            4 to tempPhoto(600, 500)
        )
        treasuresProgress = treasuresProgress.copy(
            commemorativePhotosByTreasuresDescriptionIds = mapOfPhotos.toMutableMap()
        )
        storageHelper.save(treasuresProgress)
        val reportPhotos = ReportCommemorativePhotos(createFacebookViewModel().state.value, font, seed)

        //when
        reportPhotos.draw(context, canvas, 0f)

        //then
        //save to hava a reference in case of failing test
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, File(context.getFilesDir(), tmpFile).outputStream())
        assertTrue(bitmap.sameAs(expected("commemorative2.png")))
    }

    private fun tempPhoto(width: Int, height: Int): String {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.BLUE)
        val fileName = "/${UUID.randomUUID()}.png"
        filesToDelete.add(fileName)
        val file = File(context.getFilesDir(), fileName)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, file.outputStream())
        return file.absolutePath
    }
}