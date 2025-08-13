package pl.marianjureczko.poszukiwacz.screen.facebook

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Typeface
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.shared.PhotoScalingHelper
import java.io.File
import java.io.FileInputStream
import java.lang.System.currentTimeMillis
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

class ReportCommemorativePhotos(
    model: FacebookReportState,
    private val font: Typeface,
    rotationSeed: Long = currentTimeMillis()
) : ReportPart {
    private val photos = model.getCommemorativePhotoElements().filter { it.isSelected }
    private val random = Random(rotationSeed)
    lateinit var canvas: Canvas
    private val textPaint = ReportCommons.getTextPaint(font, Paint.Align.CENTER)
    private val headerPaint = ReportCommons.getHeaderPaint(font)

    enum class PhotoPosition(val x: Int) {
        SINGLE(ReportCommons.reportMarginAsFloat() + IMAGE_PLACEHOLDER_WIDTH.toInt() + VERTICAL_SPACING),
        LEFT_OF_2(VERTICAL_SPACING_FOR_TWO),
        RIGHT_OF_2(2 * VERTICAL_SPACING_FOR_TWO + IMAGE_PLACEHOLDER_WIDTH.toInt()),
        FIRST_OF_3(ReportCommons.reportMarginAsFloat()),
        SECOND_OF_3(ReportCommons.reportMarginAsFloat() + IMAGE_PLACEHOLDER_WIDTH.toInt() + VERTICAL_SPACING),
        THIRD_OF_3(ReportCommons.reportMarginAsFloat() + 2 * IMAGE_PLACEHOLDER_WIDTH.toInt() + 2 * VERTICAL_SPACING)
    }

    companion object {
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
            heightOfAllPhotos(photos) + ReportCommons.HEADER_FONT_SIZE + 2 * ReportCommons.HEADER_HORIZONTAL_SPACING
        }

    fun draw(context: Context, canvas: Canvas, currentTop: Float) {
        if (photos.isNotEmpty()) {
            this.canvas = canvas
            photosHeader(context, currentTop)
            photos(currentTop + ReportCommons.HEADER_FONT_SIZE + 2 * ReportCommons.HEADER_HORIZONTAL_SPACING)
        }
    }

    private fun photosHeader(context: Context, y: Float) {
        val headerTxt = context.getString(R.string.our_adventures)
        canvas.drawText(headerTxt, ReportCommons.reportWidthAsFloat() / 2, y + ReportCommons.HEADER_FONT_SIZE + ReportCommons.HEADER_HORIZONTAL_SPACING, headerPaint)
    }

    private fun photos(y: Float) {
        var currentY = y
        photos.sortedBy { it -> it.orderNumber }
            .chunked(3)
            .forEach { row ->
                val images = row.map { photoElement ->
                    PhotoScalingHelper.scalePhotoKeepRatio(BitmapFactory.decodeStream(FileInputStream(File(photoElement.photo!!))), IMAGE_PLACEHOLDER_HEIGHT, IMAGE_PLACEHOLDER_WIDTH)
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
        val rotator = Matrix()
        rotator.postRotate(random.nextFloat() * 10 - 5)
        rotator.postTranslate(x1, y1)
        canvas.drawBitmap(image, rotator, null)
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
        val imageBottom = calculatePhotoY(photoHeight, currentY) + photoHeight
        return imageBottom + IMAGE_CAPTION_PLACEHOLDER_HEIGHT / 2
    }

    private fun heightOfAllPhotos(photos: List<ElementDescription>): Float {
        val count = photos.size
        val rows = count / 3 + min(count % 3, 1)
        return rows * (rowHeight())
    }
}

