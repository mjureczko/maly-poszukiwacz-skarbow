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
import android.widget.Toast
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
    private val fileName: String = "DO_NOT_DELETE_little_treasure_hunter.json"
    private val dirName: String = "DO_NOT_DELETE_little_treasure_hunter/"
    private val TAG = javaClass.simpleName

    override fun load(): Achievements? {
        try {
            val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                loadFromNewDevice()
            } else {
                loadFromOldDevice()
            }
            return data?.let {
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                saveToNewDevice(
                    fileName,
                    "application/octet-stream",
                    Environment.DIRECTORY_DOWNLOADS + "/" + dirName,
                    dataToSave
                )
            } else {
                saveToOldDevice(
                    Environment.DIRECTORY_DOWNLOADS,
                    fileName,
                    "application/octet-stream"
                ) { out ->
                    out.write(dataToSave)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Could not save achievements", e)
        }
    }

    override fun save(bitmap: Bitmap, fileName: String): Boolean {
        val name = if (fileName.contains('.')) fileName else "$fileName.jpg"

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                return saveBitmapToMediaStore(name, bitmap)
            } else {
                saveToOldDevice(
                    Environment.DIRECTORY_PICTURES,
                    name,
                    "image/jpeg"
                ) { out ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 95, out)
                }
                return true
            }
        } catch (e: Exception) {
            Log.e(TAG, "Could not save bitmap to gallery", e)
            return false
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun loadFromNewDevice(): ByteArray? {
        val resolver = context.contentResolver
        val uri = getUriToFile(resolver)
        return uri?.let { uri2 ->
            resolver.openInputStream(uri2)?.use { it.readBytes() }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun getUriToFile(resolver: ContentResolver): Uri? {
        val projection = arrayOf(MediaStore.Downloads._ID)
        val selection = "${MediaStore.Downloads.DISPLAY_NAME} = ? AND ${MediaStore.Downloads.RELATIVE_PATH} = ?"
        val selectionArgs = arrayOf(fileName, Environment.DIRECTORY_DOWNLOADS + "/" + dirName)

        val contentUri = MediaStore.Downloads.EXTERNAL_CONTENT_URI

        resolver.query(contentUri, projection, selection, selectionArgs, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Downloads._ID))
                return ContentUris.withAppendedId(contentUri, id)
            }
        }
        return null
    }

    private fun loadFromOldDevice(): ByteArray? {
        val permissionStatus = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val myAppDir = File(downloadsDir, dirName)
            val file = File(myAppDir, fileName)
            if (file.exists()) {
                return file.readBytes()
            }
        }
        return null
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveToNewDevice(displayName: String, mimeType: String, relativePath: String, data: ByteArray) {
        val resolver = context.contentResolver
        val uriOfExistingFile = getUriToFile(resolver)
        if (uriOfExistingFile == null) {
            saveToNewFile(
                resolver,
                displayName,
                mimeType,
                null,
                null,
                relativePath,
                MediaStore.Downloads.IS_PENDING,
                MediaStore.Downloads.EXTERNAL_CONTENT_URI
            ) { it.write(data) }
        } else {
            overrideExistingFile(resolver, uriOfExistingFile, data)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
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
    private fun saveBitmapToMediaStore(name: String, bitmap: Bitmap): Boolean {
        val resolver = context.contentResolver

        return saveToNewFile(
            resolver,
            name,
            "image/jpeg",
            bitmap.width,
            bitmap.height,
            Environment.DIRECTORY_PICTURES,
            MediaStore.Images.Media.IS_PENDING,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        ) {//TODO t: duplicates old device
                stream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, stream)
        }
    }

    private fun saveToOldDevice(
        folderName: String?, fileName: String, mime: String, writeHandler: (FileOutputStream) -> Unit
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

            saveToPublicDirectory(
                appDir,
                fileName,
                true,
                mime,
                writeHandler
            )
        }
    }

    private fun saveToPublicDirectory(
        baseDir: File?,
        fileName: String,
        hasGlobalPermission: Boolean,
        mimeType: String = "application/octet-stream",
        writeContent: (FileOutputStream) -> Unit
    ): Boolean {
        if (baseDir == null) return false
        if (!baseDir.exists() && !baseDir.mkdirs()) return false

        val file = File(baseDir, fileName)
        FileOutputStream(file).use { writeContent(it) }

        MediaScannerConnection.scanFile(context, arrayOf(file.absolutePath), arrayOf(mimeType), null)

        if (!hasGlobalPermission) {
            Toast.makeText(
                context,
                "Saved to app folder: ${file.absolutePath}. Grant storage permission to save into general Pictures and make it appear in Gallery.",
                Toast.LENGTH_LONG
            ).show()
        }

        return true
    }

    private fun overrideExistingFile(resolver: ContentResolver, uriOfExistingFile: Uri, data: ByteArray) {
        resolver.openOutputStream(uriOfExistingFile)?.use { stream ->
            stream.write(data)
        }
    }
}