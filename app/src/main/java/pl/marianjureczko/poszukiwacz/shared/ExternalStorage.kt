package pl.marianjureczko.poszukiwacz.shared

import android.content.ContentResolver
import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import pl.marianjureczko.poszukiwacz.activity.facebook.FacebookActivity
import java.io.FileOutputStream

class ExternalStorage {
    private val compressFormat = Bitmap.CompressFormat.JPEG

    //TODO: what about invalid characters in name? see StorageHelper.getRouteName
    fun saveImage(name: String, image: Bitmap, resolver: ContentResolver): SavingResult {
        var saveSuccessful = false
        var fileName: String? = null
        val imagesCollection = imagesCollection()
        val imageDetails = creteImage(name)
        resolver.insert(imagesCollection, imageDetails)?.let { imageUri ->
            resolver.openFileDescriptor(imageUri, "w", null)?.let { pfd ->
                val outputStream = FileOutputStream(pfd.fileDescriptor)
                image.compress(compressFormat, 100, outputStream)
                pfd.close()
                saveSuccessful = true
                fileName = getImageFileName(resolver, imageUri)
            }
            closeFile(imageDetails, resolver, imageUri)
        }
        return SavingResult(saveSuccessful, fileName)
    }

    private fun imagesCollection(): Uri =
        if (Build.VERSION.SDK_INT >= FacebookActivity.STORAGE_DO_NOT_REQUIRE_PERMISSONS) {
            MediaStore.Images.Media.getContentUri(
                MediaStore.VOLUME_EXTERNAL_PRIMARY
            )
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

    private fun creteImage(name: String): ContentValues =
        ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "$name.${compressFormat.name.lowercase()}")
            if (Build.VERSION.SDK_INT >= FacebookActivity.STORAGE_DO_NOT_REQUIRE_PERMISSONS) {
                put(MediaStore.Audio.Media.IS_PENDING, 1)
            }
        }

    private fun getImageFileName(resolver: ContentResolver, imageUri: Uri): String? {
        var fileName: String? = null
        val cursor = resolver.query(imageUri, arrayOf(MediaStore.MediaColumns.DISPLAY_NAME), null, null, null)
        if (cursor != null && cursor.moveToFirst()) {
            fileName = cursor.getString(0)
            cursor.close()
        }
        return fileName
    }

    private fun closeFile(imageDetails: ContentValues, resolver: ContentResolver, imageUri: Uri) {
        if (Build.VERSION.SDK_INT >= FacebookActivity.STORAGE_DO_NOT_REQUIRE_PERMISSONS) {
            imageDetails.clear()
            imageDetails.put(MediaStore.Audio.Media.IS_PENDING, 0)
            resolver.update(imageUri, imageDetails, null, null)
        }
    }
}

data class SavingResult(val success: Boolean, val fileName: String?){
}