package pl.marianjureczko.poszukiwacz.shared

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.withContext
import pl.marianjureczko.poszukiwacz.activity.treasureseditor.TreasuresEditorActivity
import java.io.File
import java.io.FileOutputStream
import kotlin.math.max
import kotlin.math.min

class PhotoHelper(
    val context: Context,
    val storageHelper: StorageHelper
) {

    companion object {
        private val TAG = javaClass.simpleName
        const val TMP_PICTURE_FILE = "/tmp_commemorative_photo.jpg"

        //TODO: merge with other functions
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

        fun calculateScalingFactor(width: Int, height: Int): Float {
            val greater = max(width, height).toFloat()
            val wanted = 800F
            return if (greater < 800) {
                1F;
            } else {
                wanted / greater;
            }
        }

        //TODO: private?
        fun createScalingMatrix(width: Int, height: Int): Matrix {
            val scalingFactor = calculateScalingFactor(width, height)
            val matrix = Matrix()
            matrix.postScale(scalingFactor, scalingFactor)
            return matrix
        }

        /**
         * @param photoFile absolute path to graphic file
         */
        suspend fun rotateGraphicClockwise(photoFile: String, postExecute: Runnable) {
            val result = withContext(Dispatchers.IO) {
                val bitmap = BitmapFactory.decodeFile(photoFile)
                val matrix = Matrix()
                matrix.postRotate(90f)
                val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
                try {
                    FileOutputStream(photoFile).use { out -> rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 95, out) }
                    true
                } catch (e: Exception) {
                    Log.e(TAG, e.message, e)
                    false
                }
            }
            if (result) {
                postExecute.run()
            }
        }
    }

    fun createCommemorativePhotoTempUri(): Uri = createPhotoUri(getCommemorativePhotoTempFile())

    /**
     * @return absolute path to the commemorative photo
     */
    fun moveCommemorativePhotoToPermanentLocation(target: String? = null): String {
        val permanentCommemorativePhotoFile = target ?: this.storageHelper.newCommemorativePhotoFile()
        getCommemorativePhotoTempFile().let { source ->
            source.copyTo(target = File(permanentCommemorativePhotoFile), overwrite = true)
            source.delete()
        }
        return permanentCommemorativePhotoFile
    }

    fun createTipPhotoUri(): Uri = createPhotoUri(getPhotoTempFile())

    /** visibility for tests */
    fun getCommemorativePhotoTempFile() = File(this.storageHelper.pathToRoutesDir() + TMP_PICTURE_FILE)

    fun getPhotoTempFile() = File(storageHelper.pathToRoutesDir() + TreasuresEditorActivity.TMP_PICTURE_FILE)

    suspend fun rescaleImageAndSaveInTreasure(photoFile: File, destinationPhotoFile: File, onSuccess: Runnable, onFailure: Runnable) {
        val result = withContext(Dispatchers.IO) {
            val bm: Bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
            val width: Int = bm.width
            val height: Int = bm.height
            val newSizeMatrix = createScalingMatrix(width, height)
            val resized = Bitmap.createBitmap(bm, 0, 0, width, height, newSizeMatrix, false)

            try {
                FileOutputStream(destinationPhotoFile.absolutePath).use { out -> resized.compress(Bitmap.CompressFormat.JPEG, 95, out) }
            } catch (e: Exception) {
                Log.e(TAG, e.message, e)
                false
            }
            true
        }
        if (result) {
            onSuccess.run()
        } else {
            onFailure.run()
        }
    }

    private fun createPhotoUri(photoFile: File): Uri {
        if (!photoFile.exists()) {
            photoFile.createNewFile()
        }
        return FileProvider.getUriForFile(context, "pl.marianjureczko.poszukiwacz.fileprovider", photoFile)
    }
}

