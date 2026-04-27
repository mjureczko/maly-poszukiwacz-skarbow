package pl.marianjureczko.poszukiwacz.shared.port.external

import android.Manifest
import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import pl.marianjureczko.poszukiwacz.screen.facebook.ReportStoragePort
import pl.marianjureczko.poszukiwacz.usecase.badges.Achievements
import pl.marianjureczko.poszukiwacz.usecase.badges.AchievementsStoragePort
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class ExternalStoragePort(val context: Context) : AchievementsStoragePort, ReportStoragePort {
    companion object {
        private const val JPEG = "image/jpeg"
        private const val OCTET_STREAM = "application/octet-stream"

        private val achievementsFileName: String = "DO_NOT_DELETE_little_treasure_hunter.json"
        private val achievementsDirName: String = "DO_NOT_DELETE_little_treasure_hunter/"
        private val bitmapToOutputStream: (Bitmap, OutputStream) -> Unit = { bitmap, out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, out)
        }
    }

    private val TAG = javaClass.simpleName
    private val deviceStrategy = if (isNewDevice()) {
        NewDevice(context)
    } else {
        OldDevice(context)
    }

    override fun loadAchievements(): Achievements? {
        try {
            return deviceStrategy.loadAchievementsFile()?.let {
                Json.decodeFromString<AchievementsJson>(String(it, Charsets.UTF_8)).toDomain()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Could not load achievements", e)
            return null
        }
    }

    override fun save(achievements: Achievements) {
        val dto = AchievementsJson.from(achievements)
        val json = Json.encodeToString(dto)
        val dataToSave = json.toByteArray(charset = Charsets.UTF_8)
        try {
            var folder = if (isNewDevice()) {
                Environment.DIRECTORY_DOWNLOADS + "/" + achievementsDirName
            } else {
                Environment.DIRECTORY_DOWNLOADS
            }

            deviceStrategy.saveAchievementsFile(
                folder,
                achievementsFileName,
                OCTET_STREAM
            ) { out -> out.write(dataToSave) }
        } catch (e: Exception) {
            Log.e(TAG, "Could not save achievements", e)
        }
    }

    override fun save(bitmap: Bitmap, fileName: String): Boolean {
        val name = if (fileName.contains('.')) fileName else "$fileName.jpg"

        try {
            return deviceStrategy.saveBitmapToMediaStore(
                Environment.DIRECTORY_PICTURES,
                name,
                JPEG,
                Dimensions(bitmap.width, bitmap.height)
            ) { out -> bitmapToOutputStream(bitmap, out) }
        } catch (e: Exception) {
            Log.e(TAG, "Could not save bitmap to gallery", e)
            return false
        }
    }

    private fun isNewDevice(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    interface DeviceStrategy {
        fun loadAchievementsFile(): ByteArray?
        fun saveBitmapToMediaStore(
            folderName: String?,
            fileName: String,
            mime: String,
            dimensions: Dimensions?,
            writeHandler: (OutputStream) -> Unit
        ): Boolean

        fun saveAchievementsFile(
            folderName: String,
            fileName: String,
            mime: String,
            writeHandler: (OutputStream) -> Unit
        )
    }

    private class OldDevice(val context: Context) : DeviceStrategy {

        override fun loadAchievementsFile(): ByteArray? {
            val permissionStatus = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
            if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val myAppDir = File(downloadsDir, achievementsDirName)
                val file = File(myAppDir, achievementsFileName)
                if (file.exists()) {
                    return file.readBytes()
                }
            }
            return null
        }

        override fun saveAchievementsFile(
            folderName: String,
            fileName: String,
            mime: String,
            writeHandler: (OutputStream) -> Unit
        ) {
            save(folderName, fileName, mime, writeHandler)
        }

        override fun saveBitmapToMediaStore(
            folderName: String?,
            fileName: String,
            mime: String,
            dimensions: Dimensions?,
            writeHandler: (OutputStream) -> Unit
        ): Boolean {
            save(folderName, fileName, mime, writeHandler)
            return true
        }

        private fun save(
            folderName: String?,
            fileName: String,
            mime: String,
            writeHandler: (OutputStream) -> Unit
        ) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val targetDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val appDir = if (folderName != null) {
                    File(targetDir, folderName).apply { mkdirs() }
                } else {
                    targetDir
                }
                saveToPublicDirectory(appDir, fileName, mime, writeHandler)
            }
        }

        private fun saveToPublicDirectory(
            baseDir: File?,
            fileName: String,
            mimeType: String = OCTET_STREAM,
            writeContent: (FileOutputStream) -> Unit
        ): Boolean {
            if (baseDir == null) return false
            if (!baseDir.exists() && !baseDir.mkdirs()) return false

            val file = File(baseDir, fileName)
            FileOutputStream(file).use { writeContent(it) }
            MediaScannerConnection.scanFile(context, arrayOf(file.absolutePath), arrayOf(mimeType), null)
            return true
        }
    }

    private class NewDevice(val context: Context) : DeviceStrategy {

        @RequiresApi(Build.VERSION_CODES.Q)
        override fun loadAchievementsFile(): ByteArray? {
            val resolver = context.contentResolver
            val uri = getUriToFile(resolver)
            return uri?.let { uri2 ->
                resolver.openInputStream(uri2)?.use { it.readBytes() }
            }
        }

        @RequiresApi(Build.VERSION_CODES.Q)
        override fun saveAchievementsFile(
            folderName: String,
            fileName: String,
            mime: String,
            writeHandler: (OutputStream) -> Unit
        ) {
            val resolver = context.contentResolver
            val uriOfExistingFile = getUriToFile(resolver)
            if (uriOfExistingFile == null) {
                saveToNewFile(
                    resolver,
                    fileName,
                    mime,
                    null,
                    null,
                    folderName,
                    MediaStore.Downloads.IS_PENDING,
                    MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                    writeHandler
                )
            } else {
                overrideExistingFile(resolver, uriOfExistingFile, writeHandler)
            }
        }

        @RequiresApi(Build.VERSION_CODES.Q)
        override fun saveBitmapToMediaStore(
            folderName: String?,
            fileName: String,
            mime: String,
            dimensions: Dimensions?,
            writeHandler: (OutputStream) -> Unit
        ): Boolean {
            val resolver = context.contentResolver
            return saveToNewFile(
                resolver,
                fileName,
                mime,
                dimensions!!.width,
                dimensions!!.height,
                folderName!!,
                MediaStore.Images.Media.IS_PENDING,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                writeHandler
            )
        }

        private fun saveToNewFile(
            resolver: ContentResolver,
            fileName: String,
            mimeType: String,
            width: Int?,
            height: Int?,
            relativePath: String,
            isPendingKey: String,
            contentUri: Uri,
            writeHandler: (OutputStream) -> Unit
        ): Boolean {
            val contentValues = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                put(MediaStore.Downloads.MIME_TYPE, mimeType)
                width?.let { put(MediaStore.Images.Media.WIDTH, it) }
                height?.let { put(MediaStore.Images.Media.HEIGHT, it) }
                put(MediaStore.Downloads.RELATIVE_PATH, relativePath)
                put(isPendingKey, 1)
            }
            val uri = resolver.insert(contentUri, contentValues) ?: return false
            resolver.openOutputStream(uri)?.use(writeHandler) ?: return false

            contentValues.clear()
            contentValues.put(isPendingKey, 0)
            resolver.update(uri, contentValues, null, null)
            return true
        }

        @RequiresApi(Build.VERSION_CODES.Q)
        private fun getUriToFile(resolver: ContentResolver): Uri? {
            val projection = arrayOf(MediaStore.Downloads._ID)
            val selection = "${MediaStore.Downloads.DISPLAY_NAME} = ? AND ${MediaStore.Downloads.RELATIVE_PATH} = ?"
            val selectionArgs =
                arrayOf(achievementsFileName, Environment.DIRECTORY_DOWNLOADS + "/" + achievementsDirName)

            val contentUri = MediaStore.Downloads.EXTERNAL_CONTENT_URI

            resolver.query(contentUri, projection, selection, selectionArgs, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Downloads._ID))
                    return ContentUris.withAppendedId(contentUri, id)
                }
            }
            return null
        }

        private fun overrideExistingFile(
            resolver: ContentResolver,
            uriOfExistingFile: Uri,
            writeHandler: (OutputStream) -> Unit
        ) {
            resolver.openOutputStream(uriOfExistingFile)?.use { out ->
                writeHandler.invoke(out)
            }
        }
    }

    data class Dimensions(val width: Int, val height: Int)
}