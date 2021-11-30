package pl.marianjureczko.poszukiwacz.activity.treasureseditor

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Log
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.shared.StorageHelper
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.math.max

class PhotoHelper(private val storageHelper: StorageHelper?) {
    private val TAG = javaClass.simpleName

    companion object {
        fun calculateScalingFactor(width: Int, height: Int): Float {
            val greater = max(width, height).toFloat()
            val wanted = 800F
            return if (greater < 800) {
                1F;
            } else {
                wanted / greater;
            }
        }
    }

    fun rescaleImageAndSaveInTreasure(photoFile: File, treasureDescription: TreasureDescription): Boolean {
        val bm: Bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
        val width: Int = bm.width
        val height: Int = bm.height
        val matrix = createScalingMatrix(width, height)
        val resized = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false)

        val destinationPhotoFile = treasureDescription.instantiatePhotoFile(storageHelper!!)
        try {
            FileOutputStream(destinationPhotoFile.absolutePath).use { out ->
                resized.compress(Bitmap.CompressFormat.JPEG, 95, out)
            }
        } catch (e: IOException) {
            Log.d(TAG, e.message, e)
            return false
        }
        return true
    }

    private fun createScalingMatrix(width: Int, height: Int): Matrix {
        val scalingFactor = calculateScalingFactor(width, height)
        val matrix = Matrix()
        matrix.postScale(scalingFactor, scalingFactor)
        return matrix
    }
}