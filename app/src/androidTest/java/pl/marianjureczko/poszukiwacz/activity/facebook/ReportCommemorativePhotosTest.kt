package pl.marianjureczko.poszukiwacz.activity.facebook

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertTrue
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.shared.StorageHelper
import java.io.File
import java.util.UUID

@RunWith(AndroidJUnit4::class)
internal class ReportCommemorativePhotosTest : ReportAbstractTest() {

    //TODO t uniform tmp file management across all tests, do not delete ???
    private val tmpFile = "/bitmap.png"
    private val filesToDelete = mutableListOf<String>()
    private val seed = 1L

    @After
    fun tearDown() {
        context.deleteFile(tmpFile.replace("/", ""))
        filesToDelete.forEach { context.deleteFile(it.replace("/", "")) }
        filesToDelete.clear()
    }

    @Test
    fun shouldDrawCommemorativePhotosInSingleLine_when2() {
        //given
        val bitmap = Bitmap.createBitmap(ReportGenerator.reportWidth, 800, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)
        treasuresProgress.commemorativePhotosByTreasuresDescriptionIds.put(1, tempPhoto(800, 400))
        treasuresProgress.commemorativePhotosByTreasuresDescriptionIds.put(2, tempPhoto(600, 600))

        StorageHelper(context).save(Route(treasuresProgress.routeName))
        model.initialize(treasuresProgress, context)
        val reportPhotos = ReportCommemorativePhotos(model, font, seed)

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
        val bitmap = Bitmap.createBitmap(ReportGenerator.reportWidth, 800, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)
        treasuresProgress.commemorativePhotosByTreasuresDescriptionIds.put(1, tempPhoto(800, 400))
        treasuresProgress.commemorativePhotosByTreasuresDescriptionIds.put(2, tempPhoto(600, 600))
        treasuresProgress.commemorativePhotosByTreasuresDescriptionIds.put(3, tempPhoto(400, 800))
        treasuresProgress.commemorativePhotosByTreasuresDescriptionIds.put(4, tempPhoto(600, 500))
        model.initialize(treasuresProgress, context)
        val reportPhotos = ReportCommemorativePhotos(model, font, seed)

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