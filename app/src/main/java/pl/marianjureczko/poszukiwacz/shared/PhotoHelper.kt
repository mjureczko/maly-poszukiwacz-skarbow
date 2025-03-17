package pl.marianjureczko.poszukiwacz.shared

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.withContext
import pl.marianjureczko.poszukiwacz.shared.port.StorageHelper
import java.io.File
import java.io.FileOutputStream
import java.net.URLEncoder

class PhotoHelper(
    val context: Context,
    val storageHelper: StorageHelper
) {

    companion object {
        private val TAG = javaClass.simpleName
        const val TMP_PICTURE_FILE = "/tmp_commemorative_photo.jpg"

        /**
         * @param photoFile absolute path to graphic file
         */
        suspend fun rotateGraphicClockwise(
            ioDispatcher: CoroutineDispatcher,
            photoFile: String,
            postExecute: Runnable
        ) {
            val result = withContext(ioDispatcher) {
                val bitmap = BitmapFactory.decodeFile(photoFile)
                val matrix = Matrix()
                matrix.postRotate(90f)
                val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
                try {
                    FileOutputStream(photoFile).use { out ->
                        rotatedBitmap.compress(
                            Bitmap.CompressFormat.JPEG,
                            95,
                            out
                        )
                    }
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

        fun encodePhotoPath(photoPath: String?): String = URLEncoder.encode(photoPath ?: "", Charsets.UTF_8.name())
    }

    fun getCommemorativePhotoTempUri(): Uri = createPhotoUri(getCommemorativePhotoTempFile())

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

    fun getPhotoTempFile() = File(storageHelper.pathToRoutesDir() + "/tmp.jpg")

    suspend fun rescaleImageAndSaveInTreasure(
        photoFile: File,
        destinationPhotoFile: File,
        onSuccess: Runnable,
        onFailure: Runnable
    ) {
        val result = withContext(Dispatchers.IO) {
            val bm: Bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
            val width: Int = bm.width
            val height: Int = bm.height
            val newSizeMatrix = PhotoScalingHelper.createScalingMatrix(width, height)
            val resized = Bitmap.createBitmap(bm, 0, 0, width, height, newSizeMatrix, false)

            try {
                FileOutputStream(destinationPhotoFile.absolutePath).use { out ->
                    resized.compress(
                        Bitmap.CompressFormat.JPEG,
                        95,
                        out
                    )
                }
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

    fun createPhotoUri(photoFile: File): Uri {
        if (!photoFile.exists()) {
            photoFile.createNewFile()
        }
        return FileProvider.getUriForFile(context, context.packageName + ".fileprovider", photoFile)
    }

    fun createPhotoUri(photoFile: String): Uri = createPhotoUri(File(photoFile))
}
