package pl.marianjureczko.poszukiwacz.shared

import android.graphics.Bitmap
import android.graphics.Matrix
import kotlin.math.max
import kotlin.math.min

object PhotoScalingHelper {

    //TODO t: add suspend
    //TODO t: try to test with https://robolectric.org/androidx_test/
    /**
     * Scales the photo, but keeps the aspect ratio
     * @param maxHeight - the maximum height after scaling
     * @param maxWidth - the maximum width after scaling
     */
    fun scalePhotoKeepRatio(photo: Bitmap, maxHeight: Float, maxWidth: Float): Bitmap {
        val widthFactor = maxWidth / photo.width
        val heightFactor = maxHeight / photo.height
        val factor = min(widthFactor, heightFactor)
        val matrix = Matrix()
        matrix.postScale(factor, factor)
        return Bitmap.createBitmap(photo, 0, 0, photo.width, photo.height, matrix, false)
    }

    /**
     * Keeps aspect ratio, but scales down so that the greater dimension goes down to 800 px
     * @param matrix for unit tests
     */
    fun createScalingMatrix(imageWidth: Int, imageHeight: Int, matrix: Matrix = Matrix()): Matrix {
        val scalingFactor = scalingFactorToGoDownTo800px(imageWidth, imageHeight)
        matrix.postScale(scalingFactor, scalingFactor)
        return matrix
    }

    private fun scalingFactorToGoDownTo800px(width: Int, height: Int): Float {
        val greater = max(width, height).toFloat()
        val wanted = 800F
        return if (greater < 800) {
            1F;
        } else {
            wanted / greater;
        }
    }
}