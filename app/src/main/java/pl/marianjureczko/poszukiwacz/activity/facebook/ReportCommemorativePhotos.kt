package pl.marianjureczko.poszukiwacz.activity.facebook

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import pl.marianjureczko.poszukiwacz.shared.PhotoHelper
import java.io.File
import java.io.FileInputStream
import kotlin.math.max
import kotlin.math.min

class ReportCommemorativePhotos(
    model: FacebookViewModel,
    private val font: Typeface
) : ReportPart {
    private val photos = model.getCommemorativePhotoElements().filter { it.isSelected }
    lateinit var canvas: Canvas
    private val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 26f
        textAlign = Paint.Align.CENTER
        typeface = font
    }
    private val headerPaint = Paint().apply {
        color = Color.BLACK
        textSize = HEADER_FONT_SIZE
        textAlign = Paint.Align.CENTER
        typeface = font
    }

    enum class PhotoPosition(val x: Int) {
        SINGLE(ReportGenerator.margin() + IMAGE_PLACEHOLDER_WIDTH.toInt() + VERTICAL_SPACING),
        LEFT_OF_2(VERTICAL_SPACING_FOR_TWO),
        RIGHT_OF_2(2 * VERTICAL_SPACING_FOR_TWO + IMAGE_PLACEHOLDER_WIDTH.toInt()),
        FIRST_OF_3(ReportGenerator.margin()),
        SECOND_OF_3(ReportGenerator.margin() + IMAGE_PLACEHOLDER_WIDTH.toInt() + VERTICAL_SPACING),
        THIRD_OF_3(ReportGenerator.margin() + 2 * IMAGE_PLACEHOLDER_WIDTH.toInt() + 2 * VERTICAL_SPACING)
    }

    companion object {
        private const val HEADER_FONT_SIZE = 36f
        private const val HORIZONTAL_SPACING = 10f

        private const val VERTICAL_SPACING = 25
        private const val VERTICAL_SPACING_FOR_TWO = 145
        private const val IMAGE_PLACEHOLDER_WIDTH = 283f
        private const val IMAGE_PLACEHOLDER_HEIGHT = 300f
        private const val IMAGE_CAPTION_PLACEHOLDER_HEIGHT = 50f
    }

    override fun height(): Float =
        if (photos.isEmpty()) {
            0f
        } else {
            heightOfAllPhotos(photos) + HEADER_FONT_SIZE + 2 * HORIZONTAL_SPACING
        }

    fun draw(canvas: Canvas, currentTop: Float) {
        if (photos.isNotEmpty()) {
            this.canvas = canvas
            photosHeader(currentTop)
            photos(currentTop + HEADER_FONT_SIZE + 2 * HORIZONTAL_SPACING)
        }
    }

    private fun photosHeader(y: Float) {
        canvas.drawText("Nasze przygody na wyprawie", ReportGenerator.width() / 2, y + HEADER_FONT_SIZE + HORIZONTAL_SPACING, headerPaint)
    }

    private fun photos(y: Float) {
        var currentY = y
        photos.sortedBy { it -> it.orderNumber }
            .chunked(3)
            .forEach { row ->
                val images = row.map { photoElement ->
                    PhotoHelper.scalePhotoKeepRatio(BitmapFactory.decodeStream(FileInputStream(File(photoElement.photo!!))), IMAGE_PLACEHOLDER_HEIGHT, IMAGE_PLACEHOLDER_WIDTH)
                }
                when (row.size) {
                    3 -> {
                        renderPhotoWithCaption(images[0], row[0].description, PhotoPosition.FIRST_OF_3, currentY)
                        renderPhotoWithCaption(images[1], row[1].description, PhotoPosition.SECOND_OF_3, currentY)
                        renderPhotoWithCaption(images[2], row[2].description, PhotoPosition.THIRD_OF_3, currentY)
                    }

                    2 -> {
                        renderPhotoWithCaption(images[0], row[0].description, PhotoPosition.LEFT_OF_2, currentY)
                        renderPhotoWithCaption(images[1], row[1].description, PhotoPosition.RIGHT_OF_2, currentY)
                    }

                    1 -> {
                        renderPhotoWithCaption(images[0], row[0].description, PhotoPosition.SINGLE, currentY)
                    }
                }
                currentY += rowHeight()
            }
    }

    private fun renderPhotoWithCaption(image: Bitmap, description: String, whichPhoto: PhotoPosition, y: Float) {
        val y1 = calculatePhotoY(image.height, y)
        val x1 = calculatePhotoX(whichPhoto, image.width)
        canvas.drawBitmap(image, x1, y1, null)
        val x1caption = calculateCaptionX(whichPhoto, image.width)
        val y1caption = calculateCaptionY(image.height, y)
        canvas.drawText(description, x1caption, y1caption, textPaint)
    }

    private fun rowHeight() = IMAGE_CAPTION_PLACEHOLDER_HEIGHT + IMAGE_PLACEHOLDER_HEIGHT

    private fun calculatePhotoX(whichPhoto: PhotoPosition, photoWidth: Int): Float {
        val delta = max(0, IMAGE_PLACEHOLDER_WIDTH.toInt() - photoWidth) / 2
        return (whichPhoto.x + delta).toFloat()
    }

    private fun calculateCaptionX(whichRow: PhotoPosition, photoWidth: Int): Float {
        val imageLeft = calculatePhotoX(whichRow, photoWidth)
        return imageLeft + (photoWidth / 2)
    }

    private fun calculatePhotoY(photoHeight: Int, currentY: Float): Float {
        val delta = max(0f, IMAGE_PLACEHOLDER_HEIGHT - photoHeight.toFloat()) / 2
        return delta + currentY
    }

    private fun calculateCaptionY(photoHeight: Int, currentY: Float): Float {
        val imageBottom = calculatePhotoY(photoHeight, currentY) + IMAGE_PLACEHOLDER_HEIGHT
        return imageBottom + IMAGE_CAPTION_PLACEHOLDER_HEIGHT / 2
    }

    private fun heightOfAllPhotos(photos: List<ElementDescription>): Float {
        val count = photos.size
        val rows = count / 3 + min(count % 3, 1)
        return rows * (rowHeight())
    }
}

