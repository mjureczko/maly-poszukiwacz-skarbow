package pl.marianjureczko.poszukiwacz.screen.facebook

import android.graphics.Bitmap

interface ReportStoragePort {

    /**
     * Save a bitmap into the Pictures collection so gallery apps can see it.
     * Works on both pre-Q and Q+ devices.
     */
    fun save(bitmap: Bitmap, fileName: String): Boolean
}