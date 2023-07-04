package pl.marianjureczko.poszukiwacz.shared

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.createBitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import androidx.core.content.res.ResourcesCompat
import pl.marianjureczko.poszukiwacz.App.Companion.getResources
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
import java.io.File
import java.io.FileInputStream
import kotlin.math.min


class FacebookReport {
    companion object {
        const val width = 1000
        fun width(): Float = width.toFloat();
        const val margin = 50f
    }

    val iconHelper = IconHelper()
    val photoHelper = PhotoHelper2()

    fun create(context: Context, progress: TreasuresProgress): Bitmap {
        val height = (photoHelper.heightOfAllPhotos(progress) + PhotoHelper2.firstRowY).toInt()
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)

        val typefacee = ResourcesCompat.getFont(context, R.font.akaya_telivigala)

        val titlePaint = Paint().apply {
            color = Color.BLUE
            textSize = 46f
            textAlign = Paint.Align.CENTER
            typeface = typefacee
        }

        val textPaint = Paint().apply {
            color = Color.BLACK
            textSize = 26f
            textAlign = Paint.Align.LEFT
            typeface = typefacee
        }

        val textPaintCenter = Paint().apply {
            color = Color.BLACK
            textSize = 26f
            textAlign = Paint.Align.CENTER
            typeface = typefacee
        }

        val headerPaint = Paint().apply {
            color = Color.BLACK
            textSize = 36f
            textAlign = Paint.Align.CENTER
            typeface = typefacee
        }

        title(canvas, titlePaint, progress)
        summary(canvas, textPaint, progress)
        if (progress.commemorativePhotosByTreasuresDescriptionIds.isNotEmpty()) {
            photosHeader(canvas, headerPaint)
            photos(canvas, textPaintCenter, progress)
        }

        return bitmap
    }

    private fun photos(canvas: Canvas, textPaint: Paint, progress: TreasuresProgress) {
        progress.commemorativePhotosByTreasuresDescriptionIds.keys.asSequence().sorted()
            .chunked(3)
            .forEachIndexed { rowIdx, row ->
                val images = row.map { id ->
                    val path = progress.commemorativePhotosByTreasuresDescriptionIds[id]
                    PhotoHelper2().scalePhoto(BitmapFactory.decodeStream(FileInputStream(File(path))))
                }
                if (row.size == 3) {
                    renderPhotoWithCaption(canvas, textPaint, rowIdx, images[0], row[0], WhichPhoto.FIRST_OF_3)
                    renderPhotoWithCaption(canvas, textPaint, rowIdx, images[1], row[1], WhichPhoto.SECOND_OF_3)
                    renderPhotoWithCaption(canvas, textPaint, rowIdx, images[2], row[2], WhichPhoto.THIRD_OF_3)
                } else if (row.size == 2) {
                    renderPhotoWithCaption(canvas, textPaint, rowIdx, images[0], row[0], WhichPhoto.LEFT_OF_2)
                    renderPhotoWithCaption(canvas, textPaint, rowIdx, images[1], row[1], WhichPhoto.RIGHT_OF_2)
                } else if (row.size == 1) {
                    renderPhotoWithCaption(canvas, textPaint, rowIdx, images[0], row[0], WhichPhoto.SINGLE)
                }
            }

//        val path = progress.commemorativePhotosByTreasuresDescriptionIds[1]
//        val image: Bitmap = BitmapFactory.decodeStream(FileInputStream(File(path)))
//        val scaledPhoto = PhotoHelper2().scalePhoto(image)
//        val y = photoHelper.calculateY(0, scaledPhoto.height)
//        canvas.drawBitmap(scaledPhoto, photoHelper.calculateX(WhichPhoto.FIRST_OF_3, scaledPhoto.width), y, null);
//        canvas.drawBitmap(scaledPhoto, photoHelper.calculateX(WhichPhoto.SECOND_OF_3, scaledPhoto.width), y, null);
//        canvas.drawBitmap(scaledPhoto, photoHelper.calculateX(WhichPhoto.THIRD_OF_3, scaledPhoto.width), y, null);
    }

    private fun renderPhotoWithCaption(canvas: Canvas, textPaint: Paint, rowIndex: Int, image: Bitmap, id: Int, whichPhoto: WhichPhoto) {
        val y1 = photoHelper.calculateY(rowIndex, image.height)
        val x1 = photoHelper.calculateX(whichPhoto, image.width)
        canvas.drawBitmap(image, x1, y1, null)
        val x1caption = photoHelper.calculateCaptionX(whichPhoto, image.width)
        val y1caption = photoHelper.calculateCaptionY(rowIndex, image.height)
        canvas.drawText("Skarb $id", x1caption, y1caption, textPaint)
    }

    private fun photosHeader(canvas: Canvas, headerPaint: Paint) {
        canvas.drawText("Nasze przygody na wyprawie", width() / 2, 250f, headerPaint)
    }

    private fun summary(canvas: Canvas, textPaint: Paint, progress: TreasuresProgress) {
        canvas.drawText(summaryText(progress), margin, 200f, textPaint)
        val gold = iconHelper.loadTreasureTypeIcon(R.drawable.gold)
        canvas.drawBitmap(gold, 425f, 170f, null);
        val diamond = iconHelper.loadTreasureTypeIcon(R.drawable.diamond)
        canvas.drawBitmap(diamond, 495f, 160f, null);
        val ruby = iconHelper.loadTreasureTypeIcon(R.drawable.ruby)
        canvas.drawBitmap(ruby, 565f, 160f, null);
    }

    private fun title(canvas: Canvas, titlePaint: Paint, progress: TreasuresProgress) {
        canvas.drawText("Mały Poszukiwacz Skarbów", width() / 2, 100f, titlePaint)
        val maxTitleLength = 20
        val routeName = if (progress.routeName.length > maxTitleLength) "${progress.routeName.subSequence(0, maxTitleLength)}..." else progress.routeName
        canvas.drawText("Raport z wyprawy, trasa \"$routeName\"", width() / 2, 150f, titlePaint)
    }

    private fun summaryText(progress: TreasuresProgress): String {
        val count = progress.numberOfCollectedTreasures()
        var treasures = "skarbów"
        if (count == 1) treasures = "skarb"
        else if (count in 2..4) treasures = "skarby"
        return "Wynik poszukiwań to $count $treasures: ${progress.golds}       ${progress.diamonds}        ${progress.rubies}"
    }
}

