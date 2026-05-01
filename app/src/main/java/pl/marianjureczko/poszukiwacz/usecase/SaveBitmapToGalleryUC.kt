package pl.marianjureczko.poszukiwacz.usecase

import android.graphics.Bitmap
import pl.marianjureczko.poszukiwacz.screen.facebook.ReportStoragePort

class SaveBitmapToGalleryUC(val storage: ReportStoragePort) {

    operator fun invoke(bitmap: Bitmap, fileName: String): Boolean {
        return storage.save(bitmap, fileName)
    }
}