enum class WhichPhoto(val x: Int) {
    SINGLE(PhotoHelper2.margin + PhotoHelper2.imageWidth + PhotoHelper2.spacing),
    LEFT_OF_2(PhotoHelper2.spacingForTwo),
    RIGHT_OF_2(2 * PhotoHelper2.spacingForTwo + PhotoHelper2.imageWidth),
    FIRST_OF_3(PhotoHelper2.margin),
    SECOND_OF_3(PhotoHelper2.margin + PhotoHelper2.imageWidth + PhotoHelper2.spacing),
    THIRD_OF_3(PhotoHelper2.margin + 2 * PhotoHelper2.imageWidth + 2 * PhotoHelper2.spacing)
}

class PhotoHelper2() {
    companion object {
        const val margin = FacebookReport.margin.toInt()
        const val imageWidth = 283
        const val spacing = 25
        const val spacingForTwo = 145
        const val firstRowY = 270f
        const val imageHeight = 300f
        const val imageCaptionHeight = 50f
    }

    private val maxHeight = 300f
    private val maxWidth = 283f

    fun scalePhoto(photo: Bitmap): Bitmap {
        val widthFactor = maxWidth / photo.width
        val heightFactor = maxHeight / photo.height
        val factor = min(widthFactor, heightFactor)
        val matrix = Matrix()
        matrix.postScale(factor, factor)
        return createBitmap(photo, 0, 0, photo.width, photo.height, matrix, false)
    }

    fun calculateX(whichPhoto: WhichPhoto, photoWidth: Int): Float {
        val delta = Math.max(0, imageWidth - photoWidth) / 2
        return (whichPhoto.x + delta).toFloat()
    }

    fun calculateCaptionX(whichRow: WhichPhoto, photoWidth: Int): Float {
        val imageLeft = calculateX(whichRow, photoWidth)
        return imageLeft + (photoWidth / 2)
    }

    /**
     * @param whichRow 0-indexed
     */
    fun calculateY(whichRow: Int, photoHeight: Int): Float {
        val delta = Math.max(0f, imageHeight - photoHeight.toFloat()) / 2
        return delta + firstRowY + whichRow * (imageHeight + imageCaptionHeight)
    }

    /**
     * @param whichRow 0-indexed
     */
    fun calculateCaptionY(whichRow: Int, photoHeight: Int): Float {
        val imageBottom = calculateY(whichRow, photoHeight) + imageHeight
        return imageBottom + imageCaptionHeight / 2
    }

    fun heightOfAllPhotos(progress: TreasuresProgress): Float {
        val photos = progress.commemorativePhotosByTreasuresDescriptionIds.size
        val rows = photos / 3 + min(photos % 3, 1)
        return rows * (imageCaptionHeight + imageHeight)
    }
}

class IconHelper() {

    private val matrix = Matrix()

    init {
        matrix.postScale(0.03f, 0.03f)
    }

    fun loadTreasureTypeIcon(resource: Int): Bitmap {
        val icon: Bitmap = BitmapFactory.decodeResource(getResources(), resource)
        return createBitmap(icon, 0, 0, icon.width, icon.height, matrix, false)
    }

}